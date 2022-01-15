package top.hityx.cryptomall.sellerManage;

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

@WebServlet("/sellerStartDelivery")
public class SellerStartDelivery extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String sellerUsername = (String) req.getSession().getAttribute("sellerUsername");
        if (!(userType != null && ("seller".equals(userType)) && sellerUsername != null && sellerUsername.length() != 0)) {
            resp.sendRedirect("sellerManage/unLogin.jsp");
            return;
        }
        int orderID = Integer.valueOf(req.getParameter("orderID"));
        deliverOrder(orderID);
        resp.sendRedirect(req.getContextPath() + "/sellerManage/success_delivery.jsp");
    }

    private void deliverOrder(int orderID) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("UPDATE mall_orders SET has_on_load = 1 WHERE order_id = ?");
            prep.setInt(1, orderID);
            prep.executeUpdate();
            JdbcUtils.close(prep, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
