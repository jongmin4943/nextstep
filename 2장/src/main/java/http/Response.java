package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringUtil;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Response {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void writeResponseHeader(int status, String statusName, DataOutputStream dos, int lengthOfBodyContent, String cookie,String type) {
        String firstLine = "HTTP/1.1 " + status + " " +  statusName;
        List<String> nextLines = new ArrayList<>();
        switch (status) {
            case 200: {
                    nextLines =  response200Header(lengthOfBodyContent,type);
                break;
            }
            case 302:
                nextLines = response302Header();
                break;
        }
        try {
            dos.writeBytes(firstLine + " \r\n");
            for (String nextLine : nextLines){
                dos.writeBytes(nextLine+" \r\n");
            }
            if(StringUtil.hasText(cookie)) {
                dos.writeBytes("Set-Cookie: " + cookie + " \r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static List<String> response200Header(int lengthOfBodyContent,String type) {
        List<String> header = new ArrayList<>();
        header.add("Content-Type: text/"+type+";charset=utf-8");
        header.add("Content-Length: " + lengthOfBodyContent);
        return header;
    }

    private static List<String> response302Header() {
        List<String> header = new ArrayList<>();
        header.add("Location: /index.html");
        return header;
    }

    public static void writeResponseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
