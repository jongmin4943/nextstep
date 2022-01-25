package http;

public enum Method {
    GET, POST;

    public boolean isPost() {
        return this == POST;
    }
}
