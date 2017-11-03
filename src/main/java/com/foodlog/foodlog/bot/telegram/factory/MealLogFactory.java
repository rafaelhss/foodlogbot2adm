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


    public byte[] getPicture(Update update) {
        int id = update.getMessage().getPhoto().size() -1 ;
        String file_id = update.getMessage().getPhoto().get(id).getFile_id();

        RestTemplate restTemplate = new RestTemplate();
        URI getFileurl = ApiUrlBuilder.getGetFile(file_id);
        System.out.println("url:" + getFileurl.toString());
        GetFile getFile = (GetFile) restTemplate.getForObject(getFileurl, GetFile.class);


        System.out.println("result:" + getFile.getResult().getFile_path());

        //https://api.telegram.org/file/bot<token>/<file_path>

        String file_path = getFile.getResult().getFile_path();
        URI getBytesurl = ApiUrlBuilder.getBytesUrl(file_path);
        return restTemplate.getForObject(getBytesurl, byte[].class);
    }
}
