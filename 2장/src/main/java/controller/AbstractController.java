package controller;

import http.Request;
import http.Response;

public abstract class AbstractController implements Controller{
    @Override
    public void service(Request request, Response response) {
        String requestMethod = request.getRequestMethod();
        if("GET".equals(requestMethod)) doGet(request,response);
        else if("POST".equals(requestMethod)) doPost(request,response);
    }

    public void doGet(Request request, Response response){

    }

    public void doPost(Request request, Response response){

    }
}
