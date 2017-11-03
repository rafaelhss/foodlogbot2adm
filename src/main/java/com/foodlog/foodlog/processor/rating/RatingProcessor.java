package com.foodlog.foodlog.processor.rating;

import com.foodlog.foodlog.processor.Processor;
import com.foodlog.foodlog.util.Util;
import org.springframework.stereotype.Component;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class RatingProcessor extends Processor{
    @Override
    public void process() {
        System.out.println("Process " + this.getClass().getName());
        sendMessage("Process " + this.getClass().getName());
    }

    @Override
    public boolean check() {
        if(Util.checkRegex(update, "^[0-5]+$")){

/*            MealLog mealLog = mealLogRepository
                    .findTop1ByUserOrderByMealDateTimeDesc(getCurrentUser(update));
            Instant refDate = mealLog.getMealDateTime();

            Weight weight = weightRepository
                    .findTop1ByUserOrderByWeightDateTimeDesc(getCurrentUser(update));
            if(weight.getWeightDateTime().isAfter(refDate)){
                return false;
            }

            BodyLog bodyLog = bodyLogRepository
                    .findTop1ByUserOrderByBodyLogDatetimeDesc(getCurrentUser(update));
            if(bodyLog.getBodyLogDatetime().isAfter(refDate)){
                return false;
            }
*/
                return true;

            } else return false;

    }
}
