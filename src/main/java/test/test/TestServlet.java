package test.test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        Enumeration<String> attributeNames = req.getParameterNames();
//        attributeNames.nextElement()
        while(attributeNames.hasMoreElements()){
            System.out.println(attributeNames.nextElement());
        }
        req.getRequestDispatcher("test.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        req.setAttribute("user", "yanxing");
        req.getRequestDispatcher("/test.jsp").forward(req, resp);
    }
}
