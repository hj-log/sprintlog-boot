package etc.fileio.serial.repository;

import etc.fileio.serial.domain.*;
import etc.fileio.serial.domain.*;
import etc.fileio.serial.domain.ActivityCategory;
import etc.fileio.serial.domain.LearningActivity;
import etc.fileio.serial.domain.LectureLog;
import etc.fileio.serial.domain.PracticeLog;
import etc.fileio.serial.domain.ReadingLog;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

/*
 특정 타입의 학습 활동만 담는 제네릭 레포지토리
 */
public class ActivityRepository<T extends LearningActivity> {

    /** CSV 파일의 컬럼 순서. 헤더 행과 데이터 행 모두 이 순서를 따른다. */
    private static final String CSV_HEADER =
            "type,title,minutes,visibility,tags,instructorName,completionRate,bookTitle";

    private final List<T> storage = new ArrayList<>();

    public void add(T activity) {
        if(activity == null) {
            throw new IllegalArgumentException("저장할 활동은 null일 수 없습니다.");
        }
        storage.add(activity);
    }

    // 저장된 모든 활동을 반환한다.
    public List<T> findAll() {
        return Collections.unmodifiableList(storage); // 외부에서 함부러 수정못하게하기 위해서
    }

    // 조건에 맞는 활동들만 골라 변환한다.
    public List<T> filter(Predicate<T> predicate) {
       List<T> result = new ArrayList<>();
        for (T activity : storage) {
            if(predicate.test(activity)) {
                result.add(activity);
            }
        }
        return result;
    }

    // 조건에 맞는 첫번째 활동만 골라 변환한다.
    // 반환값이 T로 하면 조건을 하나도 만족하지 못하면 리턴을 하지 못하고 반복문이 끝나버린다. -> null을 리턴함
    // Optional로 포장해서 줌 -> 그래서 포장한걸 확인하고 값을 꺼내기 -> null을 보내지 않기 위해서 Optional 사용
    public Optional<T> findfirst(Predicate<T> predicate) {
        for (T activity : storage) {
            if(predicate.test(activity)) {
                return Optional.of(activity);
            }
        }
        return Optional.empty();
    }

    // 저장된 활동 수를 반환한다.
    public int count() {
        return storage.size();
    }

    // 저장된 모든 활동의 총 학습 시간(분)을 반환한다.
    public int getTotalMinutes() {
        int total = 0;
        for (T activity : storage) {
            total += activity.getMinutes(); // T가 LearningActivity의 자식이기 때문에 getTotalMinutes()을 호출할 수 있음.
        }
        return total;
    }

    // CSV 영속화 -----------------------------------------------------------------------

