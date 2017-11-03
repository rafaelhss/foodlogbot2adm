package com.foodlog.foodlog.processor.photo;

import com.foodlog.FoodlogbotadmApp;
import com.foodlog.foodlog.bot.telegram.factory.MealLogFactory;
import com.foodlog.foodlog.bot.telegram.model.Message;
import com.foodlog.foodlog.bot.telegram.model.Photo;
import com.foodlog.foodlog.bot.telegram.model.Update;
import com.foodlog.foodlog.bot.telegram.model.User;
import com.foodlog.foodlog.openCV.PeopleDetector;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rafael on 03/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodlogbotadmApp.class)
public class PhotoProcessorTest {
    public static Integer CHAT_ID_RAFA = 153350155;

    @Autowired
    private PhotoProcessor photoProcessor;

    @Test
    public void process() throws Exception {

    }

    @Test
    public void checkFalse() throws Exception {

        Update update = getUpdate();

        InputStream imagem = this.getClass().getResourceAsStream("/images/body.jpg");
        byte[] bytes = ByteStreams.toByteArray(imagem);



        MealLogFactory mealLogFactory = Mockito.mock(MealLogFactory.class);
        Mockito.when(mealLogFactory.getPicture(update)).thenReturn(bytes);

        // PeopleDetector peopleDetector = Mockito.mock(PeopleDetector.class);
        // Mockito.when(peopleDetector.getPeopleInPhoto(bytes)).thenReturn(bytes);

        // photoProcessor.setPeopleDetector(peopleDetector);
        photoProcessor.setMealLogFactory(mealLogFactory);
        photoProcessor.setUpdate(update);

        Assert.assertEquals(false, photoProcessor.check());



    }
    @Test
    public void checkTrue() throws Exception {

        Update update = getUpdate();

        InputStream imagem = this.getClass().getResourceAsStream("/images/meal.jpg");
        byte[] bytes = ByteStreams.toByteArray(imagem);


        MealLogFactory mealLogFactory = Mockito.mock(MealLogFactory.class);
        Mockito.when(mealLogFactory.getPicture(update)).thenReturn(bytes);

        photoProcessor.setMealLogFactory(mealLogFactory);
        photoProcessor.setUpdate(update);

        Assert.assertEquals(true, photoProcessor.check());
    }

    private Update getUpdate() {
        Update update = new Update();
        update.setUpdate_id(1L);

        Message message = new Message();

        List<Photo> photos = new ArrayList<>();
        Photo photo = new Photo();
        photo.setFile_id("1");


        photos.add(photo);
        message.setPhoto(photos);

        User from = new User();
        from.setId(CHAT_ID_RAFA);
        message.setFrom(from);
        update.setMessage(message);
        update.setUpdateDateTime(Instant.now());
        return update;
    }

}
