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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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



}
