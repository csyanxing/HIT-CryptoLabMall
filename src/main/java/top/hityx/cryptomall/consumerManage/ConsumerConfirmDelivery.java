package top.hityx.cryptomall.consumerManage;

import top.hityx.cryptomall.utils.JdbcUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/consumerConfirmDelivery")
public class ConsumerConfirmDelivery extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (!(userType != null && ("consumer".equals(userType)) && consumerUsername != null && consumerUsername.length() != 0)) {
            resp.sendRedirect("consumerManage/unLogin.jsp");
            return;
        }
        int orderID = Integer.valueOf(req.getParameter("orderID"));
        confirmOrder(orderID);
        resp.sendRedirect(req.getContextPath() + "/consumerManage/success_confirm.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private void confirmOrder(int orderID) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("UPDATE mall_orders SET has_confirm_delivery = 1 WHERE order_id = ?");
            prep.setInt(1, orderID);
            prep.executeUpdate();
            JdbcUtils.close(prep, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
