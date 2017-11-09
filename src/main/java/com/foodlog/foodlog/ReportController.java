package com.foodlog.foodlog;

import com.foodlog.domain.*;
import com.foodlog.foodlog.gateway.service.UpdateService;
import com.foodlog.foodlog.gateway.telegram.model.Update;
import com.foodlog.foodlog.report.bodylog.BodyLogImage;
import com.foodlog.foodlog.report.bodylog.BodyLogService;
import com.foodlog.foodlog.report.timeline.MealLogDayService;
import com.foodlog.foodlog.report.timeline.dayStats.DayStats;
import com.foodlog.foodlog.report.timeline.dayStats.DayStatsService;
import com.foodlog.repository.JacaRepository;
import com.foodlog.repository.ScheduledMealRepository;
import com.foodlog.repository.UserRepository;
import com.foodlog.repository.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by rafael on 09/11/17.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BodyLogService bodyLogService;

    @Autowired
    private JacaRepository jacaRepository;

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private ScheduledMealRepository scheduledMealRepository;

    @Autowired
    private DayStatsService dayStatsService;

    @Autowired
    private MealLogDayService mealLogDayService;


    @CrossOrigin(origins = "*")
    @RequestMapping("/body-log")
    public BodyLogImage getBodyPanel(@RequestParam(value="userid") Long userid,
                                     @RequestParam(defaultValue = "panel", value="image-type") String type) {

        User user = userRepository.findOne(userid);

        if(type != null && type.trim().equals("gif")){
            return bodyLogService.getBodyGif(user);
        }
        return bodyLogService.getBodyPanel(user);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/weights")
    public List<Weight> listWeights(@RequestParam(value="userid") Long userid) {
        User user = userRepository.findOne(userid);
        return weightRepository.findTop30ByUserOrderByWeightDateTimeDesc(user);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/jacas")
    public List<Jaca> listJacass(@RequestParam(value="userid") Long userid) {
        User user = userRepository.findOne(userid);
        return jacaRepository.findTop30ByUserOrderByJacaDateTime(user);
    }


    @CrossOrigin(origins = "*")
    @RequestMapping("/meal-log")
    public List<MealLog> getAllMealLogDays(@RequestParam(value="userid") Long userid) {
        return mealLogDayService.findAll(userRepository.findOne(userid));
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/scheduled-meals")
    public List<ScheduledMeal> getAllScheduledMeals(@RequestParam(value="userid") Long userid) {
        return scheduledMealRepository.findByUserOrderByTargetTime(userRepository.findOne(userid));
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/day-stats")
    public DayStats getDayStats(@RequestParam(value="userid") Long userid){

        return dayStatsService.generateStats(userRepository.findOne(userid));
    }
}
