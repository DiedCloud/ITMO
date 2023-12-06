package servlet;

import model.AreaData;
import model.UserAreaData;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet(name = "areaCheckServlet", value = "/area-check")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final long startExec = System.nanoTime();

        final String ctx = this.getServletContext().getContextPath();

        final String x = request.getParameter("X");
        final String y = request.getParameter("Y");
        final String r = request.getParameter("R");

        final double dx;
        final double dy;
        final double dr;

        try {
            dx = Double.parseDouble(x);
            dy = Double.parseDouble(y);
            dr = Double.parseDouble(r);
        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(400);
            return;
        }
        if (!checkLimits(dr, dx, dy)){
            response.sendError(400);
            return;
        }

        final boolean result = checkArea(dr, dx, dy);

        final HttpSession currentSession = request.getSession();
        UserAreaData points = (UserAreaData) currentSession.getAttribute("points");
        if (points == null) {
            points = new UserAreaData();
            currentSession.setAttribute("points", points);
        }

        points.getAreaDataList().add(new AreaData(
                dr,
                dx,
                dy,
                result,
                LocalDateTime.now(),
                System.nanoTime() - startExec
        ));

        response.setContentType("text/html;charset=UTF-8");
        final PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("    <meta http-equiv=\"Content-Type\" content=\"text/html\" charset=\"UTF-8\" />");
        out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("    <link rel=\"stylesheet\" href=\"" + ctx + "/css/style.css\">");
        out.println("    <title>Calculation result</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class=\"calc-page\">");
        out.println("        <h1>Calculation results</h1>");
        out.println("    </div>");
        out.println("    <div class=\"calc-page\">");
        out.println("        <table class=\"stat-table\">");
        out.println("            <tr>");
        out.println("                <th class=\"stat-table table-cell\">Parameter</th>");
        out.println("                <th class=\"stat-table table-cell\">Received Value</th>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td class=\"stat-table table-cell\">R</td>");
        out.println("                <td class=\"stat-table table-cell\">" + dr + "</td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td class=\"stat-table table-cell\">X</td>");
        out.println("                <td class=\"stat-table table-cell\">" + dx + "</td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td class=\"stat-table table-cell\">Y</td>");
        out.println("                <td class=\"stat-table table-cell\">" + dy + "</td>");
        out.println("            </tr>");
        out.println("        </table>");
        out.println("    </div>");
        out.println("    <div class=\"calc-page\">");
        out.println("        <p>Result: " + (result ? "Right in target!" : "Better luck next time") + "</p>");
        out.println("    </div>");
        out.println("    <div class=\"calc-page\">");
        out.println("        <p><a href=\"" + ctx + "\">Return to homepage</a></p>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    private boolean checkLimits(final double dr, final double dx, final double dy) {
        return 1 <= dr && dr <= 5 &&
                -6.5 <= dx && dx <= 6.5 &&
                -6.5 <= dy && dy <= 6.5;
    }

    private boolean checkArea(final double r, final double x, final double y) {
        if (-r/2 < y && y < 0){
            if (-r < x && x < 0){
                return true;
            }
        } else {
            if (-r < x && x < 0){
                return x*x + y*y < r*r;
            } else if (0 < x && x < r){
                return y < r;
            }
        }
        return false;
    }
}