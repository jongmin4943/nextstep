package controller;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;

public class LoginController extends AbstractController {
    @Override
    public void doPost(Request request, Response response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = DataBase.findUserById(userId);
        boolean validateUser = user != null && user.validateUser(userId,password);
        String redirectUrl = validateUser ? "/index.html": "/user/login_failed.html";
        String cookie = validateUser ? "logined=true":"logined=false";
        response.addHeader("Set-Cookie",cookie);
        response.sendRedirect(redirectUrl);
    }
}
