package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.*;

@ActiveProfiles({POSTGRES_DB, JDBC})
public class JdbcMealServiceTest extends MealServiceTest {
}
