package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.Profiles.POSTGRES_DB;

@ActiveProfiles({POSTGRES_DB, DATAJPA})
public class DataJpaMealServiceTest extends MealServiceTest {
}
