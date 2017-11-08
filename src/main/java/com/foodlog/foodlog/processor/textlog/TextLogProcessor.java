package com.foodlog.foodlog.processor.textlog;

import com.foodlog.domain.MealLog;
import com.foodlog.foodlog.processor.photo.MealLogFactory;
import com.foodlog.foodlog.processor.Processor;
import com.foodlog.foodlog.processor.photo.PhotoProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class TextLogProcessor extends Processor {
    @Autowired
    MealLogFactory mealLogFactory;

    @Autowired
    PhotoProcessor photoProcessor;

    @Override
    public void process() {
        MealLog mealLog = mealLogFactory.createTextLog(update, getCurrentUser(update));
        photoProcessor.setUpdate(update);

        String message = photoProcessor.saveMealLogAndGenerateMessage(mealLog);

        sendMessage(message);
    }

    @Override
    public boolean check() {
        try {
            return update.getMessage().getText().toLowerCase().indexOf("meal:") == 0;
        } catch (Exception e){
            return false;
        }

    }
}
