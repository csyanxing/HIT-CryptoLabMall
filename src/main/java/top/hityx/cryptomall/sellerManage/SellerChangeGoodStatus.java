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

@WebServlet("/SellerChangeGoodStatus")
public class SellerChangeGoodStatus extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String goodID = req.getParameter("goodID");
        if (goodID == null) {
            resp.sendRedirect(req.getContextPath() + "/sellerGoodList");
            return;
        }
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("UPDATE mall_goods SET good_is_on_sell = !good_is_on_sell WHERE (good_id = ?)");
            prep.setInt(1, Integer.valueOf(goodID));
            prep.execute();
            resp.sendRedirect(req.getContextPath() + "/sellerGoodList");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
