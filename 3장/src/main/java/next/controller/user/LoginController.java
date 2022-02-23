package next.controller.user;

import core.db.DataBase;
import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.View;
import next.controller.UserSessionUtils;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController implements Controller {
    @Override
    public View execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            JspView jspView = new JspView("/user/login.jsp");
            jspView.setAttribute("loginFailed", true);
            return jspView;
        }
        if (user.matchPassword(password)) {
            JspView jspView = new JspView("redirect:/");
            jspView.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return jspView;
        } else {
            JspView jspView = new JspView("/user/login.jsp");
            jspView.setAttribute("loginFailed", true);
            return jspView;
        }
    }
}