package http;

import util.HttpRequestUtils;

import java.util.Map;

public class Cookie {
    private Map<String,String> cookies;

    Cookie(String cookieValue) {
        cookies = HttpRequestUtils.parseCookies(cookieValue);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }

}
