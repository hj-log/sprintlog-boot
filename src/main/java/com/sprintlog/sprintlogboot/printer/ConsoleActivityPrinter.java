package etc.fileio.serial.printer;

import etc.fileio.serial.domain.*;

public class ConsoleActivityPrinter implements ActivityPrinter {

    @Override
    public void print(LearningActivity activity) {
        System.out.println(
                "[" + activity.getActivityType() + "]"
                        + " #" + activity.getId()
                        + " " + activity.getTitle()
                        + " - " + activity.getMinutes() + "분"
                        + " - " + activity.getDetailText()
                        + " - " + activity.getVisibilityText() + " 🙏"
        );
    }

}
