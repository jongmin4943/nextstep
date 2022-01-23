package controller;

import http.Request;
import http.Response;

public interface Controller {
    void service(Request request, Response response);
}
