package com.bookstore.controller;

import org.springframework.stereotype.Controller;
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

    @RequestMapping({"myAccount"})
    public String myAccount(){
        return "my-account";
    }
}
