package core.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private LegacyHandlerMapping lhm;
    private AnnotationHandlerMapping ahm;

    @Override
    public void init() throws ServletException {
        lhm = new LegacyHandlerMapping();
        lhm.initMapping();
        ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Controller controller = lhm.findController(requestUri);
            if (controller != null) {
                render(req, resp, controller.execute(req, resp));
            } else {
                HandlerExecution he = ahm.getHandler(req);
                if(he == null) {
                    throw new ServletException("유효하지 않은 요청입니다.");
                }
                render(req,resp,he.handle(req,resp));
            }
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView mav) throws Exception{
        View view = mav.getView();
        view.render(mav.getModel(),req,resp);
    }
}