package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StoreController extends CommonController{

  private int maxItems = 5;

  @GetMapping("/store/{changePage}")
  public ModelAndView changeStorePage(@PathVariable(value ="changePage") Integer changePage, Model model) {
    List<Product> newProductList = new ArrayList<Product>();

    int endOfProductIds = maxItems * changePage;
    int i = 1;

    i = endOfProductIds - maxItems + 1;
  
    if (endOfProductIds < productService.size() ) {
      while (i <= endOfProductIds) {
        newProductList.add(productService.getProduct(i));
        i++;
      }
      model.addAttribute("showNext", true);
      model.addAttribute("showBack", true);
    }

    if (endOfProductIds == productService.size() ) {
      while (i <= endOfProductIds) {
        newProductList.add(productService.getProduct(i));
        i++;
      }

      model.addAttribute("showBack", true);
    }

    if (endOfProductIds > productService.size() ) {
      endOfProductIds = productService.size();
      
      while (i <= endOfProductIds) {
        newProductList.add(productService.getProduct(i));
        i++;
      }

      model.addAttribute("showBack", true);
    } 

    if (changePage == 1) {
      model.addAttribute("showBack", false);
    }

    model.addAttribute("newPage", changePage + 1);
    model.addAttribute("products", newProductList);

    createCartAndVoucher(model);

    
    /**
     * Pagination Logic
     */
    double totalPages = productService.size() / maxItems;
    if ((productService.size() % maxItems) > 0 ) {
      totalPages = totalPages + 1;
    }
    model.addAttribute("totalPages" , (int) totalPages);

    return new ModelAndView("store");
  }
}
