package webserver;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        // try () <- 괄호 안에서 자원을 얻어쓰면 try 가 종료되면 자동으로 close 된다.
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // InputStream 을 Character input stream 으로 변환하기위해 BufferedReader 를 사용한다.
            // InputStreamReader 를 이용해 InputStream 을 읽어서 Reader 객체를 만든다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            // 요청 객체 생성
            Request request = new Request(br);
            Response response = new Response(out);
            if (Request.extractExtension(request.getRequestUrl()).equals("css")) {
                // css 파일 응답
                response.forward(request.getRequestUrl());
            } else if (request.checkPathVariables("user","create")) {
                User user = new User(request.getRequestBody());
                DataBase.addUser(user);
                response.sendRedirect("/index.html");
            } else if (request.checkPathVariables("user","login")) {
                String userId = request.getRequestBody("userId");
                String password = request.getRequestBody("password");
                User user = DataBase.findUserById(userId);
                boolean validateUser = user != null && user.validateUser(userId,password);
                String redirectUrl = validateUser ? "/index.html": "/user/login_failed.html";
                String cookie = validateUser ? "logined=true":"logined=false";
                response.addHeader("Set-Cookie",cookie);
                response.sendRedirect(redirectUrl);
            } else if (request.checkPathVariables("user","list")) {
                Map<String, String> cookie = request.getCookie();
                String loginCookie = cookie.get("logined");
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
            } else {
                response.forward(request.getUrl());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
