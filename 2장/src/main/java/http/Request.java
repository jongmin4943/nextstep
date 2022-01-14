package http;

import java.util.Arrays;
import java.util.List;

public class Request {

    private static final List<String> requestMethods = Arrays.asList("GET","POST","PUT","DELETE");

    public static boolean checkPathVariables(String url, String ... paths) {
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
        if(index < 0) return "";
        return requestUrl.substring(index+1);
    }
}
