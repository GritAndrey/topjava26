package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping()
    public String getMeals(Model model) {
        model.addAttribute("meals", super.getAll());
        return "/meals";
    }

    @GetMapping("{id}")
    public String get(@PathVariable Integer id, Model model) {
        model.addAttribute("meal", super.get(id));
        model.addAttribute("method", "PUT");
        log.info("Get mealForm for update meal with id: {}", id);
        return "mealForm";
    }

    @GetMapping("/new")
    public String newMeal(Model model) {
        model.addAttribute("meal", new Meal());
        model.addAttribute("method", "POST");
        log.info("Get mealForm for create new meal.");
        return "mealForm";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        log.info("Filtering mills");
        return "meals";
    }

    @PostMapping()
    public String create(HttpServletRequest request) {
        final Meal meal = getMealFromRequest(request);
        super.create(meal);
        return "redirect:/meals";
    }

    @PutMapping()
    public String update(HttpServletRequest request) {
        super.update(getMealFromRequest(request), Integer.parseInt(request.getParameter("id")));
        return "redirect:/meals";
    }

    private Meal getMealFromRequest(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
