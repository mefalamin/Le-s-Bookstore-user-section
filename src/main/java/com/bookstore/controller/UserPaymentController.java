package com.bookstore.controller;

import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
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
 * created by saikat on 4/19/19
 */
@Controller
public class UserPaymentController {

    private UserService userService;
    private UserPaymentService userPaymentService;

    public UserPaymentController(UserService userService, UserPaymentService userPaymentService) {
        this.userService = userService;
        this.userPaymentService = userPaymentService;
    }

    @RequestMapping("/listOfCreditCards")
    public String listOfCreditCards(Model model, Principal principal){

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("userPaymentList",user.getUserPaymentList());
        model.addAttribute("userShippingList",user.getUserShippingList());
        /*model.addAttribute("orderList",user.getOrderList());*/

        model.addAttribute("listOfCreditCards",true);
        model.addAttribute("listOfShippingAddresses",true);
        model.addAttribute("classActiveBilling",true);

        return "myProfile";
    }



    @RequestMapping("/addNewCreditCard")
    public String addNewCreditCard(
            Model model, Principal principal
    ){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        //for showing the form
        model.addAttribute("addNewCreditCard", true);
        //for tab pane active
        model.addAttribute("classActiveBilling", true);

        //shipping tab data load
        model.addAttribute("listOfShippingAddresses", true);


        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();

        //for getting the model of billing address and payment card details
        model.addAttribute("userBilling", userBilling);
        model.addAttribute("userPayment", userPayment);


        model.addAttribute("countryList", CountryList.getCountryList());
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
		/*model.addAttribute("orderList", user.orderList());*/

        return "myProfile";
    }


    @RequestMapping("/updateCreditCard")
    public String updateCreditCard(
            @ModelAttribute("id") Long creditCardId,
            Principal principal,
            Model model
    ){
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getUser().getId()){
            return "badRequestPage";
        }else {
            UserBilling userBilling = userPayment.getUserBilling();
            model.addAttribute("user",user);
            model.addAttribute("userPayment",userPayment);
            model.addAttribute("userBilling",userBilling);
            model.addAttribute("countryList",CountryList.getCountryList());
            model.addAttribute("userPaymentList",user.getUserPaymentList());
            model.addAttribute("userShippingList",user.getUserShippingList());
            model.addAttribute("listOfShippingAddresses",true);
            model.addAttribute("classActiveBilling",true);
            model.addAttribute("addNewCreditCard", true);
            return "myProfile";
        }
    }

    @RequestMapping("/removeCreditCard")
    public String removeCreditCard(
            @ModelAttribute("id") Long creditCardId,
            Principal principal,
            Model model
    ){
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getUser().getId()){
            return "badRequestPage";
        }else {

            userPaymentService.removeById(creditCardId);

            model.addAttribute("user",user);
            model.addAttribute("userPaymentList",user.getUserPaymentList());
            model.addAttribute("userShippingList",user.getUserShippingList());

            model.addAttribute("listOfShippingAddresses",true);
            model.addAttribute("listOfCreditCards",true);
            model.addAttribute("classActiveBilling",true);

            return "myProfile";
        }

    }

    @RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
    public String setDefaultPayment(
            @ModelAttribute("defaultPayment") Long userPaymentId,
            Principal principal,
            Model model
    ){
        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultPayment(userPaymentId,user);

        model.addAttribute("user",user);
        model.addAttribute("userPaymentList",user.getUserPaymentList());
        model.addAttribute("userShippingList",user.getUserShippingList());

        model.addAttribute("listOfShippingAddresses",true);
        model.addAttribute("listOfCreditCards",true);
        model.addAttribute("classActiveBilling",true);

        return "myProfile";

    }

    @RequestMapping(value = "/addNewCreditCard" ,method = RequestMethod.POST)
    public String addNewCreditCard(
            @ModelAttribute("userPayment") UserPayment userPayment,
            @ModelAttribute("userBilling") UserBilling userBilling,
            Model model, Principal principal
    ){
        User user = userService.findByUsername(principal.getName());
        userService.updateUserBilling(userBilling,userPayment,user);

        model.addAttribute("user",user);
        model.addAttribute("userPaymentList",user.getUserPaymentList());
        model.addAttribute("userShippingList",user.getUserShippingList());
        model.addAttribute("listOfCreditCards",true);
        model.addAttribute("classActiveBilling",true);
        model.addAttribute("ListOfShippingAddress",true);

        return "myProfile";
    }
}
