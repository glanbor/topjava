package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealDao {
    Collection<Meal> getAll();

    Meal get(int id);

    Meal save(Meal meal);

    boolean delete(int id);
}
