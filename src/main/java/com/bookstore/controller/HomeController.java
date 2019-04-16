package com.bookstore.controller;

import com.bookstore.domain.Book;
import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;
import com.bookstore.service.impl.UserSecurityService;
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
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.*;

/**
 * created by saikat on 4/9/19
 */
@Controller
public class HomeController {


    private UserService userService;
    private BookService bookService;

    public HomeController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @RequestMapping({"","/","index","index.html"})
    public String index(){
        return "index";
    }



    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("classActiveLogin",true);
        return "myAccount";
    }



    @RequestMapping("/bookshelf")
    public String bookshelf(Model model){

        List<Book> boolList = bookService.findAll();
        model.addAttribute("bookList",boolList);
        return "bookshelf";
    }

    @RequestMapping("/bookDetail")
    public String bookDetail(
            @PathParam("id") Long id,
            Model model,Principal principal
    ){

        if(principal != null){
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user",user);
        }
        Book book = bookService.findOne(id);
        model.addAttribute("book",book);


        List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        model.addAttribute("qtyList",qtyList);
        model.addAttribute("qty",1);

        return "bookDetail";

    }

}
