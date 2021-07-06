package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController extends CommonController{
  
  @GetMapping("/")
  private String indexPage(Model model) {

    if (hasRole("ADMIN")) {
      model.addAttribute("crud", true);
    }else {
      model.addAttribute("crud", false);
    }


    return "index";
  }
  
}
