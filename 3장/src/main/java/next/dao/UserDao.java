package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

public class UserDao {
    public void insert(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            return pstmt;
        });
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update("UPDATE USERS SET password=?, name=?, email=? where userId = ?", pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
            return pstmt;
        });
    }

    public List<User> findAll() {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        return selectJdbcTemplate.query("SELECT userId, password, name, email FROM USERS",
                pstmt -> null, rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }

    public User findByUserId(String userId) {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        return selectJdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?", pstmt -> {
            pstmt.setString(1, userId);
            return pstmt;
        }, rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }

}