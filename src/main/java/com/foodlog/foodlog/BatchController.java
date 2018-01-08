package com.foodlog.foodlog;

import com.foodlog.domain.*;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rafael on 09/11/17.
 */
@RestController
@RequestMapping("/api/batch")
public class BatchController {

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
    @RequestMapping("/scheduled-meals")
    public List<ScheduledMeal> getUserTelegram(@RequestParam(value="minute-window", defaultValue = "30") Long userid) {
        //TODO Ajustar quando mudar o tipo do target time
        return scheduledMealRepository.findByOrderByTargetTimeDesc()
            .stream()
            .filter(meal -> checkTime(meal))
            .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/scheduled-meals")
    public List<ScheduledMeal> getAllScheduledMeals(@RequestParam(value="minute-window", defaultValue = "30") Long userid) {
        //TODO Ajustar quando mudar o tipo do target time
        return scheduledMealRepository.findByOrderByTargetTimeDesc()
            .stream()
            .filter(meal -> checkTime(meal))
            .collect(Collectors.toList());
    }

    private boolean checkTime(ScheduledMeal scheduledMeal){

        try {
            String time[] = scheduledMeal.getTargetTime().split(":");

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));

            System.out.println("teste");
            System.out.println(ZonedDateTime.now());
            System.out.println(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));


            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            ZonedDateTime target = now.with(LocalTime.of(hour, minute));

            ZonedDateTime after = target.plusMinutes(20);
            ZonedDateTime before = target.minusMinutes(20);

            System.out.println("datas: now[" + now + "] target[" + target + "] before[" + before + "] after[" + after + "] Return:" + (now.isBefore(after) && now.isAfter(before)));

            return (now.isBefore(after) && now.isAfter(before));
        } catch (Exception ex){
            System.out.println("errrxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

}
