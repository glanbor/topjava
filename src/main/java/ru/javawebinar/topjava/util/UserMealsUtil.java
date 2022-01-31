package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println("---------------------------------------------------------------------------------------------------");
        List<UserMealWithExcess> mealsTo2 = filteredByFutureTask(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------------------");
        List<UserMealWithExcess> mealsTo3 = filteredByOneCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo3.forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------------------------------");
        List<UserMealWithExcess> mealsTo4 = filteredByStreamsInOnePath(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo4.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dateCaloriesMap = new HashMap<>();
        for (UserMeal meal : meals) {
            dateCaloriesMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDate locDate = meal.getDateTime().toLocalDate();
            LocalTime locTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(locTime, startTime, endTime)) {
                mealsWithExcess.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), dateCaloriesMap.get(locDate) > caloriesPerDay));
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dateCaloriesMap = meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        dateCaloriesMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByFutureTask(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dateCaloriesMap = new HashMap<>();
        List<Callable<UserMealWithExcess>> callableTasks = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDate locDate = meal.getDateTime().toLocalDate();
            LocalTime locTime = meal.getDateTime().toLocalTime();
            dateCaloriesMap.merge(locDate, meal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(locTime, startTime, endTime)) {
                callableTasks.add(() -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), dateCaloriesMap.get(locDate) > caloriesPerDay));
            }
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            List<Future<UserMealWithExcess>> futures = executorService.invokeAll(callableTasks);
            executorService.shutdown();
            for (Future<UserMealWithExcess> future : futures) {
                mealsWithExcess.add(future.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByOneCycle(List<UserMeal> meals, LocalTime startTime,
                                                              LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> dateCaloriesMap = new HashMap<>();
        recurseForFilteredByOneCycle(userMealWithExcess, startTime, endTime, caloriesPerDay,
                dateCaloriesMap, new LinkedList<>(meals));
        return userMealWithExcess;
    }

    private static void recurseForFilteredByOneCycle(List<UserMealWithExcess> userMealWithExcess, LocalTime startTime,
                                                     LocalTime endTime, int caloriesPerDay,
                                                     Map<LocalDate, Integer> dateCaloriesMap, LinkedList<UserMeal> userMeals) {
        if (userMeals.isEmpty()) return;
        UserMeal userMeal = userMeals.pop();
        LocalDate locDate = userMeal.getDateTime().toLocalDate();
        LocalTime locTime = userMeal.getDateTime().toLocalTime();

        dateCaloriesMap.merge(locDate, userMeal.getCalories(), Integer::sum);
        recurseForFilteredByOneCycle(userMealWithExcess, startTime, endTime, caloriesPerDay, dateCaloriesMap, userMeals);

        if (TimeUtil.isBetweenHalfOpen(locTime, startTime, endTime)) {
            userMealWithExcess.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                    userMeal.getCalories(), dateCaloriesMap.get(locDate) > caloriesPerDay));
        }
    }

    public static List<UserMealWithExcess> filteredByStreamsInOnePath(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        class MealsCollector implements Collector<UserMeal, ArrayList<UserMeal>, Stream<UserMealWithExcess>> {
            private int caloriesAccumulator;

            @Override
            public Supplier<ArrayList<UserMeal>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<ArrayList<UserMeal>, UserMeal> accumulator() {
                return (userMeals, e) -> {
                    if (TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime))
                        userMeals.add(e);
                    caloriesAccumulator += e.getCalories();
                };
            }

            @Override
            public BinaryOperator<ArrayList<UserMeal>> combiner() {
                return (l, r) -> {
                    l.addAll(r);
                    return l;
                };
            }

            @Override
            public Function<ArrayList<UserMeal>, Stream<UserMealWithExcess>> finisher() {
                return meals -> meals.stream().map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(), userMeal.getCalories(),
                        caloriesAccumulator > caloriesPerDay));
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.CONCURRENT);
            }
        }

        return meals.stream().collect(new MealsCollector()).collect(Collectors.toList());

    }
}
