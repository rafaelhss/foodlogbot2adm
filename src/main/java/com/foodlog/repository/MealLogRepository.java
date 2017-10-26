package com.foodlog.repository;

import com.foodlog.domain.MealLog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the MealLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MealLogRepository extends JpaRepository<MealLog,Long> {

    @Query("select meal_log from MealLog meal_log where meal_log.user.login = ?#{principal.username}")
    List<MealLog> findByUserIsCurrentUser();

}
