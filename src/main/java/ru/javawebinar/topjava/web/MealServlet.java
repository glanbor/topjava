package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private final MealDao mealDao = new MealDaoInMemoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");

        if (action == null) {
            log.debug("getAll");
            request.setAttribute("meals", MealsUtil.getUnfilteredListWithExcess(mealDao.getAll()));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equals("create")) {
            Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 100);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealInfo.jsp").forward(request, response);
        } else if (action.equals("update")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = mealDao.get(id);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealInfo.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            log.debug("delete id = {}", id);
            mealDao.delete(id);
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Integer id = request.getParameter("id").isEmpty() ? null : Integer.valueOf(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(id, dateTime, description, calories);

        log.debug(id == null ? "Create meal={}" : "Update meal={}", meal);
        mealDao.save(meal);
        response.sendRedirect("meals");
    }

}
