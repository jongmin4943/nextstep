package util;

import http.Request;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception{
        InputStream in = new FileInputStream(testDirectory + "Http_GET.txt");
        Request request = new Request(in);

        assertEquals("GET",request.getMethod());
        assertEquals("/user/create",request.getPath());
        assertEquals("keep-alive",request.getHeader("Connection"));
        assertEquals("dbswww",request.getParameter("userId"));
    }

    @Test
    public void request_POST() throws Exception{
        InputStream in = new FileInputStream(testDirectory + "Http_POST.txt");
        Request request = new Request(in);

        assertEquals("POST",request.getMethod());
        assertEquals("/user/create",request.getPath());
        assertEquals("keep-alive",request.getHeader("Connection"));
        assertEquals("dbswww",request.getParameter("userId"));
    }

}
