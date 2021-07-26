package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebInfoController extends CommonController{
  
  // @GetMapping("/about-us")
  // private String goToAboutUs(Model model) {    

  //   getNumberOfProductsInCart(model);

  //   getUserRole(model);

  //   getUserFirstAndLastName(model);

  //   return "about-us";
  // }
  @GetMapping("/contact-us")
  private String goToContactUs(Model model) {    

    getNumberOfProductsInCart(model);

    getUserRole(model);

    getUserFirstAndLastName(model);

    return "contact-us";
  }
  @GetMapping("/faq")
  private String goToFAQ(Model model) {    

    getNumberOfProductsInCart(model);
    
    getUserRole(model);

    getUserFirstAndLastName(model);

    return "faq";
  }
}
