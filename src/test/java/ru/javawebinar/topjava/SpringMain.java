package ru.javawebinar.topjava;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.jpa.JpaUserRepository;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpringMain {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // java 7 automatic resource management (ARM)
        System.setProperty("spring.profiles.active", "postgres,jpa");
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml")) {
            System.out.println("Bean definition names: ");
            final String[] beanDefinitionNames = appCtx.getBeanDefinitionNames();
            Arrays.stream(beanDefinitionNames).forEach(System.out::println);
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            System.out.println();

            MealRestController mealController = appCtx.getBean(MealRestController.class);
            List<MealTo> filteredMealsWithExcess =
                    mealController.getBetween(
                            LocalDate.of(2020, Month.JANUARY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2020, Month.JANUARY, 31), LocalTime.of(11, 0));
            filteredMealsWithExcess.forEach(System.out::println);

            System.out.println();
            System.out.println(mealController.getBetween(null, null, null, null));

            ConfigurableListableBeanFactory factory = appCtx.getBeanFactory();
            final String[] beenNames = appCtx.getBeanDefinitionNames();
            for (String beenName : beenNames) {
                final BeanDefinition beanDefinition = factory.getBeanDefinition(beenName);
                final String originalClassName = beanDefinition.getBeanClassName();
                final Class<?> originalClass = Class.forName(originalClassName);
                for (Method method : originalClass.getDeclaredMethods()) {
                    if (method.getName().equals("getAll")) {
                        final Object bean = appCtx.getBean(beenName);
                        System.out.println(beenName + " but class is: " + bean.getClass());
                    }
                }
            }
            final Object bean = appCtx.getBean("jpaUserRepository");
            final Method getAllMethod = JpaUserRepository.class.getMethod("getAll");
            final Method currentMethod = bean.getClass().getMethod(getAllMethod.getName(), getAllMethod.getParameterTypes());
            List<User> result;
            result = (List<User>) currentMethod.invoke(bean);
            System.out.println(result);

            UserRepository beanU = (UserRepository) bean;
            System.out.println(beanU.getAll());

            System.out.println("Conclusion: Retrieve bean by interface, not by class");
        }
    }
}
