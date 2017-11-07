package com.foodlog.foodlog.processor.timeline;

import com.foodlog.foodlog.processor.Processor;
import com.foodlog.foodlog.util.Util;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class TimelineProcessor extends Processor {

    private Util util = new Util();

    @Override
    public void process() {
        sendMessage("Sua Timeline sera gerada...");

        try {
            URL url = new URL("https://foodlogbotimagebatch.herokuapp.com/timeline?userid=" + getCurrentUser(update).getId());

            util.performHttpGet(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean check() {
        try {
            return update.getMessage().getText().trim().toLowerCase().equals("timeline");
        } catch (Exception e) {
            return false;
        }
    }

    //para testes
    public void setUtil(Util util) {
        this.util = util;
    }
}
