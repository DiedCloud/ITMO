package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "controllerServlet", value = "/controller")
public class ControllerServlet extends HttpServlet {
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String forwardPath = getServletContext().getContextPath();

        if (request.getParameter("clear") != null) {
            final HttpSession session = request.getSession();
            session.invalidate();
        }

        if (request.getParameter("R") != null
                && request.getParameter("X") != null
                && request.getParameter("Y") != null)
        {
            forwardPath = this.getServletContext().getContextPath() + "/area-check?R=" + request.getParameter("R")
                    + "&X=" + request.getParameter("X") + "&Y=" + request.getParameter("Y");
        }

        response.sendRedirect(forwardPath);
    }
}