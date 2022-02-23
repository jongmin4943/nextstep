package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JspView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private String viewName;
    private Map<String, Object> attribute = new HashMap<>();

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getAttribute() {
        return attribute;
    }

    public void setAttribute(String key, Object value) {
        attribute.put(key, value);
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Set<String> keys = attribute.keySet();
        for (String key: keys) {
            request.setAttribute(key, attribute.get(key));
        }
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }
}
