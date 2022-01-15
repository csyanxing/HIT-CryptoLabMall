package top.hityx.cryptomall.consumerManage;

import top.hityx.cryptomall.models.Good;
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
import java.util.Date;

@WebServlet("/consumerGenerateOrder")
public class ConsumerGenerateOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (!(userType != null && ("consumer".equals(userType)) && consumerUsername != null && consumerUsername.length() != 0)) {
            resp.sendRedirect("consumerManage/unLogin.jsp");
            return;
        }
        int goodNum = 1;
        String goodID = req.getParameter("goodID");
        Good good = getGood(Integer.valueOf(goodID));
        Order order = new Order(0,
                consumerUsername,
                good.getGoodSellerUsername(),
                Integer.valueOf(goodID),
                good.getGoodName(),
                good.getGoodPrice(),
                goodNum,
                goodNum * good.getGoodPrice(),
                new Date(),
                false,
                false,
                false,
                "");
        addOrder(order);
        deleteCart(consumerUsername, goodID);
        resp.sendRedirect(req.getContextPath()+"/consumerManage/success_generate_order.jsp");
    }

    private void deleteCart(String consumerUsername, String goodID) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("DELETE FROM mall_cart WHERE good_id = ? and consumer_username = ?");
            prep.setInt(1, Integer.valueOf(goodID));
            prep.setString(2, consumerUsername);
            prep.executeUpdate();
            JdbcUtils.close(prep, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addOrder(Order order) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("INSERT INTO mall_orders values(null,?,?,?,?,?,?,?,?,?,?,?,?)");
            prep.setString(1, order.getConsumerUsername());
            prep.setString(2, order.getSellerUsername());
            prep.setInt(3, order.getGoodID());
            prep.setString(4, order.getGoodName());
            prep.setDouble(5, order.getGoodPrice());
            prep.setInt(6, order.getGoodNum());
            prep.setDouble(7, order.getOrderMoney());
            prep.setTimestamp(8, new java.sql.Timestamp(order.getOrderTime().getTime()));
            prep.setBoolean(9, false);
            prep.setBoolean(10, false);
            prep.setBoolean(11, false);
            prep.setString(12, "");
            prep.executeUpdate();
            JdbcUtils.close(prep, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Good getGood(int goodID) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        Good ret = null;
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_goods WHERE good_id=?");
            prep.setInt(1, goodID);
            rs = prep.executeQuery();
            if (rs.next()) {
                ret = new Good(
                        rs.getInt("good_id"),
                        rs.getString("good_name"),
                        rs.getString("good_description"),
                        rs.getString("good_image_name"),
                        rs.getDouble("good_price"),
                        rs.getBoolean("good_is_on_sell"),
                        rs.getString("good_seller_username")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
