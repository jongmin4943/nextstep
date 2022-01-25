package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> cookie = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private RequestLine requestLine;

    public Request(InputStream in) {
        // InputStream 을 Character input stream 으로 변환하기위해 BufferedReader 를 사용한다.
        // InputStreamReader 를 이용해 InputStream 을 읽어서 Reader 객체를 만든다.
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            requestLine = new RequestLine(createRequestLine(br));
            // 헤더를 map 형태로 추출
            extractHeaders(br);
            this.cookie = HttpRequestUtils.parseCookies(header.get("Cookie"));
            extractParams(br);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private void extractParams(BufferedReader br) throws IOException {
        if(Method.valueOf(getMethod()).isPost()) {
            String body = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));
            params = HttpRequestUtils.parseQueryString(body);
        } else {
            params = HttpRequestUtils.parseQueryString(requestLine.getQueryString());
        }
    }

    private void extractHeaders(BufferedReader br) throws IOException {
        String nextLine;
        // 헤더의 마지막은 공백이므로 공백이 아닐때까지 읽어들인다.
        while(!"".equals(nextLine = br.readLine())) {
            if(nextLine == null) break;
            String[] temp = nextLine.split(":");
            header.put(temp[0].trim(),temp[1].trim());
        }
    }

    private String createRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException();
        }
        return line;
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public String getCookie(String key) {
        return cookie.get(key);
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }
}
