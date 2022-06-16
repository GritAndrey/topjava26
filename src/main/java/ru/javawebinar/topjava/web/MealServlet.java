package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.MemoryMealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MealServlet extends HttpServlet {
    public static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    MealStorage storage;

    @Override
    public void init() throws ServletException {
        storage = new MemoryMealStorage();
        log.info("init");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operation = request.getParameter("action");
        switch (operation == null ? "null" : operation) {
            case "delete":
                String mealId = request.getParameter("id");
                log.info("Delete meal: {} ", mealId);
                storage.delete(Integer.parseInt(mealId));
                response.sendRedirect("meals");
                return;
            case "update":
                mealId = request.getParameter("id");
                log.info("Update meal: {} ", mealId);
                Meal meal = storage.get(Integer.parseInt(mealId));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
                break;
            case "add":
                meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0);
                log.info("Add new");
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
                break;
            case "null":
            default:
                log.info("Get all meals from storage");
                request.setAttribute("meals", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        final LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        final String description = request.getParameter("description");
        final int calories = Integer.parseInt(request.getParameter("calories"));
        String mealId = (request.getParameter("id"));
        final Meal meal = new Meal(mealId == null || mealId.isEmpty() ? null : Integer.parseInt(mealId), dateTime, description, calories);
        log.info("doPost {}", meal);
        storage.save(meal);
        response.sendRedirect("meals");
    }
}
