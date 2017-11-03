package com.foodlog.foodlog.processor.photo;

import com.foodlog.foodlog.bot.telegram.factory.MealLogFactory;
import com.foodlog.foodlog.openCV.PeopleDetector;
import com.foodlog.foodlog.processor.Processor;
import org.springframework.stereotype.Component;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class PhotoProcessor extends Processor{


    private PeopleDetector peopleDetector = new PeopleDetector();
    private MealLogFactory mealLogFactory = new MealLogFactory();

    @Override
    public void process() {
        System.out.println("Process " + this.getClass().getName());
        sendMessage("Process " + this.getClass().getName());
    }

    @Override
    public boolean check() {
        if (update.getMessage().getPhoto() != null && update.getMessage().getPhoto().size() > 0) {
            byte[] photo = mealLogFactory.getPicture(update);
            byte[] imagePeopleBytes = peopleDetector.getPeopleInPhoto(photo);
            if(imagePeopleBytes != null){
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public void setMealLogFactory(MealLogFactory mealLogFactory) {
        this.mealLogFactory = mealLogFactory;
    }

    public void setPeopleDetector(PeopleDetector peopleDetector) {
        this.peopleDetector = peopleDetector;
    }
}
