package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

public class DataJpaUserServiceTest extends AbstractServiceTest {
    @Autowired
    private UserService service;

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.meals);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }
}
