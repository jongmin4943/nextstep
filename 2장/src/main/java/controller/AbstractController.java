package controller;

import http.Method;
import http.Request;
import http.Response;

public abstract class AbstractController implements Controller{
    @Override
    public void service(Request request, Response response) {
        Method requestMethod = request.getMethod();
        if(requestMethod.isPost()) doPost(request,response);
        else doGet(request,response);
    }

    public void doGet(Request request, Response response){

    }

    public void doPost(Request request, Response response){

    }
}
