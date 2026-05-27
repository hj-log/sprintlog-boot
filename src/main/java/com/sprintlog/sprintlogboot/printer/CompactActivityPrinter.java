package com.sprintlog.sprintlogboot.printer;

import com.sprintlog.sprintlogboot.domain.*;
import org.springframework.stereotype.*;

@Component("compact")
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
