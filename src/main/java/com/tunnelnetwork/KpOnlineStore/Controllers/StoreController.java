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

  @GetMapping("/store/{changePage}/storeSearch")
  private String search(
    HttpServletRequest request, Model model, 
    @PathVariable(value ="changePage") Integer changePage,
    @RequestParam("storeSearch") Optional<String> storeSearch) {
    
    List<Product> newProductList = new ArrayList<Product>();
    List<Product> newDividedProductList = new ArrayList<Product>();
    List<Product> productListBasedOnName = productService.getProductsContainingInName(storeSearch.get());
    List<Product> productListBasedOnCategory = productService.getProductsContainingInCategory(storeSearch.get());

    if (!storeSearch.isEmpty()) {
      if (productListBasedOnName != null) {
        for (Product product : productListBasedOnName) {
          newProductList.add(product);
        }
      }
      else if (productListBasedOnCategory != null) {
        for (Product product : productListBasedOnCategory) {
          newProductList.add(product);
        }
      }
    } else {
      for (Product product : productService.getAllProducts()) {
        newProductList.add(product);
      }
    }

    pagination(model, changePage, newDividedProductList, newProductList);

    model.addAttribute("products", newDividedProductList);
    createCartAndVoucher(model);
    model.addAttribute("uri", "storeSearch?" + request.getQueryString());

    return "store";
  }

  @GetMapping("/store/{changePage}/addFilters")
  private String addFilters(
    HttpServletRequest request,
    Model model,
    @PathVariable(value ="changePage") Integer changePage,
    @RequestParam("available") Optional<String> available,
    @RequestParam("preorder") Optional<String> preorder,
    @RequestParam("outOfStock") Optional<String> outOfStock,
    @RequestParam("bestSeller") Optional<String> bestSeller,
    @RequestParam("album") Optional<String> albums,
    @RequestParam("doll") Optional<String> dolls,
    @RequestParam("imagePicket") Optional<String> imagePickets,
    @RequestParam("lightStick") Optional<String> lightSticks,
    @RequestParam("package") Optional<String> packages,
    @RequestParam("phoneCase") Optional<String> phoneCases,
    @RequestParam("photobook") Optional<String> photobooks,
    @RequestParam("photocard") Optional<String> photocards,
    @RequestParam("poster") Optional<String> posters,
    @RequestParam("sticker") Optional<String> stickers,
    @RequestParam("keyRing") Optional<String> keyRings,
    @RequestParam("apparel") Optional<String> apparels,
    @RequestParam("priceMin") Optional<Integer> priceMin,
    @RequestParam("priceMax") Optional<Integer> priceMax,
    @RequestParam("rating") Optional<Integer> rating)  {
    
    List<Product> newProductList = new ArrayList<Product>();

    boolean isStatusListEmpty = true;
    List<Optional<String>> statusList = new ArrayList<Optional<String>>();
    statusList.add(available);
    statusList.add(preorder);
    statusList.add(outOfStock);

    boolean isCategoryListEmpty = true;
    List<Optional<String>> categoryList = new ArrayList<Optional<String>>();
    categoryList.add(albums);
    categoryList.add(dolls);
    categoryList.add(imagePickets);
    categoryList.add(lightSticks);
    categoryList.add(packages);
    categoryList.add(phoneCases);
    categoryList.add(photobooks);
    categoryList.add(photocards);
    categoryList.add(posters);
    categoryList.add(stickers);
    categoryList.add(keyRings);
    categoryList.add(apparels);

    for (Optional<String> status : statusList) {
      if (status.isPresent()) {
        isStatusListEmpty = false;
        break;
      }
    }
    for (Optional<String> category : categoryList) {
      if (category.isPresent()) {
        isCategoryListEmpty = false;
        break;
      }
    }

    // If user picked a status but no category
    if (!isStatusListEmpty && isCategoryListEmpty) {
      for (Optional<String> status : statusList) {
        if (status.isPresent()) {
          if (bestSeller.isPresent()) {
            for (Product product : productService.getProductsByBestSeller(
                                   productService.getProductsByStatus(status.get()))) {
              getProductsByPrice(model, newProductList, product, priceMin, 
                                      priceMax, rating);
            } 

            model.addAttribute(bestSeller.get().toUpperCase(), "checked");
          } else {
            for (Product product : productService.getProductsByStatus(status.get())) {
              getProductsByPrice(model, newProductList, product, priceMin, 
                                      priceMax, rating);
            }
          }

          model.addAttribute(status.get().toUpperCase(), "checked");
        }
      }
    } 
    // If user picked a category but no status
    if (isStatusListEmpty && !isCategoryListEmpty) {
      for (Optional<String> category : categoryList) {
        if (category.isPresent()) {
          if (bestSeller.isPresent()) {
            for (Product product : productService.getProductsByBestSeller(
                                   productService.getProductsByCategory(category.get()))) {
              getProductsByPrice(model, newProductList, product, priceMin, 
                                      priceMax, rating);
            }

            model.addAttribute(bestSeller.get().toUpperCase(), "checked");
          } else {
            for (Product product : productService.getProductsByCategory(category.get())) {
              getProductsByPrice(model, newProductList, product, priceMin, 
                                      priceMax, rating);
            }
          }
          
          model.addAttribute(category.get().toUpperCase(), "checked");
        }
      }
    }
    // If user picked a category and status
    if (!isStatusListEmpty && !isCategoryListEmpty) {
      List<Product> productsBasedStatus = new ArrayList<Product>();

      for (Optional<String> status : statusList) {
        if (status.isPresent()) {
          if (bestSeller.isPresent()) {
            for (Product product : productService.getProductsByBestSeller(
                                   productService.getProductsByStatus(status.get()))){
              productsBasedStatus.add(product);
            }

            model.addAttribute(bestSeller.get().toUpperCase(), "checked");
          } else {
            for (Product product : productService.getProductsByStatus(status.get())) {
              productsBasedStatus.add(product);
            }
          }
          
          model.addAttribute(status.get().toUpperCase(), "checked");
        }
      }
      for (Optional<String> category : categoryList) {
        if (category.isPresent()) {
          for (Product product : productsBasedStatus) {
            if (product.getCategory().equals(category.get())) {
              getProductsByPrice(model, newProductList, product, priceMin, 
                                        priceMax, rating);
            }
          }

          model.addAttribute(category.get().toUpperCase(), "checked");
        }
      }
    }

    // When user did not pick any product status and category
    if (isStatusListEmpty && isCategoryListEmpty) {
        if (bestSeller.isPresent()) {
          for (Product product : productService.getProductsByBestSeller(productService.getAllProducts())) {
            getProductsByPrice(model, newProductList, product, priceMin, 
                                        priceMax, rating);
          }

          model.addAttribute(bestSeller.get().toUpperCase(), "checked");
        } else {
          for (Product product : productService.getAllProducts()) {
            getProductsByPrice(model, newProductList, product, priceMin, 
                                        priceMax, rating);
          }
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
    Optional<Integer> rating) {

    if(!priceMin.isPresent() && !priceMax.isPresent()) {
      if (rating.isPresent()) {
        getProductsByRating(model, rating, newProductList, productToAdd);
      } 
      else {
        newProductList.add(productToAdd);
      }
    }  
    else if(priceMin.isPresent() && !priceMax.isPresent() && 
            productToAdd.getPrice() >= priceMin.get()) {
      if (rating.isPresent()) {
        getProductsByRating(model, rating, newProductList, productToAdd);
      } 
      else {
        newProductList.add(productToAdd);
      }

      model.addAttribute("priceMin", priceMin.get());
    }
    else if(!priceMin.isPresent() && priceMax.isPresent() && 
            productToAdd.getPrice() <= priceMax.get()) {
      if (rating.isPresent()) {
        getProductsByRating(model, rating, newProductList, productToAdd);
      } 
      else {
        newProductList.add(productToAdd);
      }

      model.addAttribute("priceMax", priceMax.get());
    }
    else if(priceMin.isPresent() && priceMax.isPresent() && 
            productToAdd.getPrice() >= priceMin.get() && productToAdd.getPrice() <= priceMax.get()) {
      if (rating.isPresent()) {
        getProductsByRating(model, rating, newProductList, productToAdd);
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
    Optional<Integer> rating, 
    List<Product> newProductList, Product productToAdd) {

    if (rating.isPresent() && rating.get() == 5 && productToAdd.getRating() == rating.get()) {
      newProductList.add(productToAdd);
    }
    else if (rating.isPresent() && productToAdd.getRating() >= rating.get()) {
      newProductList.add(productToAdd);
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
