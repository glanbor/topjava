package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            User user = adminUserController.create(new User("userName", "email@mail.ru", "password", Role.ADMIN));
            System.out.println(user.toString());
            System.out.println();
            List<User> allUsers = adminUserController.getAll();
            System.out.println(allUsers);
            System.out.println();
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            List<MealTo> all = mealRestController.getAll();
            System.out.println(all);
            System.out.println();
            System.out.println(mealRestController.get(1));
            System.out.println();
            Meal meal = mealRestController.create(new Meal(LocalDateTime.now(), "new Meal", 999, 1));
            System.out.println(meal.toString());
            System.out.println();
            System.out.println(mealRestController.getAll());
            mealRestController.delete(15);
            System.out.println();
            System.out.println(mealRestController.getAll());
            System.out.println();
            List<MealTo> filtered = mealRestController.getBetween(null, null, null, null);
            filtered.forEach(System.out::println);
        }
    }
}
