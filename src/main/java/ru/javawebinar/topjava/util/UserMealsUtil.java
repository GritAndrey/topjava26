package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
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

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByRecursion(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByRecursion(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByRecursion(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                               int caloriesPerDay) {
        List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        recursionFilter(meals, startTime, endTime, caloriesPerDay, 0, new HashMap<>(), mealWithExcesses);
        return mealWithExcesses;
    }

    private static void recursionFilter(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                        int caloriesPerDay, int currentIndex, Map<LocalDate, Integer> caloriesDateGrouped,
                                        List<UserMealWithExcess> mealWithExcesses) {
        if (currentIndex == meals.size()) {
            return;
        }

        caloriesDateGrouped.merge(meals.get(currentIndex).getDate(), meals.get(currentIndex).getCalories(), Integer::sum);
        recursionFilter(meals, startTime, endTime, caloriesPerDay, ++currentIndex, caloriesDateGrouped, mealWithExcesses);

        UserMeal meal = meals.get(currentIndex - 1);
        if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
            mealWithExcesses.add(new UserMealWithExcess(
                    meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories(),
                    caloriesDateGrouped.get(meal.getDate()) > caloriesPerDay)
            );
        }
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesDateGrouped = new HashMap<>();
        for (UserMeal meal : meals) {
            final LocalDate mlDate = meal.getDate();
            caloriesDateGrouped.put(mlDate, caloriesDateGrouped.getOrDefault(mlDate, 0) + meal.getCalories());
        }

        List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcesses.add(new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesDateGrouped.get(meal.getDate()) > caloriesPerDay)
                );
            }
        }
        return mealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesDateGrouped = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate,
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(),
                        caloriesDateGrouped.get(m.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
