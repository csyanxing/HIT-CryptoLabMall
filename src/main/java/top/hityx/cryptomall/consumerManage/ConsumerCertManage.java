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

@WebServlet("/consumerCertManage")
public class ConsumerCertManage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (userType != null && ("consumer".equals(userType)) && consumerUsername != null && consumerUsername.length() != 0) {
            req.setAttribute("consumerUsername", consumerUsername);
            String certInfo = getCertInfo(consumerUsername);
//            double balance = getBalance(bankNum);

            req.setAttribute("certInfo", certInfo);
//            req.setAttribute("consumerBankBalance", balance);
            req.getRequestDispatcher("consumerManage/consumer_cert_manage.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("consumerManage/unLogin.jsp");
        }
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        String certInfo = req.getParameter("cert");
        System.out.println(certInfo);
        String userType = (String) req.getSession().getAttribute("userType");
        String consumerUsername = (String) req.getSession().getAttribute("consumerUsername");
        if (userType != null && ("consumer".equals(userType)) && consumerUsername != null && consumerUsername.length() != 0) {
            req.setAttribute("consumerUsername", consumerUsername);
//            double balance = getBalance(bankNum);
            updateCertInfo(certInfo, consumerUsername);
            resp.getWriter().println("<script>window.alert('Success!');window.location.href='"+req.getContextPath()+"/consumerCertManage'"+"</script>");
//            req.setAttribute("consumerBankBalance", balance);
//            req.getRequestDispatcher("consumerManage/consumer_cert_manage.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("consumerManage/unLogin.jsp");
        }
        return;
    }
    private String getCertInfo(String consumerUsername) {
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("SELECT * FROM mall_consumers where consumer_username=?");
            prep.setString(1, consumerUsername);
            rs = prep.executeQuery();
            if (rs.next()) {
                String ret = rs.getString("consumer_cert");
                JdbcUtils.close(rs, prep, conn);
                return ret;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcUtils.close(rs, prep, conn);
        return null;
    }

    private void updateCertInfo(String cert, String consumerUsername){
        //UPDATE `CryptoLab`.`mall_consumers` SET `consumer_cert` = '33' WHERE (`consumer_id` = '1');
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("UPDATE mall_consumers SET consumer_cert = ? WHERE (consumer_username = ?)");
            prep.setString(1, cert);
            prep.setString(2, consumerUsername);
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    private double getBalance(String bankNum) {
//        Connection conn = JdbcUtils.getConnection();
//        PreparedStatement prep = null;
//        ResultSet rs = null;
//        try {
//            prep = conn.prepareStatement("SELECT * FROM mall_bank where bank_card_id=?");
//            prep.setString(1, bankNum);
//            rs = prep.executeQuery();
//            if (rs.next()) {
//                double ret = rs.getDouble("balance");
//                JdbcUtils.close(rs, prep, conn);
//                return ret;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        JdbcUtils.close(rs, prep, conn);
//        return 0;
//    }
}
