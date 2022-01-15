package top.hityx.cryptomall.sellerManage;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import top.hityx.cryptomall.aesUtils.AESUtils;
import top.hityx.cryptomall.rsaUtils.RSAUtils;
import top.hityx.cryptomall.utils.JdbcUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/sellerAddGood")
@MultipartConfig
public class SellerAddGood extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = (String) req.getSession().getAttribute("userType");
        String sellerUsername = (String) req.getSession().getAttribute("sellerUsername");
        if (userType != null && ("seller".equals(userType)) && sellerUsername != null && sellerUsername.length() != 0) {
            req.setAttribute("sellerUsername", sellerUsername);
            req.getRequestDispatcher("sellerManage/seller_add_good.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("sellerManage/unLogin.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("提交了商品添加请求......");
        String sellerUsername = (String) req.getSession().getAttribute("sellerUsername");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        req.setCharacterEncoding("utf-8");
        System.out.println(req.getCharacterEncoding());
        //文件名中文乱码处理也可以如此写
        upload.setHeaderEncoding("utf-8");
        //设置缓冲区大小与临时文件目录
        factory.setSizeThreshold(1024 * 1024 * 10);
        File uploadTemp = new File("D:\\IdeaProjects\\cryptoMall\\src\\main\\webapp\\image\\goods");
        uploadTemp.mkdirs();
        factory.setRepository(uploadTemp);
        //设置单个文件大小限制
        upload.setFileSizeMax(1024 * 1024 * 10);
        //设置所有文件总和大小限制
        upload.setSizeMax(1024 * 1024 * 30 * 1024);
        Map<String, String> map = new HashMap<>();
        map = getParam(req, resp);
        try {
            List<FileItem> list = upload.parseRequest(req);
            for (FileItem fileItem : list) {
                System.out.println(fileItem);
                if (!fileItem.isFormField() && fileItem.getName() != null && !"".equals(fileItem.getName())) {
                    String filName = fileItem.getName();
                    //利用UUID生成伪随机字符串，作为文件名避免重复
                    String uuid = UUID.randomUUID().toString();
                    //获取文件后缀名
                    String suffix = filName.substring(filName.lastIndexOf("."));
                    //获取文件上传目录路径，在项目部署路径下的upload目录里。若想让浏览器不能直接访问到图片，可以放在WEB-INF下
                    String uploadPath = "D:\\IdeaProjects\\cryptoMall\\src\\main\\webapp\\image\\goods";
                    File file = new File(uploadPath);
                    file.mkdirs();
                    //写入文件到磁盘，该行执行完毕后，若有该临时文件，将会自动删除
                    fileItem.write(new File(uploadPath, uuid + suffix));
                    map.put("goodImageName", uuid + suffix);
                } else {
                    map.put(fileItem.getFieldName(), fileItem.getString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(map);
        String goodName = new String(map.get("goodName").getBytes("iso8859-1"), "UTF-8");
        String goodDescription = new String(map.get("goodDescription").getBytes("iso8859-1"), "UTF-8");
        double goodPrice = Double.valueOf(map.get("price"));
        boolean goodOnSell = Boolean.valueOf(map.get("goodOnSell"));
        String goodImageName = map.get("goodImageName");
        System.out.println("即将添加商品......");
        System.out.println("goodName: " + goodName);
        System.out.println("goodDescription: " + goodDescription);
        System.out.println("goodPrice: " + goodPrice);
        System.out.println("goodOnSell: " + goodOnSell);
        System.out.println("goodImageName: " + goodImageName);
        System.out.println("sellerUsername: " + sellerUsername);

        boolean b = addToDB(goodName, goodDescription, goodImageName, goodPrice, goodOnSell, sellerUsername);
        if (b) {
            resp.sendRedirect(req.getContextPath() + "/sellerManage/success_seller_add_good.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/sellerManage/fail_seller_add_good.jsp");
        }
//        resp.sendRedirect(req.getContextPath() + "/sellerAddGood");
    }

    private Map<String, String> getParam(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String privExpString = "2303722540491704065766921887141457384619000005979787508545997584207950755550516424081976521287821935155547204744173637074752948410357161621094984108336243356162204537009908501946626618771701046743389006792224757451220508543776805903196892186284813238731109560453407126388304170053799666037932142861228815579480232995282360079268908441898965198222910411172465718433992653786065511572926091009006565885986718312465012488713275463568189978444587866905474342164244161952179576214149641966257586781926597608762637626937741466548943072274088909289347054577410578455531521403161683217816915703196771486427463763037437674113";
            String modulusString = "28867889892199772343817736083669157287911166996538687179269415616871217718262752368080400626317396972139406722240708920261583934984622810929579727248189747769178278918167949042461963425323321510596842320868457730225743493008317500664974134457466119546218112287463660199256269673578559983389667083498346632243627965274407049256412061228174333521289841241580679432832064492586086773749252589130598103889094230223804587824090511984909898578984539057083340762186324904157710792905689686517370506668459158914440777340979040775220401940386218716835451246077107660074953512949678022810109429100853469753848536162139171336157";
            if(!(privExpString == null)){
                return new HashMap<>();
            }
            Map<String, String> params = new HashMap<>();
            RSAPrivateKey rsaPrivateKey = null;
            try {
                rsaPrivateKey = RSAUtils.generateRSAPrivateKey(new BigInteger(privExpString), new BigInteger(modulusString));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            if (rsaPrivateKey == null) {
                return params;
            }
            String username = null;
            String password = null;
            String checkCode = req.getParameter("checkcode");
            String encryptedUsername = req.getParameter("encryptedUsername");
            String encryptedPassword = req.getParameter("encryptedPassword");
            String encryptedBigIntegerSymKeyStr = req.getParameter("encryptedBigIntegerSymKey");
            BigInteger encryptedBigIntegerSymKey = new BigInteger(encryptedBigIntegerSymKeyStr, 16);
            String symKey = RSAUtils.decrypt(rsaPrivateKey, encryptedBigIntegerSymKey);
//        System.out.println("解密得到的对称密钥: " + symKey);
            String decryptedUsername = AESUtils.AESDecrypt(symKey, encryptedUsername);
            String decryptedPassword = AESUtils.AESDecrypt(symKey, encryptedPassword);
//        System.out.println("解密得到的用户名: " + decryptedUsername);
//        System.out.println("解密得到的密码: " + decryptedPassword);
            username = decryptedUsername;
            password = decryptedPassword;
            params.put("username", username);
            params.put("password", password);
            params.put("checkCode", checkCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean addToDB(String goodName, String goodDescription, String goodImageName, double goodPrice, boolean goodOnSell, String sellerUsername) {
        Connection conn = JdbcUtils.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO mall_goods values(null,?,?,?,?,?,?)");
            preparedStatement.setString(1, goodName);
            preparedStatement.setString(2, goodDescription);
            preparedStatement.setString(3, goodImageName);
            preparedStatement.setDouble(4, goodPrice);
            preparedStatement.setBoolean(5, goodOnSell);
            preparedStatement.setString(6, sellerUsername);
            int i = preparedStatement.executeUpdate();
            JdbcUtils.close(preparedStatement, conn);
            if (i == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
