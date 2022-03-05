package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.JPA;
import static ru.javawebinar.topjava.Profiles.POSTGRES_DB;

@ActiveProfiles({POSTGRES_DB, JPA})
public class JPAUserServiceTest extends UserServiceTest{
}
