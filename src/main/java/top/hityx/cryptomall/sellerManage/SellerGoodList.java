package top.hityx.cryptomall.sellerManage;

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

@WebServlet("/sellerGoodList")
public class SellerGoodList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String sellerUsername = (String) req.getSession().getAttribute("sellerUsername");
        if (!(userType != null && ("seller".equals(userType)) && sellerUsername != null && sellerUsername.length() != 0)) {
            resp.sendRedirect("sellerManage/unLogin.jsp");
            return;
        }
        req.setAttribute("sellerUsername", sellerUsername);
        List<Good> goodList = getGoodList(sellerUsername);
        System.out.println(goodList);
        req.setAttribute("goodList", goodList);
        req.getRequestDispatcher("sellerManage/seller_good_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private List<Good> getGoodList(String sellerUsername) {
        List<Good> goodList = new ArrayList<>();
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM mall_goods WHERE good_seller_username=?");
            preparedStatement.setString(1, sellerUsername);
            rs = preparedStatement.executeQuery();
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
            JdbcUtils.close(rs, preparedStatement, conn);
        } catch (SQLException e) {
            JdbcUtils.close(rs, preparedStatement, conn);
            e.printStackTrace();
        }
        return goodList;
    }
}

