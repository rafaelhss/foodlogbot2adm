package com.foodlog.foodlog.processor.none;

import com.foodlog.foodlog.processor.Processor;
import org.springframework.stereotype.Component;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class NoneProcessor extends Processor {
       @Override
    public void process() {
           System.out.println("Process " + this.getClass().getName());
           sendMessage("Process " + this.getClass().getName());
    }

    @Override
    public boolean check() {
        return false;
    }
}
