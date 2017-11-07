package com.foodlog.foodlog.bot.telegram.factory;


import com.foodlog.domain.MealLog;
import com.foodlog.domain.ScheduledMeal;
import com.foodlog.domain.User;
import com.foodlog.foodlog.bot.telegram.ApiUrlBuilder;
import com.foodlog.foodlog.bot.telegram.model.GetFile;
import com.foodlog.foodlog.bot.telegram.model.Update;
import com.foodlog.repository.ScheduledMealRepository;
import com.foodlog.repository.UserTelegramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.net.URI;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by rafael on 04/06/17.
 */
@Service
public class MealLogFactory {

    public static final int DEFAULT_RATING = 3;
    @Autowired
    private ScheduledMealRepository scheduledMealRepository;

    public byte[] getPicture(Update update) {
        int id = update.getMessage().getPhoto().size() -1 ;
        String file_id = update.getMessage().getPhoto().get(id).getFile_id();

        RestTemplate restTemplate = new RestTemplate();
        URI getFileurl = ApiUrlBuilder.getGetFile(file_id);
        GetFile getFile = (GetFile) restTemplate.getForObject(getFileurl, GetFile.class);

        //https://api.telegram.org/file/bot<token>/<file_path>

        String file_path = getFile.getResult().getFile_path();
        URI getBytesurl = ApiUrlBuilder.getBytesUrl(file_path);
        return restTemplate.getForObject(getBytesurl, byte[].class);
    }

    public MealLog create(Update update, User current) {

        MealLog mealLog = getBaseMealLog(update, current);

        //Photo
        byte[] imageBytes = getPicture(update);
        mealLog.setPhoto(DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(imageBytes)));
        mealLog.setPhotoContentType("image/jpg");

        return mealLog;
    }
    private MealLog getBaseMealLog(Update update, User current){
        MealLog mealLog = new MealLog();

        //Date = now
        mealLog.setMealDateTime(update.getUpdateDateTime());

        //Comment = caption
        String caption = update.getMessage().getCaption();
        if(caption != null) {
            mealLog.setComment((caption.trim()));
        }

        //ScheduledMeal
        ScheduledMeal scheduledMeal = defineScheduledMeal(mealLog, current);
        mealLog.setScheduledMeal(scheduledMeal);

        mealLog.setUpdateId(update.getUpdate_id());

        //Valor Default
        mealLog.setRating(DEFAULT_RATING);

        mealLog.setUser(current);

        return mealLog;
    }

    private ScheduledMeal defineScheduledMeal(MealLog mealLog, User currentUser) {
        try {

            if(mealLog.getComment() != null && !mealLog.getComment().isEmpty()){
                List<ScheduledMeal> scheduledMeals = scheduledMealRepository.findByNameAndUser(mealLog.getComment(), currentUser);
                if(scheduledMeals != null && !scheduledMeals.isEmpty()){
                    return scheduledMeals.get(0);
                }
            }


            for (ScheduledMeal scheduledMeal : scheduledMealRepository.findAll()) {
                if(checkTime(scheduledMeal, mealLog.getMealDateTime())) {
                    return scheduledMeal;
                }
            }
        } catch (Exception ex){
            System.out.println("errrxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private boolean checkTime(ScheduledMeal scheduledMeal, Instant mealDateTime)  {
        String time[] = scheduledMeal.getTargetTime().split(":");

        ZonedDateTime now = mealDateTime.atZone(ZoneId.of("America/Sao_Paulo"));

        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        ZonedDateTime target = now.with(LocalTime.of(hour, minute));

        ZonedDateTime after = target.plusMinutes(30);
        ZonedDateTime before = target.minusMinutes(30);

        return (target.isBefore(after) && target.isAfter(before));
    }

    //para testes
    public void setScheduledMealRepository(ScheduledMealRepository scheduledMealRepository) {
        this.scheduledMealRepository = scheduledMealRepository;
    }
}
