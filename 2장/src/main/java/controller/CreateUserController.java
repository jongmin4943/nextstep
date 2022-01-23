package controller;


import db.DataBase;
import http.Request;
import http.Response;
import model.User;

public class CreateUserController extends AbstractController{
    @Override
    public void doPost(Request request, Response response) {
        User user = new User(request.getRequestBody());
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
