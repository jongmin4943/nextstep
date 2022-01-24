package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
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
import java.util.HashMap;
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
            // 요청 객체 생성
            Request request = new Request(in);
            // 응답 객체 생성
            Response response = new Response(out);
            // 요청 객체에서 url을 따온다
            String path = request.getPath();
            // 요청 url 로 등록된 controller 에서 controller를 가져온다.
            Controller controller = RequestMapping.getController(path);
            // 없으면 그대로 파일만 내보내준다.
            if (controller == null) {
                if(path.equals("/")) path = "/index.html";
                response.forward(path);
            } else {
                // 있으면 service 를 호출해 로직 진행 후 내보낸다.
                controller.service(request, response);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
