package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemoryImpl implements MealDao {
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public MealDaoInMemoryImpl() {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Collection<Meal> getAll() {
        return mealMap.values();
    }

    @Override
    public Meal get(int id) {
        return mealMap.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(idCounter.incrementAndGet());
        }
        return mealMap.put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int id) {
        return mealMap.remove(id) != null;
    }
}
