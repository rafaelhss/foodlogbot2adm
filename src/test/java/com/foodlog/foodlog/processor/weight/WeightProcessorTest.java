package com.foodlog.foodlog.processor.weight;

import com.foodlog.FoodlogbotadmApp;
import com.foodlog.foodlog.bot.telegram.model.Message;
import com.foodlog.foodlog.bot.telegram.model.Update;
import com.foodlog.foodlog.bot.telegram.model.User;
import com.foodlog.foodlog.bot.telegram.sender.Sender;
import com.foodlog.foodlog.util.TestUtil;
import com.foodlog.foodlog.util.Util;
import com.foodlog.repository.WeightRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.time.Instant;
import java.util.Random;

/**
 * Created by rafael on 07/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodlogbotadmApp.class)
public class WeightProcessorTest {
    private static final Long UPDATE_ID = new Random().nextLong();
    public static Integer CHAT_ID_RAFA = 153350155;

    @Autowired
    WeightProcessor weightProcessor;

    @Autowired
    TestUtil testUtil;

    @Mock
    Sender senderMock;

    @Spy
    Util utilMock;

    @Autowired
    WeightRepository weightRepository;

    @Before
    public void setup() {
        testUtil.createUser();
    }



    @Test
    public void process() throws Exception {

        weightProcessor.setUpdate(getUpdate("94.5"));
        Mockito.doNothing().when(senderMock).sendResponse(null, "");
        weightProcessor.setSender(senderMock);

        Mockito.doNothing().when(utilMock).performHttpGet(Mockito.any());
        weightProcessor.setUtil(utilMock);


        long countBefore = weightRepository.count();

        weightProcessor.process();


        Mockito.verify(senderMock, Mockito.times(1)).sendResponse(Mockito.eq(153350155), Mockito.any());

        URL url = new URL("https://foodlogbotimagebatch.herokuapp.com/?userid=" + testUtil.getUser().getId());
        Mockito.verify(utilMock, Mockito.times(1)).performHttpGet(Mockito.eq(url));


        Assert.assertEquals(countBefore + 1, weightRepository.count());


    }

    @Test
    public void checkPonto() throws Exception {
        Update update = getUpdate("94.5");


        weightProcessor.setUpdate(update);
        Mockito.doNothing().when(senderMock).sendResponse(null, "");
        weightProcessor.setSender(senderMock);

        Assert.assertEquals(true, weightProcessor.check());


    }

    private Update getUpdate(String text) {
        Update update = new Update();
        update.setUpdate_id(1L);

        Message message = new Message();
        message.setText(text);
        User from = new User();
        from.setId(CHAT_ID_RAFA);
        message.setFrom(from);
        update.setMessage(message);
        update.setUpdateDateTime(Instant.now());
        return update;
    }

    @Test
    public void checkVirgula() throws Exception {
        Update update = getUpdate("94,5");


        weightProcessor.setUpdate(update);
        Mockito.doNothing().when(senderMock).sendResponse(null, "");
        weightProcessor.setSender(senderMock);

        Assert.assertEquals(true, weightProcessor.check());


    }



}
