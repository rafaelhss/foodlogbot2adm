package com.foodlog.foodlog.processor.bodylog;

import com.foodlog.domain.BodyLog;
import com.foodlog.foodlog.openCV.PeopleDetector;
import com.foodlog.foodlog.processor.Processor;
import com.foodlog.foodlog.util.Util;
import com.foodlog.repository.BodyLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

/**
 * Created by rafael on 08/11/17.
 */
@Component
public class BodyLogProcessor extends Processor  {

    @Autowired
    private BodyLogFactory bodyLogFactory;

    @Autowired
    private PeopleDetector peopleDetector;

    @Autowired
    private BodyLogRepository bodyLogRepository;

    private Util util = new Util();


    @Override
    public void process() {
        byte[] photo = bodyLogFactory.getPicture(update);
        byte[] imagePeopleBytes = peopleDetector.getPeopleInPhoto(photo);

        System.out.println("imagePeopleBytes: " + imagePeopleBytes);

        if(imagePeopleBytes != null) {
            BodyLog bodyLog = new BodyLog();
            //byte[] imageBytes = new MealLogFactory().getPicture(update);
            bodyLog.setPhoto(DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(imagePeopleBytes)));
            bodyLog.setPhotoContentType("image/jpg");
            bodyLog.setBodyLogDatetime(update.getUpdateDateTime());
            bodyLog.setUser(getCurrentUser(update));
            bodyLog.setUpdateId(update.getUpdate_id());

            System.out.println("antes");

            bodyLogRepository.save(bodyLog);

            System.out.println("depois");

            sendMessage("Body Log salvo com sucesso. Vou mandar");

            System.out.println("depois send");

            try {
                URL url = new URL("https://foodlogbotimagebatch.herokuapp.com/bodypanel?userid=" + getCurrentUser(update).getId());
                util.performHttpGet(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean check() {
        if (update.getMessage().getPhoto() != null && update.getMessage().getPhoto().size() > 0) {
            byte[] photo = bodyLogFactory.getPicture(update);
            byte[] imagePeopleBytes = peopleDetector.getPeopleInPhoto(photo);
            return (imagePeopleBytes != null);
        } else {
            return false;
        }
    }

    public void setBodyLogFactory(BodyLogFactory bodyLogFactory) {
        this.bodyLogFactory = bodyLogFactory;
    }

    public void setUtil(Util util) {
        this.util = util;
    }
}
