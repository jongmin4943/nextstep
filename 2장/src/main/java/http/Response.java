package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void writeResponseHeader(int status, String statusName, DataOutputStream dos, int lengthOfBodyContent) {
        String firstLine = "HTTP/1.1 " + status + " " +  statusName;
        try {
            dos.writeBytes(firstLine + " \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
