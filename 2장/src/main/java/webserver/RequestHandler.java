package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.net.HttpHeaders;
import db.DataBase;
import http.Request;
import http.Response;
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
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            byte[] body = "Hello World".getBytes();
            boolean isCreate = false;
            boolean isLogin = false;
            boolean isUserList = false;
            boolean validateUser = false;
            // InputStream 을 Character input stream 으로 변환하기위해 BufferedReader 를 사용한다.
            // InputStreamReader 를 이용해 InputStream 을 읽어서 Reader 객체를 만든다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            // 헤더의 마지막은 공백이므로 공백이 아닐때까지 읽어들인다.
                String line = br.readLine();
                if(line == null) return;
                System.out.println(line);
                String[] requestMethodAndUrl = Request.extractRequestMethodAndUrl(line);
                String requestMethod = requestMethodAndUrl[0];
                String requestUrl = requestMethodAndUrl[1];
                // requestUrl 이 있다면 webapp 폴더에 있는 요청된 파일을 읽어서 byte 로 변환해 body 에 넣어주고 반복문을 종료한다.
                if(StringUtil.hasText(requestUrl)) {
                    String url = Request.extractUrl(requestUrl);
                    String params = Request.extractParams(requestUrl); // params 가 있으면 ? 뒤의 String 이 추출된다.
                    int contentLength = 0;
                    String loginCookie = "";
                    // 헤더에서 Content-Length 추출
                    while(!"".equals(line = br.readLine())) {
                        if(line == null) break;
                        if(line.startsWith("Content-Length")) {
                            String[] temp = line.split(" ");
                            contentLength = Integer.parseInt(temp[1]);
                        }
                        if(line.startsWith("Cookie")) {
                            String[] token = line.split(" ");
                            Map<String, String> cookieMap = HttpRequestUtils.parseCookies(token[1]);
                            loginCookie = cookieMap.get("logined");
                        }
                    }
                    // url 이 뭔지 판단하는 함수.
                    isCreate = Request.checkPathVariables(url,"user","create");
                    isLogin = Request.checkPathVariables(url,"user","login");
                    isUserList = Request.checkPathVariables(url,"user","list");
                    if(isCreate) {
                        String requestBody = util.IOUtils.readData(br,contentLength);
                        // user/create 일경우 유저를 넘어온 params 값으로 생성한다.
                        User user = new User(requestBody);
                        DataBase.addUser(user);
                        System.out.println(user);
                        url="/index.html";
                    }
                    if(isLogin) {
                        String requestBody = util.IOUtils.readData(br,contentLength);
                        // user/create 일경우 유저를 넘어온 params 값으로 생성한다.
                        Map<String, String> queryString = HttpRequestUtils.parseQueryString(requestBody);
                        String userId = queryString.get("userId");
                        User user = DataBase.findUserById(userId);
                        validateUser =user.validateUser(requestBody);
                        if(validateUser) {
                            url="/index.html";
                        } else {
                            url="/user/login_failed.html";
                        }
                    }
                    if (isUserList) {
                        if(StringUtil.hasText(loginCookie) && Boolean.parseBoolean(loginCookie)) {
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
                            url += ".html";
                            String fileString = new String(Files.readAllBytes(new File("./webapp" + url).toPath()));
                            fileString = fileString.replace("{{user_list}}", URLDecoder.decode(sb.toString(), "UTF-8"));
                            body = fileString.getBytes(StandardCharsets.UTF_8);
                            DataOutputStream dos = new DataOutputStream(out);
                            Response.writeResponseHeader(200,"OK",dos,body.length,"");
                            Response.writeResponseBody(dos,body);
                            return;
                        } else {
                            url = "/login.html";
                        }
                    }
                    body = Files.readAllBytes(new File("./webapp" + url).toPath());
            }
            DataOutputStream dos = new DataOutputStream(out);
            if(isCreate) {
                Response.writeResponseHeader(302,"Found",dos,body.length,"");
            } else if(isLogin) {
                String cookie = validateUser ? "logined=true":"logined=false";
                Response.writeResponseHeader(302,"Found",dos,body.length,cookie);
            } else {
                Response.writeResponseHeader(200,"OK",dos,body.length,"");
                Response.writeResponseBody(dos,body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
