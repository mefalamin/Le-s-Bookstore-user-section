package com.bookstore.utility;

import com.bookstore.domain.User;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * created by saikat on 4/11/19
 */
@Component
public class MailConstructor {

    private Environment env;

    public MailConstructor(Environment env) {
        this.env = env;
    }

    public SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user, String password
    ){
        String url = contextPath+ "/newUser?token=" +token;
        String message = "\nPlease click on this link to verify your email and edit your personal " +
                "information.Your password is: \n"+password;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Le's Bookstore - New User");
        email.setText(url+message);
        email.setFrom(env.getProperty("support.email"));

        return email;

    }
}
