package com.lostandfind.controller;

import com.lostandfind.domain.User;
import com.lostandfind.utils.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet("/sendEmail")
public class sendEmail extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        if("PUT".equals(request.getParameter("_method"))){
            sendMessage(request, response);
        }else{
            sendEmail(request, response);
        }
    }

    public void sendEmail(HttpServletRequest request, HttpServletResponse response){
        String email = TransformRequest.transform(request, "email");
        System.out.println(email);
        try {
            JavaMailUtil.receiveMailAccount = email; // 给用户输入的邮箱发送邮件

            // 1、创建参数配置，用于连接邮箱服务器的参数配置
            Properties props = new Properties();
            // 开启debug调试
            // props.setProperty("mail.debug", "true");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            // 设置右键服务器的主机名
            props.setProperty("mail.host", JavaMailUtil.emailSMTPHost);
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");

            // 2、根据配置创建会话对象，用于和邮件服务器交互
            Session session = Session.getInstance(props);
            // 设置debug，可以查看详细的发送log
            // session.setDebug(true);
            // 3、创建一封邮件
            String code = RandomUtil.getRandom();
            System.out.println("邮箱验证码：" + code);
            String html = htmlText.html(code);
            MimeMessage message = JavaMailUtil.creatMimeMessage(session, JavaMailUtil.emailAccount,
                    JavaMailUtil.receiveMailAccount, html);

            // 4、根据session获取邮件传输对象
            Transport transport = session.getTransport();
            // 5、使用邮箱账号和密码连接邮箱服务器emailAccount必须与message中的发件人邮箱一致，否则报错
            transport.connect(JavaMailUtil.emailAccount, JavaMailUtil.emailPassword);
            // 6、发送邮件,发送所有收件人地址
            transport.sendMessage(message, message.getAllRecipients());
            // 7、关闭连接
            transport.close();
            //  写入session
			/*req.getSession().removeAttribute("code");
			req.getSession().setAttribute("code", code);
			req.getSession().setMaxInactiveInterval(30);*/
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", 200);
            data.put("content", code);
            data.put("msg", "新建成功");
            ResponseJsonUtils.json(response, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(HttpServletRequest request, HttpServletResponse response){
        String userId = TransformRequest.transform(request, "userId");
        System.out.println(userId);
        String email = "";
        try {
            Connection conn = null;
            QueryRunner queryRunner = new QueryRunner();
            String sql = "SELECT email FROM users WHERE userId = ?";
            Object[] params = {userId};

            //QueryRunner提供了两个方法，query、update
            try{
                conn = C3P0utils.getConnection();
                User user = queryRunner.query(conn, sql, new BeanHandler<User>(User.class), params);
                email = user.getEmail();

            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                C3P0utils.close(conn, null, null);
            }

            System.out.println(email);
            JavaMailUtil.receiveMailAccount = email; // 给用户输入的邮箱发送邮件

            // 1、创建参数配置，用于连接邮箱服务器的参数配置
            Properties props = new Properties();
            // 开启debug调试
            // props.setProperty("mail.debug", "true");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            // 设置右键服务器的主机名
            props.setProperty("mail.host", JavaMailUtil.emailSMTPHost);
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");

            // 2、根据配置创建会话对象，用于和邮件服务器交互
            Session session = Session.getInstance(props);
            // 设置debug，可以查看详细的发送log
            // session.setDebug(true);
            // 3、创建一封邮件
            String html = htmlText.connectHtml();
            MimeMessage message = JavaMailUtil.creatMimeMessage(session, JavaMailUtil.emailAccount,
                    JavaMailUtil.receiveMailAccount, html);

            // 4、根据session获取邮件传输对象
            Transport transport = session.getTransport();
            // 5、使用邮箱账号和密码连接邮箱服务器emailAccount必须与message中的发件人邮箱一致，否则报错
            transport.connect(JavaMailUtil.emailAccount, JavaMailUtil.emailPassword);
            // 6、发送邮件,发送所有收件人地址
            transport.sendMessage(message, message.getAllRecipients());
            // 7、关闭连接
            transport.close();
            //  写入session
			/*req.getSession().removeAttribute("code");
			req.getSession().setAttribute("code", code);
			req.getSession().setMaxInactiveInterval(30);*/
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", 200);
            data.put("content", null);
            data.put("msg", "发送成功");
            ResponseJsonUtils.json(response, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
