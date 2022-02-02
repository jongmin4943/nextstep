package controller;

import db.DataBase;
import http.Request;
import http.Response;
import http.Session;
import model.User;

import java.util.Collection;

public class ListUserController extends AbstractController{
    @Override
    public void doGet(Request request, Response response) {
        if(isLogined(request.getSession())) {
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

    private boolean isLogined(Session session) {
        Object user = session.getAttribute("user");
        return user != null;
    }
}
