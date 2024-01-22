package com.example.employee.web;

import com.example.employee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView, HttpSession httpSession){
        modelAndView.setViewName("index");
        modelAndView.addObject("name",httpSession.getAttribute("application-name"));
        return modelAndView;
    }
    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView, @AuthenticationPrincipal Principal principal){
        modelAndView.setViewName("home");
        modelAndView.addObject("user",principal);
        return modelAndView;
    }
}
