package top.hityx.cryptomall.showgoods;

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

@WebServlet("/addToCart")
public class AddToCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String goodIDString = req.getParameter("goodID");
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (goodIDString == null || userType == null || consumerUsername == null) {
            resp.sendRedirect("showgoods/fail_add_cart.jsp");
            return;
        }
        // 检查是否有重复的goodIDString在购物车中
        if(hasAlreadyExist(consumerUsername, Integer.valueOf(goodIDString))){
            resp.sendRedirect("showgoods/fail_add_cart.jsp");
            return;
        }
        System.out.println("即将添加购物车......");
        System.out.println(goodIDString);
        System.out.println(consumerUsername);
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("INSERT INTO mall_cart VALUES (null,?,?)");
            prep.setInt(1, Integer.valueOf(goodIDString));
            prep.setString(2, consumerUsername);
            prep.executeUpdate();
            JdbcUtils.close(prep, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.sendRedirect("showgoods/success_add_cart.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private boolean hasAlreadyExist(String consumerUsername, int goodID){
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_cart where good_id=? and consumer_username=?");
            prep.setInt(1,goodID);
            prep.setString(2,consumerUsername);
            rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
