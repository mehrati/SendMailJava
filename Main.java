// use API javamail.jar

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

public class Main extends Application {
    Stage window;
    public HashMap<String,String> requires ;
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("MehdiMailer");
        //GridPane with 10px padding around edge
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 20));
        grid.setVgap(8);
        grid.setHgap(10);
        //Name Label - constrains use (child, column, row)
        Label emailLabel = new Label("Email :");
        //Name Input
        TextField emailField = new TextField();
        emailField.setPromptText("ایمیل ارسال کننده");
        //Password Label
        Label passLabel = new Label("Password :");
        //Password Input
        PasswordField passField = new PasswordField();
        passField.setPromptText("رمز");
        //rec Label
        Label recLabel = new Label("Recipient :");
        //Recipient Input
        TextField recipientField = new TextField();
        recipientField.setPromptText("ایمیل دریافت کننده");
        //Subject Label
        Label subjectLabel = new Label("Subject:");
        //Subject Input
        TextField subjectField = new TextField();
        subjectField.setPromptText("موضوع پیام");
        Label textLabel = new Label("Message:");
        //TextArea
        TextArea message = new TextArea();
//        message.righ
        message.setPromptText("پیام");

        //send Email
        Button sendButton = new Button("Send Email");

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String emailF = emailField.getText().toString();
                String passF = passField.getText().toString();
                String recipientF = recipientField.getText().toString();
                String subjectF = subjectField.getText().toString();
                String msg = message.getText().toString();
                if(emailF.length() != 0 && passF.length() != 0 && recipientF.length() != 0 && subjectF.length() != 0 && msg.length() != 0 ) {
                    requires = new HashMap<>();
                    requires.put("Email", emailF);
                    requires.put("Password", passF);
                    requires.put("Recipient",recipientF );
                    requires.put("Subject",subjectF );
                    requires.put("Message", msg);
                    Alert alertinfo = new Alert(Alert.AlertType.INFORMATION);
                    try{
                        Transport.send(SendMail.createMessage(requires));
                        alertinfo.setTitle("Ok");
                        alertinfo.setContentText("ایمیل ارسال شد");

                    }
                    catch (Exception ex){
                        alertinfo.setTitle("Error");
                        alertinfo.setContentText(ex.getMessage());
                    }
                    finally {
                        alertinfo.show();
                    }

                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("لطفا تمام موارد خواسته شده را وارد کنید");
                    alert.show();
                }
            }});

        grid.add(emailLabel,0,0);
        grid.add(emailField,1,0);
        grid.add(passLabel,0,1);
        grid.add(passField,1,1);
        grid.add(recLabel,0,2);
        grid.add(recipientField,1,2);
        grid.add(subjectLabel,0,3);
        grid.add(subjectField,1,3);
        grid.add(textLabel,0,4);
        grid.add(message,1,4);
        grid.add(sendButton,0,5);
        Scene scene = new Scene(grid, 700, 400);
        window.setScene(scene);
        window.show();
    }
}

class SendMail {

    public static Properties setProperties(String Email) {
        String patternYahoo = ".*($@yahoo.com)$";
        String patternOutlook = ".*(@outlook.com)$";
        String patternGmail = ".*(@gmail.com)$";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        if (Pattern.matches(patternGmail, Email)) {
            props.put("mail.smtp.host", "smtp.gmail.com");
        } else if (Pattern.matches(patternOutlook, Email)) {
            props.put("mail.smtp.host", "smtp-mail.outlook.com");
        } else if (Pattern.matches(patternYahoo, Email)) {
            props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        } else {
            System.out.println("Error !!!");
            System.exit(1);

        }
        props.put("mail.smtp.port", "587");
        return props;
    }

    public static Message createMessage(HashMap<String, String> values) throws Exception {
        Properties props = setProperties(values.get("Email"));
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(values.get("Email"), values.get("Password"));
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(values.get("Email")));
//        InternetAddress.parse(values.get("Recipient")
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(values.get("Recipient")));
        msg.setSubject(values.get("Subject"));
        msg.setText(values.get("Message"));
        msg.setSentDate(new Date());
        return msg;
    }

}

