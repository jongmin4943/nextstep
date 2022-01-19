package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringUtil;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Response {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);


    public static void response (OutputStream out, String url, int status, String statusName, String cookie, String type) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        writeResponse(status, statusName, cookie, type, dos, body);
    }


    public static void responseWithTemplate (OutputStream out, String url, int status, String statusName, String cookie, String type, String template) throws IOException {
        String fileString = new String(Files.readAllBytes(new File("./webapp" + url).toPath()));
        fileString = fileString.replace("{{user_list}}", URLDecoder.decode(template, "UTF-8"));
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = fileString.getBytes(StandardCharsets.UTF_8);
        writeResponse(status, statusName, cookie, type, dos, body);
    }

    public static void writeResponse(int status, String statusName, String cookie, String type, DataOutputStream dos, byte[] body) {
        Response.writeResponseHeader(status, statusName, dos, body.length, cookie, type);
        Response.writeResponseBody(dos, body);
    }

    public static void writeResponseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

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
}
