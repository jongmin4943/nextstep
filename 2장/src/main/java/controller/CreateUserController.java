package controller;


import db.DataBase;
import http.Request;
import http.Response;
import model.User;

public class CreateUserController extends AbstractController{
    @Override
    public void doPost(Request request, Response response) {
        User user = new User(
                request.getParameter("userId")
                ,request.getParameter("password")
                ,request.getParameter("name")
                ,request.getParameter("email")
        );
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
