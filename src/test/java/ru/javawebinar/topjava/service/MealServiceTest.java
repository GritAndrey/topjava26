package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getNew;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        final Meal actual = service.get(meal0.getId(), USER_ID);
        assertMatch(actual, meal0);

    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getInvalidAuthorization() {
        assertThrows(NotFoundException.class, () -> service.get(admMeal0.getId(), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(meal0.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(meal0.getId(), USER_ID));
    }

    @Test
    public void deleteWrongId() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteInvalidAuthorization() {
        assertThrows(NotFoundException.class, () -> service.delete(admMeal1.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 31),
                        LocalDate.of(2020, Month.JANUARY, 31), USER_ID),
                meal6, meal5, meal4, meal3);
    }

    @Test
    public void existDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, admMeal0.getDateTime(),
                        "Meal with existing DateTime", 0), ADMIN_ID));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), userMeals);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(admMeal0.getId(), ADMIN_ID), updated);
    }

    @Test
    public void updateInvalidAuthorization() {
        assertThrows(NotFoundException.class, () -> service.update(meal0, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }
}