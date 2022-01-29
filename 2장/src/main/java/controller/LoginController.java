package controller;

import db.DataBase;
import http.Request;
import http.Response;
import http.Session;
import model.User;

public class LoginController extends AbstractController {
    @Override
    public void doPost(Request request, Response response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = DataBase.findUserById(userId);
        boolean validateUser = user != null && user.validateUser(userId,password);
        if(validateUser) {
            Session session = request.getSession();
            session.setAttribute("user",user);
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }

    }
}
