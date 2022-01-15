package top.hityx.cryptomall;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class home extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
//        String username = (String) req.getSession().getAttribute("username");
//        System.out.println(username);
//        if (username != null) {
//            req.setAttribute("msg", "欢迎您!" + username);
//        }
        String userType = (String) req.getSession().getAttribute("userType");
        if (userType != null) {
            if (userType.equals("seller")) {
                String username = (String) req.getSession().getAttribute("sellerUsername");
                req.setAttribute("msg", "欢迎卖家"+username);
            }else if(userType.equals("consumer")){
                String username = (String) req.getSession().getAttribute("consumerUsername");
                req.setAttribute("msg", "欢迎买家"+username);
            }
        }
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
