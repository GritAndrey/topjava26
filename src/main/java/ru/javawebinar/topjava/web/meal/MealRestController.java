package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        final int userId = authUserId();
        log.info("Create meal: {}; user id: {}", meal, userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        final int userId = authUserId();
        log.info("Delete meal with id: {}; user id: {}", id, userId);
        service.delete(id, userId);
    }

    public Meal get(int id) {
        final int userId = authUserId();
        log.info("get meal: {}; user id: {}", id, userId);
        return service.get(id, userId);
    }

    public void update(Meal meal, int id) {
        final int userId = authUserId();
        log.info("update meal: {}; user id: {}", meal, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public List<MealTo> getAll() {
        final int userId = authUserId();
        log.info("get all meals. user id: {}", userId);
        return service.getAll(userId, SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllByDate(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        final int userId = authUserId();
        log.info("user id: {} Filter meals. StartDate:{} EndDate:{} StartTime: {} EndTime: {} ", userId, startDate,
                endDate, startTime, endTime);
        return service.getAllByDate(userId, startDate, startTime, endDate, endTime, SecurityUtil.authUserCaloriesPerDay());
    }
}