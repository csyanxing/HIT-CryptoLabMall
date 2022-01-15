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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/consumerBankManage")
public class ConsumerBankManage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (userType != null && ("consumer".equals(userType)) && consumerUsername != null && consumerUsername.length() != 0) {
            req.setAttribute("consumerUsername", consumerUsername);
            String bankNum = getBankCardNum(consumerUsername);
            double balance = getBalance(bankNum);
            req.setAttribute("consumerBankCardNum", bankNum);
            req.setAttribute("consumerBankBalance", balance);
            req.getRequestDispatcher("consumerManage/consumer_bank_manage.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("consumerManage/unLogin.jsp");
        }
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private String getBankCardNum(String consumerUsername) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_consumers where consumer_username=?");
            prep.setString(1, consumerUsername);
            rs = prep.executeQuery();
            if (rs.next()) {
                String ret = rs.getString("consumer_bank_card_num");
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
