package com.bookstore.controller;

import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.UserService;
import com.bookstore.service.impl.UserSecurityService;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * created by saikat on 4/9/19
 */
@Controller
public class HomeController {


    private UserService userService;
    private UserSecurityService userSecurityService;
    private JavaMailSender mailSender;
    private MailConstructor mailConstructor;

    public HomeController(UserService userService, UserSecurityService userSecurityService, JavaMailSender mailSender, MailConstructor mailConstructor) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
        this.mailSender = mailSender;
        this.mailConstructor = mailConstructor;
    }

    @RequestMapping({"","/","index","index.html"})
    public String index(){
        return "index";
    }


    @RequestMapping(value = "/newUser",method = RequestMethod.POST)
    public String newUserPost(
            HttpServletRequest request,
            @ModelAttribute("email") String userEmail,
            @ModelAttribute("username") String username,
            Model model
    )throws Exception{
        model.addAttribute("classActiveNewAccount",true);
        model.addAttribute("email",userEmail);
        model.addAttribute("username",username);

        if(userService.findByUsername(username) != null){
            model.addAttribute("usernameExists",true);

            return "myAccount";
        }
        if(userService.findByEmail(userEmail) != null){
            model.addAttribute("userEmailExists",true);

            return "myAccount";
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);
        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user,role));
        userService.createUser(user,userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenforUser(user,token);

        String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl,
                                                request.getLocale(),token,user,password);

        mailSender.send(email);
        model.addAttribute("emailSent",true);

        return  "myAccount";
    }




    @RequestMapping("/newUser")
    public String newUser(
            Locale locale,
            @RequestParam("token") String token,
            Model model){
        PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

        if(passwordResetToken == null){
            String message = "Invalid Token";
            model.addAttribute("message",message);
            return "redirect:/badRequest";
        }

        User user = passwordResetToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(),userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("classActiveEdit",true);
        model.addAttribute("user",user);
        return "myProfile";
    }

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("classActiveLogin",true);
        return "myAccount";
    }

    @RequestMapping("/forgetPassword")
    public String forgetPassword(

            Model model){

        model.addAttribute("classActiveForgetPassword",true);
        return "myAccount";
    }
}
