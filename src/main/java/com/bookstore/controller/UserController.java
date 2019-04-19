package com.bookstore.controller;

import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.impl.UserSecurityService;
import com.bookstore.utility.CountryList;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;
import com.bookstore.utility.USConstants;
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
import java.security.Principal;
import java.util.*;

/**
 * created by saikat on 4/16/19
 */
@Controller
public class UserController {

    private UserService userService;
    private UserSecurityService userSecurityService;
    private JavaMailSender mailSender;
    private MailConstructor mailConstructor;
    private UserPaymentService userPaymentService;


    public UserController(UserService userService, UserSecurityService userSecurityService, JavaMailSender mailSender, MailConstructor mailConstructor, UserPaymentService userPaymentService) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
        this.mailSender = mailSender;
        this.mailConstructor = mailConstructor;
        this.userPaymentService = userPaymentService;
    }

    @RequestMapping("/newUser")
    public String newUser(
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
            model.addAttribute("emailExists",true);

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


    @RequestMapping("/forgetPassword")
    public String forgetPassword(
            HttpServletRequest request,
            @ModelAttribute("email") String email,
            Model model
    ){

        model.addAttribute("classActiveForgetPassword",true);

        User user = userService.findByEmail(email);

        if(user == null){
            model.addAttribute("emailNotExist",true);
            return "myAccount";
        }
        else {
            String password = SecurityUtility.randomPassword();

            String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
            user.setPassword(encryptedPassword);

            userService.save(user);

            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenforUser(user,token);

            String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

            SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl,
                    request.getLocale(),token,user,password);

            mailSender.send(newEmail);
            model.addAttribute("forgetPasswordEmailSent",true);
        }


        return "myAccount";
    }


    @RequestMapping("/myProfile")
    public String myProfile(Model model,Principal principal){

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("userPaymentList",user.getUserPaymentList());
        model.addAttribute("userShippingList",user.getUserShippingList());
        /*model.addAttribute("orderList",user.getOrderList());*/

      /*  UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping",userShipping);*/

        model.addAttribute("listOfCreditCards",true);
        model.addAttribute("listOfShippingAddresses",true);
        model.addAttribute("classActiveEdit",true);

        return "myProfile";
    }












}



