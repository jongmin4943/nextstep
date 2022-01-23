package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private static final List<String> requestMethods = Arrays.asList("GET","POST","PUT","DELETE");
    private String requestMethod;
    private String requestUrl;
    private String url;
    private String params;
    private int contentLength = 0;
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> cookie = new HashMap<>();
    private String requestBody;

    public Request(InputStream in) {
        // InputStream 을 Character input stream 으로 변환하기위해 BufferedReader 를 사용한다.
        // InputStreamReader 를 이용해 InputStream 을 읽어서 Reader 객체를 만든다.
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            String firstLine = br.readLine();
            if(firstLine == null) return;
            String[] requestMethodAndUrl = extractRequestMethodAndUrl(firstLine);
            this.requestMethod = requestMethodAndUrl[0];
            this.requestUrl = requestMethodAndUrl[1];
            this.url = extractUrl(this.requestUrl);
            // params 가 있으면 ? 뒤의 String 이 추출된다
            this.params = extractParams(this.requestUrl);
            String nextLine;
            // 헤더의 마지막은 공백이므로 공백이 아닐때까지 읽어들인다.
            // 헤더를 map 형태로 추출
            while(!"".equals(nextLine = br.readLine())) {
                if(nextLine == null) break;
                String[] temp = nextLine.split(":");
                header.put(temp[0].trim(),temp[1].trim());
            }
            this.cookie = HttpRequestUtils.parseCookies(header.get("Cookie"));
            this.contentLength = Integer.parseInt(header.get("Content-Length"));
            this.requestBody = util.IOUtils.readData(br,contentLength);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public String getParameter(String key) {
        return HttpRequestUtils.parseQueryString(params).get(key);
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public Map<String,String> getHeader() {
        return header;
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public void setHeader(Map<String,String> header) {
        this.header = header;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getRequestBody(String key) {
        return HttpRequestUtils.parseQueryString(requestBody).get(key);
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getCookie() {
        return cookie;
    }

    public void setCookie(Map<String, String> cookie) {
        this.cookie = cookie;
    }

    public boolean checkPathVariables(String ... paths) {
        // url 은 시작이 / 이므로 없애준다.
        String tempUrl = this.url.substring(1);

        // ex) user/create -> [user, create]
        String[] pathVariables = tempUrl.split("/");
        boolean result = true;
        // 넘어온 체크할 paths 를 비교한다.
        for (int i = 0; i < paths.length; i++) {
            if(!paths[i].equals(pathVariables[i])) {
                return false;
            }
        }
        return result;
    }

    public static String[] extractRequestMethodAndUrl(String line) {
        // 줄마다 띄어쓰기로 정보가 나눠져 있으므로 split 을 한다.
        String[] token = line.split(" ");
        // 첫번째 값이 requestMethods 값중 하나일 경우 두번째 값은 요청 url 이 된다.
        if(requestMethods.contains(token[0])) {
            return token;
        }
        return new String[]{"",""};
    }

    public static String extractUrl(String requestUrl) {
        int index = requestUrl.indexOf("?");
        if("/".equals(requestUrl)) requestUrl = "/index.html";
        return index == -1 ? requestUrl : requestUrl.substring(0,index);
    }

    public static String extractParams(String requestUrl) {
        int index = requestUrl.indexOf("?");
        if(index == -1) return "";
        return requestUrl.substring(index+1);
    }

    public static String extractExtension(String requestUrl) {
        int idx = requestUrl.lastIndexOf(".");
        if(idx == -1) return "";
        return requestUrl.substring(idx+1);
    }
}
