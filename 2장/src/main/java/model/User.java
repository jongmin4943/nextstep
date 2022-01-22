package model;

import util.HttpRequestUtils;
import util.StringUtil;

import java.util.Map;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(String params) {
        // 넘어온 params 를 추출해 유저 생성
        Map<String, String> paramsMap = HttpRequestUtils.parseQueryString(params);
        this.userId = paramsMap.get("userId");
        this.password = paramsMap.get("password");
        this.name = paramsMap.get("name");
        this.email =paramsMap.get("email");
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public boolean validateUser(String userId, String password) {
        return (StringUtil.hasText(userId) && StringUtil.hasText(password) && validatePassword(userId,password));
    }

    public boolean validatePassword(String userId, String password) {
        return this.userId.equals(userId) && this.password.equals(password);
    }
}
