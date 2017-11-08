package com.foodlog.foodlog.service;

import com.foodlog.foodlog.bot.telegram.model.Update;
import com.foodlog.foodlog.processor.Processor;
import com.foodlog.foodlog.processor.jaca.JacaProcessor;
import com.foodlog.foodlog.processor.none.NoneProcessor;
import com.foodlog.foodlog.processor.photo.PhotoProcessor;
import com.foodlog.foodlog.processor.prox.ProxProcessor;
import com.foodlog.foodlog.processor.rating.RatingProcessor;
import com.foodlog.foodlog.processor.textlog.TextLogProcessor;
import com.foodlog.foodlog.processor.timeline.TimelineProcessor;
import com.foodlog.foodlog.processor.undo.UndoProcessor;
import com.foodlog.foodlog.processor.weight.WeightProcessor;
import com.foodlog.foodlog.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rafael on 27/10/17.
 */
@Service
public class UpdateService {

    @Autowired
    public ProxProcessor proxProcessor;
    @Autowired
    public NoneProcessor noneProcessor;
    @Autowired
    public WeightProcessor weightProcessor;
    @Autowired
    public TimelineProcessor timelineProcessor;
    @Autowired
    public TextLogProcessor textLogProcessor;
    @Autowired
    public UndoProcessor undoProcessor;
    @Autowired
    public JacaProcessor jacaProcessor;
    @Autowired
    public RatingProcessor ratingProcessor;
    @Autowired
    public PhotoProcessor photoProcessor;


    private List<Processor> processors;


    private void init(){
        processors = new ArrayList<>();

        processors.add(proxProcessor);
        processors.add(noneProcessor);
        processors.add(weightProcessor);
        processors.add(timelineProcessor);
        processors.add(textLogProcessor);
        processors.add(undoProcessor);
        processors.add(jacaProcessor);
        processors.add(ratingProcessor);
        processors.add(photoProcessor);
    }


    public void processUpdate(Update update){
        update = adjustTime(update);
        if(processors ==  null){
            init();
        }
        for (Processor processor : processors){
            processor.setUpdate(update);
            if(processor.check()){
                processor.process();
            }
        }
    }


    protected Update adjustTime(Update update) {
        String regex = "([0-1]\\d|2[0-3]):([0-5]\\d)";

        if(Util.checkRegex(update, regex)){  //verifica xx:xx)

            Pattern r = Pattern.compile(regex);

            String text = update.getMessage().getText();
            if(text == null || text.trim().equals("")){
                text = update.getMessage().getCaption();
            }

            if(text != null && !text.trim().equals("")) {
                Matcher m = r.matcher(text);
                m.find();

                String newtime = m.group(0);


                for (int i = 0; i < m.groupCount(); i++) {
                    System.out.println("group " + i + ":" + m.group(i));
                }




                String time[] = newtime.split(":");

                ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));

                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                ZonedDateTime target = now.with(LocalTime.of(hour, minute));

                System.out.println("m.replaceAll(\"\"):" + m.replaceAll(""));
                update.setUpdateDateTime(Instant.from(target));
                update.getMessage().setCaption(m.replaceAll("").trim());
                update.getMessage().setText(m.replaceAll("").trim());
                return update;
            }
        }

        update.setUpdateDateTime(Instant.now());
        return update;

    }


}
