package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    public static final int BATCH_SIZE = 500;
    //private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;
    //https://arnaudroger.github.io/blog/2017/06/13/jdbc-template-one-to-many.html
    private final ResultSetExtractor<List<User>> ROW_MAPPER = rs -> {
        final List<User> users = new ArrayList<>();
        User currentUser = null;
        while (rs.next()) {
            if (currentUser == null) {
                currentUser = mapUser(rs);
            } else if (currentUser.id() != rs.getLong("id")) {
                users.add(currentUser);
                currentUser = mapUser(rs);
            }
            if (rs.getString("role") != null) {
                Set<Role> roles = currentUser.getRoles();
                roles.add(Role.valueOf(rs.getString("role")));
                currentUser.setRoles(roles);
            }
        }
        if (currentUser != null) {
            users.add(currentUser);
        }
        return users;
    };

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ?", user.getId());
        insertRoles(user.getRoles(), user.getId());
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("""
                        SELECT * FROM users
                        LEFT JOIN user_roles ON users.id = user_roles.user_id
                        WHERE user_id=?""",
                ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("""
                        SELECT * FROM users
                        LEFT JOIN user_roles ON users.id =user_roles.user_id
                        WHERE email=?"""
                , ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                        SELECT * FROM users
                        LEFT JOIN user_roles ON users.id = user_roles.user_id
                        ORDER BY name, email"""
                , ROW_MAPPER);
    }

    //https://www.mkyong.com/spring/spring-jdbctemplate-batchupdate-example/
    private void insertRoles(Set<Role> roles, Integer id) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                roles,
                BATCH_SIZE,
                (ps, argument) -> {
                    ps.setInt(1, id);
                    ps.setString(2, argument.toString());
                });
    }

    private User mapUser(ResultSet rs) throws SQLException {
        final String role = rs.getString("role");
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("calories_per_day"),
                rs.getBoolean("enabled"),
                rs.getDate("registered"),
                role != null ? List.of(Role.valueOf(role))
                        : List.of()
        );
    }
}
