package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATETIME_DESC = Sort.by(Sort.Direction.DESC, "dateTime");
    private final CrudMealRepository mealRepository;
    private final CrudUserRepository userRepository;

    @Autowired
    public DataJpaMealRepository(CrudMealRepository mealRepository, CrudUserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getReferenceById(userId));

        if (meal.isNew()) {
            return mealRepository.save(meal);
        } else if (get(meal.id(), userId) == null) {
            return null;
        }
        return mealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealRepository.findById(id).orElse(null);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAll(SORT_DATETIME_DESC).stream().filter(m -> m.getUser().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepository.findAllByDateTimeIsGreaterThanEqualAndDateTimeBeforeAndUserId(startDateTime, endDateTime, userId, SORT_DATETIME_DESC);
    }
}
