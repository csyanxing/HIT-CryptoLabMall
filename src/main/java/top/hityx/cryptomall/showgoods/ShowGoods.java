package top.hityx.cryptomall.showgoods;

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

@WebServlet("/showGoods")
public class ShowGoods extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        if (userType == null) {
            resp.sendRedirect("showgoods/unLogin.jsp");
            return;
        }
        req.setAttribute("userType", userType);
        String username = null;
        if (userType.equals("consumer")) {
            username = (String) req.getSession().getAttribute("consumerUsername");
        } else if (userType.equals("seller")) {
            username = (String) req.getSession().getAttribute("sellerUsername");
        }
        if (username != null) {
            req.setAttribute("username", username);
        }
        List<Good> goodList = getGoodList();
//        System.out.println(goodList);
        System.out.println("查询到" + goodList.size() + "件商品.");
        System.out.println("------------------查看了所有商品--------------------");
        req.setAttribute("goodList", goodList);
        req.getRequestDispatcher("showgoods/showgoods.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private List<Good> getGoodList() {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        List<Good> goodList = new ArrayList<>();
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_goods where good_is_on_sell=?");
            prep.setBoolean(1, true);
            rs = prep.executeQuery();
            while (rs.next()) {
                goodList.add(new Good(
                        rs.getInt("good_id"),
                        rs.getString("good_name"),
                        rs.getString("good_description"),
                        rs.getString("good_image_name"),
                        rs.getDouble("good_price"),
                        rs.getBoolean("good_is_on_sell"),
                        rs.getString("good_seller_username")));
            }
            JdbcUtils.close(rs, prep, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goodList;
    }
}
