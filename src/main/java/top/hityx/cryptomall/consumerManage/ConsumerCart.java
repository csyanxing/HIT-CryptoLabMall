package top.hityx.cryptomall.consumerManage;

import top.hityx.cryptomall.models.Good;
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
import java.util.List;

@WebServlet("/consumerCart")
public class ConsumerCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (!(userType != null && ("consumer".equals(userType)) && consumerUsername != null && consumerUsername.length() != 0)) {
            resp.sendRedirect("consumerManage/unLogin.jsp");
            return;
        }
        req.setAttribute("consumerUsername", consumerUsername);
        List<Good> goodList = getGoodList(consumerUsername);
//        System.out.println(goodList);
        req.setAttribute("goodList", goodList);
        req.getRequestDispatcher("consumerManage/consumer_cart_list.jsp").forward(req, resp);
    }

    private List<Good> getGoodList(String consumerUsername) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        List<Integer> listGoodID = new ArrayList<>();
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_cart WHERE consumer_username=?");
            prep.setString(1, consumerUsername);
            rs = prep.executeQuery();
            while (rs.next()) {
                listGoodID.add(rs.getInt("good_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcUtils.close(rs, prep, conn);
        List<Good> goodList = getGoods(listGoodID);
        return goodList;
    }

    private List<Good> getGoods(List<Integer> listGoodID) {
        List<Good> ret = new ArrayList<>();
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        String sql = null;
        for (int goodID : listGoodID) {
            sql = "SELECT * FROM mall_goods where good_id=?";
            try {
                prep = conn.prepareStatement(sql);
                prep.setInt(1, goodID);
                rs = prep.executeQuery();
                if (rs.next()) {
                    ret.add(new Good(
                                    rs.getInt("good_id"),
                                    rs.getString("good_name"),
                                    rs.getString("good_description"),
                                    rs.getString("good_image_name"),
                                    rs.getDouble("good_price"),
                                    rs.getBoolean("good_is_on_sell"),
                                    rs.getString("good_seller_username")
                            )
                    );
                }
                if (prep != null) {
                    prep.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        JdbcUtils.close(rs, prep, conn);
        return ret;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
