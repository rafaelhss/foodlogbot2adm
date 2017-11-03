package com.foodlog.foodlog.sender;

import com.foodlog.foodlog.bot.telegram.sender.Sender;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by rafael on 03/11/17.
 */
public class TestSender extends Sender {
    public TestSender() {
        super("botId");
    }
    @Override
    public void sendResponse(Integer id, String text_response) throws IOException {
        text_response = URLEncoder.encode(text_response, "UTF-8");
        URL url = new URL(UrlTemplate.replace("@@CHATID@@", id.toString()).replace("@@TEXT@@", text_response));

        System.out.println("#### Chamando: " + url.toString());
    }
}
