package com.foodlog.foodlog.util;

import com.foodlog.foodlog.bot.telegram.model.Update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rafael on 01/11/17.
 */
public class Util {
    public static boolean checkRegex(Update update, String regex){
        // Create a Pattern object
        Pattern r = Pattern.compile(regex);

        // Now create matcher object.
        if(update.getMessage().getText() != null) {
            Matcher m = r.matcher(update.getMessage().getText());
            return m.find();
        }
        if(update.getMessage().getCaption() != null) {
            Matcher m = r.matcher(update.getMessage().getCaption());
            return m.find();
        }
        return false;
    }

}
