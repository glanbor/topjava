package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.Profiles.POSTGRES_DB;

@ActiveProfiles({POSTGRES_DB, DATAJPA})
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getWithUser() throws Exception {
        Meal meal = service.getWithUser(MealTestData.MEAL1_ID, UserTestData.USER_ID);
        MealTestData.MEAL_MATCHER.assertMatch(meal, MealTestData.meal1);
        UserTestData.USER_MATCHER.assertMatch(meal.getUser(), UserTestData.user);
    }

    @Test(expected = NotFoundException.class)
    public void getWithUserNotFound() throws Exception {
        service.getWithUser(9999, UserTestData.USER_ID);
    }
}