    // 저장소에 모든 활동 객체를 CSV 파일로 저장한다.
    public void saveToFile(Path csvPath) throws IOException { // 경로 정보를 담아줄 수 있는 타입의 객체로 보내세요 -> Path
        Path parent = csvPath.getParent(); // 경로가 실제로 존재하는지 확인하기 위해 부모경로를 부름
        if (parent != null) {
            Files.createDirectories(parent); // .createDirectories() 예외처리가 필요함, try-catch 안 쓸거면 throws 필수
        }

        // 파일 입출력을 담당하는 BufferedWriter(문자 기반 스트림)
        // 첫번째 매개값: 파일 경로, 두번째 매개값: 문자열 인코딩 방식 (한글 작성 시 UTF-8)
        // newBufferedWriter 이 친구도 예외처리가 필요함.
        // try-with-resource: AutoCloseable 인터페이스의 구현체인 경우 자동으로 close()를 진행해주는 문법
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8)) {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (T activity : storage) {
                writer.write((toCsvRow(activity)));
                writer.newLine();
            }
        }

    }

    // CSV 파일을 읽어 LearningActivity 레포지토리로 복원
    public static ActivityRepository<LearningActivity> loadFromFile(Path csvPath) throws IOException {

        ActivityRepository<LearningActivity> repository = new ActivityRepository<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            reader.readLine();// 첫 줄을 읽는 메서드 (한 줄을 읽어주는 메서드), 헤더 행 건너뛰기 - 다음 줄을 읽기는 해야 되는데, 변수에 담지는 않겠다.

            String line;
            // reader.readLine()을 사용하여 한 행을 읽어 들어서 line 할당한 그 결과가 null이 아니라면 true
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                LearningActivity activity = parseCsvRow(line);
                repository.add(activity);
            }
        }
        return repository;
    }
    // LECTURE,Stream 이론,55,PUBLIC,이론;stream,박코치,,
    private static LearningActivity parseCsvRow(String line) throws IOException {
        // 두번째 매개값 -1: 끝에 오는 빈 필드도 결과 배열에 포함시킨다.
        // split은 빈 문자열은 배열에 포함을 안시켜서 두번째 매개값으로 -1 넣음.
        String[] cols = line.split(",", -1);
        if (cols.length < 8 ) {
            throw new IOException("CSV 컬럼 수가 부족합니다. (8개가 필요, 실제 " + cols.length +"개");
        }

        String type = cols[0];
        String title = cols[1];
        int minutes;
        try {
            minutes = Integer.parseInt(cols[2]);
        } catch (NumberFormatException e) {
            throw new IOException("minutes 컬럼이 정수가 아닙니다: " + cols[2], e);
        }

        Visibility visibility;
        try {
            visibility = Visibility.valueOf(cols[3]);
        } catch (IllegalArgumentException e) {
            throw new IOException("알 수 없는 visibility 값: " + cols[3], e);
        }

        String tagsField = cols[4];
        String instructorName = cols[5];
        String completionRateField = cols[6];
        String bookTitle = cols[7];

        ActivityCategory category;
        try {
            category = ActivityCategory.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IOException("알 수 없는 활동 유형: " + type, e);
        }

        LearningActivity activity;
        switch (category) {
            case LECTURE:
                activity = new LectureLog(title, minutes, visibility, instructorName);
                break;
            case PRACTICE:
                int completionRate;
                try {
                    completionRate = Integer.parseInt(completionRateField);
                } catch (NumberFormatException e) {
                    throw new IOException("completionRate가 정수가 아닙니다: "
                            + completionRateField, e);
                }
                activity = new PracticeLog(title, minutes, visibility, completionRate);
                break;
            case READING:
                activity = new ReadingLog(title, minutes, visibility, bookTitle);
                break;
            default:
                throw new IOException("처리할 수 없는 활동 유형: " + type);
        }

        // 태그 복원
        if (!tagsField.isBlank()) {
            for (String tag : tagsField.split(";")) {
                if (!tag.isBlank()) {
                    activity.addTag(tag);
                }
            }
        }

        return activity;


    }

    /**
     * 활동을 CSV 한 행으로 직렬화한다.
     */
    private String toCsvRow(T activity) {
        String type = activity.getCategory().name();
        String title = activity.getTitle();
        String minutes = String.valueOf(activity.getMinutes());
        String visibility = activity.getVisibility().name();
        String tags = String.join(";", activity.getTags());

        String instructorName = "";
        String completionRate = "";
        String bookTitle = "";

        if (activity instanceof LectureLog) {
            instructorName = ((LectureLog) activity).getInstructorName();
        } else if (activity instanceof PracticeLog) {
            completionRate = String.valueOf(((PracticeLog) activity).getCompletionRate());
        } else if (activity instanceof ReadingLog) {
            bookTitle = ((ReadingLog) activity).getBookTitle();
        }

        return String.join(",",
                type, title, minutes, visibility, tags,
                instructorName, completionRate, bookTitle);
    }

    public void saveToBinary(Path binaryPath) throws IOException {

        Path parent = binaryPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(binaryPath)))) {
            oos.writeObject(new ArrayList<>(storage));
        }
    }

    public static ActivityRepository<LearningActivity> loadFromBinary(Path binaryPath) throws IOException, ClassNotFoundException {
        ActivityRepository<LearningActivity> repository = new ActivityRepository<>();

        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(binaryPath)))) {
            List<LearningActivity> list = (List<LearningActivity>) ois.readObject();
            for (LearningActivity a : list) {
                repository.add(a);
            }
        }

        return repository;
    }
}


