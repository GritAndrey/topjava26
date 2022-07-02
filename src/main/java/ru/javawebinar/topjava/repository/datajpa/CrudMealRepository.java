package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Meal m WHERE m.id=:id
            AND m.user.id=:user_id
            """)
    int delete(@Param("id") int id, @Param("user_id") int userId);

    List<Meal> findAllByDateTimeIsGreaterThanEqualAndDateTimeBeforeAndUserId(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId, Sort sortDatetimeDesc);
}
