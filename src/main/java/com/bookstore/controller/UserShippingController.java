package com.bookstore.controller;

import com.bookstore.domain.User;
import com.bookstore.domain.UserShipping;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.service.impl.UserSecurityService;
import com.bookstore.utility.CountryList;
import com.bookstore.utility.MailConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * created by saikat on 4/18/19
 */
@Controller
public class UserShippingController {

    private UserService userService;
    private UserShippingService userShippingService;

    public UserShippingController(UserService userService, UserShippingService userShippingService) {
        this.userService = userService;
        this.userShippingService = userShippingService;
    }

    @RequestMapping("/listOfShippingAddresses")
    public String listOfShippingAddresses(
            Model model,Principal principal
    ){

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("userPaymentList",user.getUserPaymentList());
        model.addAttribute("userShippingList",user.getUserShippingList());
        /*model.addAttribute("orderList",user.getOrderList());*/

        model.addAttribute("listOfCreditCards",true);
        model.addAttribute("listOfShippingAddresses",true);
        model.addAttribute("classActiveShipping",true);

        return "myProfile";
    }

    @RequestMapping("/addNewShippingAddress")
    public String addNewShippingAddress(
            Model model, Principal principal
    ){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);

        UserShipping userShipping = new UserShipping();
        //for getting the model
        model.addAttribute("userShipping", userShipping);


        model.addAttribute("countryList", CountryList.getCountryList());
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
		/*model.addAttribute("orderList", user.orderList());*/

        return "myProfile";
    }

    @RequestMapping(value = "/addNewShippingAddress" ,method = RequestMethod.POST)
    public String addNewShippingAddress(
            @ModelAttribute("userShipping") UserShipping userShipping,
            Principal principal,
            Model model
    ){

        User user = userService.findByUsername(principal.getName());
        userService.updateUserShipping(userShipping,user);

        model.addAttribute("user", user);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfShippingAddresses",true);
        return "myProfile";

    }

    @RequestMapping("/updateUserShipping")
    public String updateUserShipping(
            @ModelAttribute("id") Long shippingAddressId,
            Principal principal,
            Model model
    ){
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(shippingAddressId);

        if(user.getId() != userShipping.getUser().getId()){
            return "badRequestPage";
        }else {
            model.addAttribute("userShipping",userShipping);
            model.addAttribute("user",user);
            model.addAttribute("countryList",CountryList.getCountryList());
            model.addAttribute("userPaymentList",user.getUserPaymentList());
            model.addAttribute("userShippingList",user.getUserShippingList());
            model.addAttribute("listOfCreditCards",true);
            model.addAttribute("classActiveShipping",true);
            model.addAttribute("addNewShippingAddress",true);

            return "myProfile";
        }
    }

    @RequestMapping(value = "/setDefaultShipping", method = RequestMethod.POST)
    public String setDefaultShipping(
            @ModelAttribute("defaultShipping") Long shippingAddressId,
            Principal principal,
            Model model
    ){
        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultShipping(shippingAddressId,user);

        model.addAttribute("user",user);
        model.addAttribute("userPaymentList",user.getUserPaymentList());
        model.addAttribute("userShippingList",user.getUserShippingList());

        model.addAttribute("listOfShippingAddresses",true);
        model.addAttribute("listOfCreditCards",true);
        model.addAttribute("classActiveShipping",true);

        return "myProfile";

    }

    @RequestMapping("/removeUserShipping")
    public String removeUserShipping(
            @ModelAttribute("id") Long shippingId,
            Principal principal,
            Model model
    ){
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(shippingId);

        if(user.getId() != userShipping.getUser().getId()){
            return "badRequestPage";
        }else {
            userShippingService.removeById(shippingId);
            model.addAttribute("userShipping",userShipping);
            model.addAttribute("user",user);
            model.addAttribute("userPaymentList",user.getUserPaymentList());
            model.addAttribute("userShippingList",user.getUserShippingList());
            model.addAttribute("listOfCreditCards",true);
            model.addAttribute("classActiveShipping",true);
            model.addAttribute("listOfShippingAddresses",true);

            return "myProfile";
        }

    }


}
