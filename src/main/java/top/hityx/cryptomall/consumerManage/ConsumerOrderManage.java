package top.hityx.cryptomall.consumerManage;

import top.hityx.cryptomall.models.Order;
import top.hityx.cryptomall.utils.JdbcUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/consumerOrderManage")
public class ConsumerOrderManage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (userType != null && ("consumer".equals(userType)) && consumerUsername != null && consumerUsername.length() != 0) {
            req.setAttribute("consumerUsername", consumerUsername);
            List<Order> orderList = getOrders(consumerUsername);
            req.setAttribute("orderList", orderList);
            req.getRequestDispatcher("consumerManage/consumer_order_manage.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("consumerManage/unLogin.jsp");
        }
//        req.getRequestDispatcher("consumerManage/consumer_order_manage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private List<Order> getOrders(String consumerUsername) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        List<Order> orderList = new ArrayList<>();

        try {
            prep = conn.prepareStatement("SELECT * FROM mall_orders WHERE consumer_username = ?");
            prep.setString(1, consumerUsername);
            rs = prep.executeQuery();
            while (rs.next()) {
                orderList.add(new Order(
                        rs.getInt("order_id"),
                        rs.getString("consumer_username"),
                        rs.getString("seller_username"),
                        rs.getInt("good_id"),
                        rs.getString("good_name"),
                        rs.getDouble("good_price"),
                        rs.getInt("good_num"),
                        rs.getDouble("order_money"),
                        new Date(rs.getTimestamp("order_time").getTime()),
                        rs.getBoolean("has_payed"),
                        rs.getBoolean("has_on_load"),
                        rs.getBoolean("has_confirm_delivery"),
                        rs.getString("order_info")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }
}
