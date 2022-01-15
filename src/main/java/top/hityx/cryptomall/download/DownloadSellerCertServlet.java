package top.hityx.cryptomall.download;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/downloadSellerCert")
public class DownloadSellerCertServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = "seller.cert";
        resp.setHeader("content-type", "application/octet-stream");
        resp.setHeader("content-disposition", "attachment;filename=" + filename);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("D:\\IdeaProjects\\cryptoMall\\src\\main\\java\\top\\hityx\\cryptomall\\download\\cert\\seller证书.cert");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("完成创建FileInputStream");
        // 创建ServletOutputStream
        System.out.println("创建ServletOutputStream");
        ServletOutputStream servletOutputStream = resp.getOutputStream();
        System.out.println("完成创建ServletOutputStream");
        byte[] buff = new byte[5000 * 8];
        int len = 0;
        while ((len = fileInputStream.read(buff)) != -1) {
            servletOutputStream.write(buff, 0, len);
        }
        fileInputStream.close();
    }
}
