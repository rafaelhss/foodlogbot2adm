package com.foodlog.foodlog.processor.timeline;

import com.foodlog.foodlog.processor.Processor;
import org.springframework.stereotype.Component;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class TimelineProcessor extends Processor {
    @Override
    public void process() {
        System.out.println("Process " + this.getClass().getName());
        sendMessage("Process " + this.getClass().getName());
    }

    @Override
    public boolean check() {
        try {
            return update.getMessage().getText().trim().toLowerCase().equals("timeline");
        } catch (Exception e) {
            return false;
        }
    }
}
