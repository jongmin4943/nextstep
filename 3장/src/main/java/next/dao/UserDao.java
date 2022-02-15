package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

/**
 * 동영상을 통한 재 학습 -> https://www.youtube.com/watch?v=ylrMBeakVnk
 * 1. 개발자가 구현해야하는부분과 라이브러리로 구현하는 부분을 나눈다.
 * 2. 리팩토링 도중 항상 테스트를 한다. ( 변경 시 오류가 없나 계속 확인 )
 * 3. 메소드를 결정할수없는 부분을 추상클래스 -> 구현을 뒤로 미룬다. -> 익명클래스 ------ 템플릿메소드패턴
 * 4. 특정 객체와 연결되어 있으면 끊어준다.
 * 5. 달라지는 부분중 간단한부분은 파라미터, 복잡하다면 익명클래스를 사용해본다.
 * 6. 템플릿 메소드의 단점 -> 변경이 생기면 추상클래스를 구현하고있던곳을 전부 바꿔야한다.
 * 7. 단점 극복 -> 추상클래스들을 인터페이스화 시키고 매개변수로 주고 받을 수 있게 바꾼다.
 * 8. 중복을 항상 어떻게 제거 할 수 있을까 고민해야한다.
 */
public class UserDao {
    public void insert(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.executeUpdate("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.executeUpdate("UPDATE USERS SET password=?, name=?, email=? where userId = ?" , user.getPassword(), user.getName(), user.getEmail(),user.getUserId());
    }

    public List<User> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.executeQueryForList("SELECT userId, password, name, email FROM USERS",
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                            rs.getString("email")));
    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.executeQuery("SELECT userId, password, name, email FROM USERS WHERE userId=?",
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")),
                userId);
    }

}