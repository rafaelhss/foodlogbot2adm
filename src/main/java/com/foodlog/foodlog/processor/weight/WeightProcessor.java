package com.foodlog.foodlog.processor.weight;

import com.foodlog.foodlog.processor.Processor;
import com.foodlog.foodlog.util.Util;
import org.springframework.stereotype.Component;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class WeightProcessor extends Processor{
    @Override
    public void process() {
        System.out.println("Process " + this.getClass().getName());
        sendMessage("Process " + this.getClass().getName());
    }

    @Override
    public boolean check() {
        return Util.checkRegex(update, "^[+-]?([0-9]*[.])+[0-9]+$");
    }
}
