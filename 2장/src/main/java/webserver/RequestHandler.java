package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "Hello World".getBytes();
            // InputStream 을 Character input stream 으로 변환하기위해 BufferedReader 를 사용한다.
            // InputStreamReader 를 이용해 InputStream 을 읽어서 Reader 객체를 만든다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            // 헤더의 마지막은 공백이므로 공백이 아닐때까지 읽어들인다.
            while(!"".equals(line = br.readLine())) {
                if(line == null) return; // null 이 들어올경우 반복문을 종료한다.
                System.out.println(line);
                String requestUrl = extractRequestUrl(line);
                // requestUrl 이 있다면 webapp 폴더에 있는 요청된 파일을 읽어서 byte 로 변환해 body 에 넣어주고 반복문을 종료한다.
                if(hasText(requestUrl)) {
                    String url = extractUrl(requestUrl);
                    String params = extractParams(requestUrl); // params 가 있으면 ? 뒤의 String 이 추출된다.

                    // url 이 뭔지 판단하는 함수.
                    if(checkPathVariables(url,"user","create")) {
                        // user/create 일경우 유저를 넘어온 params 값으로 생성한다.
                        User user = createUserByParams(params);
                        System.out.println(user);
                    }
                    body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    break;
                }
            }
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean checkPathVariables(String url, String ... paths) {
        // url 은 시작이 / 이므로 없애준다.
        url = url.substring(1);

        // ex) user/create -> [user, create]
        String[] pathVariables = url.split("/");
        boolean result = true;

        // 넘어온 체크할 paths 를 비교한다.
        for (int i = 0; i < paths.length; i++) {
            if(!paths[i].equals(pathVariables[i])) {
                return false;
            }
        }
        return result;
    }

    private User createUserByParams(String params) {
        // 넘어온 params 를 추출해 유저 생성
        Map<String, String> paramsMap = HttpRequestUtils.parseQueryString(params);
        String userId = paramsMap.get("userId");
        String password = paramsMap.get("password");
        String name = paramsMap.get("name");
        String email = paramsMap.get("email");
        return new User(userId,password,name,email);
    }

    private String extractRequestUrl(String line) {
        // 줄마다 띄어쓰기로 정보가 나눠져 있으므로 split 을 한다.
        String[] token = line.split(" ");
        String requestUrl = "";
        // 첫번째 값이 GET 이라는 글자일 경우 두번째 값은 요청 url 이 된다.
        if("GET".equals(token[0])) {
            requestUrl = token[1];
        }
        return requestUrl;
    }

    private String extractUrl(String requestUrl) {
        int index = requestUrl.indexOf("?");
        return requestUrl.substring(0,index);
    }

    private String extractParams(String requestUrl) {
        int index = requestUrl.indexOf("?");
        if(index < 0) return "";
        return requestUrl.substring(index+1);
    }

    private boolean hasText(String str) {
        return !(str == null || str.isEmpty());
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
