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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/sellerBankManage")
public class SellerBankManage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String sellerUsername = (String) req.getSession().getAttribute("sellerUsername");
        if (userType != null && ("seller".equals(userType)) && sellerUsername != null && sellerUsername.length() != 0) {
            req.setAttribute("sellerUsername", sellerUsername);
            String bankNum = getBankCardNum(sellerUsername);
            double balance = getBalance(bankNum);
            req.setAttribute("sellerBankCardNum", bankNum);
            req.setAttribute("sellerBankBalance", balance);
            req.getRequestDispatcher("sellerManage/seller_bank_manage.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("sellerManage/unLogin.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println(req.getAttribute("deltaBalance"));
//        double deltaBalance = Double.valueOf((String) req.getAttribute("deltaBalance"));
//        System.out.println(deltaBalance);
//        resp.sendRedirect("sellerManage/success_add_balance.jsp");
        // TODO 添加银行充钱代码
    }

    private String getBankCardNum(String sellerUsername) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_sellers where seller_username=?");
            prep.setString(1, sellerUsername);
            rs = prep.executeQuery();
            if (rs.next()) {
                String ret = rs.getString("seller_bank_card_num");
                JdbcUtils.close(rs, prep, conn);
                return ret;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcUtils.close(rs, prep, conn);
        return null;
    }

    private double getBalance(String bankNum) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_bank where bank_card_id=?");
            prep.setString(1, bankNum);
            rs = prep.executeQuery();
            if (rs.next()) {
                double ret = rs.getDouble("balance");
                JdbcUtils.close(rs, prep, conn);
                return ret;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcUtils.close(rs, prep, conn);
        return 0;
    }
}
