package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StoreController extends CommonController{

  private int maxItems = 5;

  @GetMapping("/store/{changePage}")
  public ModelAndView openStore(
    @PathVariable(value ="changePage") Integer changePage,
    Model model) {

    List<Product> newProductList = new ArrayList<Product>();
    List<Product> oldProductList = productService.getAllProducts();
    
    pagination(model, changePage, newProductList, oldProductList);

    model.addAttribute("products", newProductList);

    createCartAndVoucher(model);
    model.addAttribute("uri", null);
    return new ModelAndView("store");
  }

  @GetMapping("/store/{changePage}/addFilters")
  private String addFilters(
    HttpServletRequest request,
    Model model,
    @PathVariable(value ="changePage") Integer changePage,
    @RequestParam("available") Optional<String> available,
    @RequestParam("preorder") Optional<String> preorder,
    @RequestParam("outOfStock") Optional<String> outOfStock,
    @RequestParam("priceMin") Optional<Integer> priceMin,
    @RequestParam("priceMax") Optional<Integer> priceMax,
    @RequestParam("fiveStar") Optional<Integer> fiveStar,
    @RequestParam("fourStar") Optional<Integer> fourStar,
    @RequestParam("threeStar") Optional<Integer> threeStar,
    @RequestParam("twoStar") Optional<Integer> twoStar,
    @RequestParam("oneStar") Optional<Integer> oneStar)  {
    
    List<Product> newProductList = new ArrayList<Product>();
    List<Optional<String>> statusList = new ArrayList<Optional<String>>();
    statusList.add(available);
    statusList.add(preorder);
    statusList.add(outOfStock);

    List<Optional<Integer>> ratingList = new ArrayList<Optional<Integer>>();
    ratingList.add(fiveStar);
    ratingList.add(fourStar);
    ratingList.add(threeStar);
    ratingList.add(twoStar);
    ratingList.add(oneStar);

    // Check if user picked a product status
    for (Optional<String> status : statusList) {
      if (status.isPresent()) {
        for (Product product : productService.getProductsByStatus(status.get())) {
          getProductsByPrice(model, newProductList, product, priceMin, 
                                  priceMax, ratingList);
        }

        model.addAttribute(status.get(), "checked");
      }
    }

    // When user did not pick any product status
    if (!available.isPresent() && !preorder.isPresent() && !outOfStock.isPresent()) {
      for (Product product : productService.getAllProducts()) {
        getProductsByPrice(model, newProductList, product, priceMin, 
                                priceMax, ratingList);
      }
    }

    List<Product> newDividedProductList = new ArrayList<Product>();
    pagination(model, changePage, newDividedProductList, newProductList);

    model.addAttribute("products", newDividedProductList);
    createCartAndVoucher(model);
    model.addAttribute("uri", "addFilters?" + request.getQueryString());
    return "store";
  }

  private void getProductsByPrice(
    Model model,
    List<Product> newProductList, Product productToAdd,
    Optional<Integer> priceMin, Optional<Integer> priceMax,
    List<Optional<Integer>> ratingList) {
    
    boolean isThereRatings = false;

    for (Optional<Integer> rating : ratingList) {
      if (rating.isPresent()) {
        isThereRatings = true;
        break;
      }
    }

    if(!priceMin.isPresent() && !priceMax.isPresent()) {
      if (isThereRatings) {
        getProductsByRating(model, ratingList, newProductList, productToAdd);
      } 
      else {
        newProductList.add(productToAdd);
      }
    }  
    else if(priceMin.isPresent() && !priceMax.isPresent() && 
            productToAdd.getPrice() >= priceMin.get()) {
      if (isThereRatings) {
        getProductsByRating(model, ratingList, newProductList, productToAdd);
      } 
      else {
        newProductList.add(productToAdd);
      }

      model.addAttribute("priceMin", priceMin.get());
    }
    else if(!priceMin.isPresent() && priceMax.isPresent() && 
            productToAdd.getPrice() <= priceMax.get()) {
      if (isThereRatings) {
        getProductsByRating(model, ratingList, newProductList, productToAdd);
      } 
      else {
        newProductList.add(productToAdd);
      }

      model.addAttribute("priceMax", priceMax.get());
    }
    else if(priceMin.isPresent() && priceMax.isPresent() && 
            productToAdd.getPrice() >= priceMin.get() && productToAdd.getPrice() <= priceMax.get()) {
      if (isThereRatings) {
        getProductsByRating(model, ratingList, newProductList, productToAdd);
      } 
      else {
        newProductList.add(productToAdd);
      }

      model.addAttribute("priceMin", priceMin.get());
      model.addAttribute("priceMax", priceMax.get());
    }
  }

  private void getProductsByRating(
    Model model,
    List<Optional<Integer>> ratingList, 
    List<Product> newProductList, Product productToAdd) {

    for (Optional<Integer> rating : ratingList) {
      if (rating.isPresent() && rating.get() == 5 && productToAdd.getRating() >= rating.get()) {
        newProductList.add(productToAdd);
      }
      else if (rating.isPresent() && productToAdd.getRating() >= rating.get()) {
        newProductList.add(productToAdd);
      }
    }
  }

  private void pagination(
    Model model, Integer changePage, 
    List<Product> newProductList, List<Product> productsToTransfer) {

    int endOfProducts = maxItems * changePage;

    double totalPages = productsToTransfer.size() / maxItems;
    if ((productsToTransfer.size() % maxItems) > 0 ) {
      totalPages = totalPages + 1;
    }
    model.addAttribute("totalPages" , (int) totalPages);

    if (changePage == 1) {
      for (int i = 0; i < maxItems; i++) {
        try {
          newProductList.add(productsToTransfer.get(i));
        } catch (Exception e) {
          break;
        }
      }

      if (totalPages != 1) {
        model.addAttribute("showNext", true);
      }
    }
    if (changePage > 1) {
      for (int i = endOfProducts - maxItems; i < endOfProducts; i++) {
        try {
          newProductList.add(productsToTransfer.get(i));
        } catch (Exception e) {
          break;
        }
      }

      model.addAttribute("showBack", true);
      if (changePage != totalPages) {
        model.addAttribute("showNext", true);
      }
    }
   
    model.addAttribute("newPage", changePage + 1);
  }
}
