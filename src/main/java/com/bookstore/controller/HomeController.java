package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by saikat on 4/9/19
 */
@Controller
public class HomeController {

    @RequestMapping({"","/","index","index.html"})
    public String index(){
        return "index";
    }


    @RequestMapping("/newUser")
    public String newUser(Model model){
        model.addAttribute("classActiveNewAccount",true);
        return "my-account";
    }

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("classActiveLogin",true);
        return "my-account";
    }

    @RequestMapping("/forgetPassword")
    public String forgetPassword(Model model){
        model.addAttribute("classActiveForgetPassword",true);
        return "my-account";
    }
}
