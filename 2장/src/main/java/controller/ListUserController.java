package controller;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;
import util.StringUtil;

import java.util.Collection;

public class ListUserController extends AbstractController{
    @Override
    public void doGet(Request request, Response response) {
        String loginCookie = request.getCookie("logined");
        if(StringUtil.hasText(loginCookie) && Boolean.parseBoolean(loginCookie)) {
            Collection<User> userList = DataBase.findAll();
            StringBuilder sb = new StringBuilder();
            sb.append("<tr>");
            for(User user : userList) {
                sb.append("<th scope=\"row\">")
                        .append("</th><td>")
                        .append(user.getUserId())
                        .append("</td> <td>")
                        .append(user.getName())
                        .append("</td> <td>")
                        .append(user.getEmail())
                        .append("</td></tr>");
            }
            response.forwardBody(sb.toString());
        } else {
            response.sendRedirect("/user/login.html");
        }
    }
}
