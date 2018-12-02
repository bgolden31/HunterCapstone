package registration;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.json.JSONObject;

public class Email {
    private static String from = "recipeinprogress";  // GMail user name (just the part before "@gmail.com")
    private  static String pass = "food1516"; // GMail password
    
    public static void verify(JSONObject data) {
    	
        String[] to = { data.getString("email") }; // list of recipient email addresses
        String subject = "RecipeInProgress Verfication";
        String body = "https://www.youtube.com/watch?v=6n3pFFPSlW4 \n";
        
        String verify = "	http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/user/verify/" + data.getString("username");
        sendFromGMail(to, subject, body +verify);
    }
    
    public static void forgotPassword(JSONObject data) {
    	
        String[] to = { data.getString("email") }; // list of recipient email addresses
        String subject = "RecipeInProgress Password";
        String body =  "Username:" + data.getString("username") +"\n Password:" + data.getString("password");
        sendFromGMail(to, subject, body);
    }
    
    

    private static void sendFromGMail(String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("sent?");
        }
        catch (AddressException ae) {
            ae.printStackTrace();
            System.out.println(ae);
        }
        catch (MessagingException me) {
            me.printStackTrace();
            System.out.println(me);
        }
    }
}


