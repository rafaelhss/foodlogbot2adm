package com.foodlog.foodlog;

import com.foodlog.domain.*;
import com.foodlog.foodlog.report.bodylog.BodyLogImage;
import com.foodlog.foodlog.report.bodylog.BodyLogService;
import com.foodlog.foodlog.report.timeline.MealLogDayService;
import com.foodlog.foodlog.report.timeline.dayStats.DayStats;
import com.foodlog.foodlog.report.timeline.dayStats.DayStatsService;
import com.foodlog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private ScheduledMealRepository scheduledMealRepository;

    @Autowired
    private UserTelegramRepository userTelegramRepository;

    @Autowired
    private UserRepository userRepository;


    @CrossOrigin(origins = "*")
    @RequestMapping("/user-telegrams/{user-id}")
    public UserTelegram getUserTelegram(@PathVariable(value="user-id") Long userId) {

        System.out.println("Userid:" + userId);
        User user = userRepository.findOne(userId);

        System.out.println("User: " + user);

        return userTelegramRepository.findOneByUser(user);

    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/scheduled-meals")
    public List<ScheduledMeal> getAllScheduledMeals(@RequestParam(value="minute-window", defaultValue = "30") Long minuteWindow) {
        //TODO Ajustar quando mudar o tipo do target time
        //TODO usar time window
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
