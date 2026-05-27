package etc.fileio.serial.printer;

import etc.fileio.serial.domain.*;

public class CompactActivityPrinter implements ActivityPrinter {

    @Override
    public void print(LearningActivity activity) {
        System.out.println(
                activity.getActivityType()
                        + " | " + activity.getTitle()
                        + " (" + activity.getMinutes() + "분)"
        );
    }

}
