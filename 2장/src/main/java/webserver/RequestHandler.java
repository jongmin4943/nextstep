package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

import db.DataBase;
import http.Response;
import model.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.StringUtil;

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
            if (Request.extractExtension(request.getRequestUrl()).equals("css")) {
                // css 파일 응답
                Response.response(out,request.getUrl(),200,"OK","","css");
            } else if (request.checkPathVariables("user","create")) {
                User user = new User(request.getRequestBody());
                DataBase.addUser(user);
                request.setUrl("/index.html");
                Response.response(out,request.getUrl(),302,"Found","","html");
            } else if (request.checkPathVariables("user","login")) {
                String requestBody = request.getRequestBody();
                Map<String, String> queryString = HttpRequestUtils.parseQueryString(requestBody);
                String userId = queryString.get("userId");
                User user = DataBase.findUserById(userId);
                boolean validateUser = user != null && user.validateUser(requestBody);
                request.setUrl(validateUser ? "/index.html": "/user/login_failed.html");
                String cookie = validateUser ? "logined=true":"logined=false";
                int status  = validateUser ? 302 : 401;
                String statusName  = validateUser ? "Found" : "Unauthorized";
                Response.response(out,request.getUrl(),status,statusName,cookie,"html");
            } else if (request.checkPathVariables("user","list")) {
                String loginCookie = request.getCookie();
                if(StringUtil.hasText(request.getCookie()) && Boolean.parseBoolean(loginCookie)) {
                    int idx = 3;
                    Collection<User> userList = DataBase.findAll();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<tr>");
                    for(User user : userList) {
                        sb.append("<th scope=\"row\">")
                            .append(idx)
                            .append("</th><td>")
                            .append(user.getUserId())
                            .append("</td> <td>")
                            .append(user.getName())
                            .append("</td> <td>")
                            .append(user.getEmail())
                            .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
                        idx++;
                    }
                    request.setUrl("/user/list.html");
                    Response.responseWithTemplate(out,request.getUrl(),200,"OK","","html",sb.toString());
                } else {
                    request.setUrl("/user/login.html");
                    Response.response(out,request.getUrl(),401,"Unauthorized","","html");
                }
            } else {
                Response.response(out,request.getUrl(),200,"OK","","html");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
