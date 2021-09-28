package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.LinkedHashSet;

import javax.servlet.http.HttpServletRequest;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StoreController extends CommonController {

  private int maxItems = 16;

  @GetMapping("/store/{changePage}")
  public String openStore(@PathVariable(value = "changePage") Integer changePage, Model model) {

    if (isThereLoggedInUser()) {
      createCartAndVoucher(model);
    }

    getNumberOfProductsInCart(model);

    getUserRole(model);

    getUserFirstAndLastName(model);

    List<Product> newProductList = new ArrayList<Product>();
    List<Product> oldProductList = productRepository.getAllProducts();

    pagination(model, this.maxItems, changePage, newProductList, oldProductList);

    model.addAttribute("products", newProductList);

    model.addAttribute("uri", "");

    return "store";
  }

  @GetMapping("/store/{changePage}/storeSearch")
  private String search(HttpServletRequest request, Model model, @PathVariable(value = "changePage") Integer changePage,
      @RequestParam("storeSearch") Optional<String> storeSearch) {

    if (isThereLoggedInUser()) {
      createCartAndVoucher(model);
    }

    getNumberOfProductsInCart(model);

    getUserRole(model);

    getUserFirstAndLastName(model);

    List<Product> newProductList = new ArrayList<Product>();
    List<Product> newDividedProductList = new ArrayList<Product>();
    List<Product> productListBasedOnName = productRepository.getProductsContainingInName(storeSearch.get());
    List<Product> productListBasedOnBand = productRepository.getProductsContainingInBand(storeSearch.get());
    List<Product> productListBasedOnCategory = productRepository.getProductsContainingInCategory(storeSearch.get());

    if (!storeSearch.isEmpty()) {
      if (productListBasedOnName != null) {
        for (Product product : productListBasedOnName) {
          newProductList.add(product);
        }
      }
      if (productListBasedOnBand != null) {
        for (Product product : productListBasedOnBand) {
          newProductList.add(product);
        }
      }
      if (productListBasedOnCategory != null) {
        for (Product product : productListBasedOnCategory) {
          newProductList.add(product);
        }
      }
    } else {
      for (Product product : productRepository.getAllProducts()) {
        newProductList.add(product);
      }
    }

    List<Product> removeDuplicatesProductList = new ArrayList<>(new LinkedHashSet<>(newProductList));

    pagination(model, this.maxItems, changePage, newDividedProductList, removeDuplicatesProductList);

    model.addAttribute("products", newDividedProductList);

    // uri to put in our page buttons
    model.addAttribute("uri", "storeSearch?" + request.getQueryString());

    return "store";
  }

  @GetMapping("/store/{changePage}/addFilters")
  private String addFilters(HttpServletRequest request, Model model,
      @PathVariable(value = "changePage") Integer changePage, @RequestParam("available") Optional<String> available,
      @RequestParam("preorder") Optional<String> preorder, @RequestParam("outOfStock") Optional<String> outOfStock,
      @RequestParam("bestSeller") Optional<String> bestSeller, @RequestParam("gIdle") Optional<String> gIdle,
      @RequestParam("blackpink") Optional<String> blackpink, @RequestParam("bts") Optional<String> bts,
      @RequestParam("day6") Optional<String> day6, @RequestParam("enhypen") Optional<String> enhypen,
      @RequestParam("exo") Optional<String> exo, @RequestParam("girlsGeneration") Optional<String> girlsGeneration,
      @RequestParam("itzy") Optional<String> itzy, @RequestParam("iu") Optional<String> iu,
      @RequestParam("mamamoo") Optional<String> mamamoo, @RequestParam("nct") Optional<String> nct,
      @RequestParam("redVelvet") Optional<String> redVelvet, @RequestParam("seventeen") Optional<String> seventeen,
      @RequestParam("strayKids") Optional<String> strayKids,
      @RequestParam("tomorrowXTogether") Optional<String> tomorrowXTogether,
      @RequestParam("twice") Optional<String> twice, @RequestParam("album") Optional<String> albums,
      @RequestParam("doll") Optional<String> dolls, @RequestParam("imagePicket") Optional<String> imagePickets,
      @RequestParam("lightStick") Optional<String> lightSticks, @RequestParam("package") Optional<String> packages,
      @RequestParam("phoneCase") Optional<String> phoneCases, @RequestParam("photobook") Optional<String> photobooks,
      @RequestParam("photocard") Optional<String> photocards, @RequestParam("poster") Optional<String> posters,
      @RequestParam("sticker") Optional<String> stickers, @RequestParam("keyRing") Optional<String> keyRings,
      @RequestParam("apparel") Optional<String> apparels, @RequestParam("priceMin") Optional<Long> priceMin,
      @RequestParam("priceMax") Optional<Long> priceMax, @RequestParam("rating") Optional<Integer> rating) {

    if (isThereLoggedInUser()) {
      createCartAndVoucher(model);
    }

    getNumberOfProductsInCart(model);

    getUserRole(model);

    getUserFirstAndLastName(model);

    List<Product> newProductList = new ArrayList<Product>();

    boolean isStatusListEmpty = true;
    List<Optional<String>> statusList = new ArrayList<Optional<String>>();
    statusList.add(available);
    statusList.add(preorder);
    statusList.add(outOfStock);

    boolean isBandListEmpty = true;
    List<Optional<String>> bandList = new ArrayList<Optional<String>>();
    bandList.add(gIdle);
    bandList.add(blackpink);
    bandList.add(bts);
    bandList.add(day6);
    bandList.add(enhypen);
    bandList.add(exo);
    bandList.add(girlsGeneration);
    bandList.add(itzy);
    bandList.add(iu);
    bandList.add(mamamoo);
    bandList.add(nct);
    bandList.add(redVelvet);
    bandList.add(seventeen);
    bandList.add(strayKids);
    bandList.add(tomorrowXTogether);
    bandList.add(twice);

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

    // Check if at least status and category have one present value/checkbox
    for (Optional<String> status : statusList) {
      if (status.isPresent()) {
        isStatusListEmpty = false;
        break;
      }
    }
    for (Optional<String> band : bandList) {
      if (band.isPresent()) {
        isBandListEmpty = false;
        break;
      }
    }
    for (Optional<String> category : categoryList) {
      if (category.isPresent()) {
        isCategoryListEmpty = false;
        break;
      }
    }

    if (!isStatusListEmpty) {
      for (Optional<String> status : statusList) {
        if (status.isPresent()) {

          getProductsByBestSeller(model, bestSeller, newProductList,
              productRepository.getProductsByStatus(status.get()), priceMin, priceMax, rating);

          model.addAttribute(status.get().toUpperCase(), "checked");
        }
      }
    }

    if (!isCategoryListEmpty) {
      for (Optional<String> category : categoryList) {
        if (category.isPresent()) {

          getProductsByBestSeller(model, bestSeller, newProductList,
              productRepository.getProductsByCategory(category.get()), priceMin, priceMax, rating);

          model.addAttribute(category.get().replaceAll("[^a-zA-Z0-9]", "").toUpperCase(), "checked");
        }
      }
    }

    if (!isBandListEmpty) {
      for (Optional<String> band : bandList) {
        if (band.isPresent()) {

          getProductsByBestSeller(model, bestSeller, newProductList, productRepository.getProductsByBand(band.get()),
              priceMin, priceMax, rating);

          model.addAttribute(band.get().replaceAll("[^a-zA-Z0-9]", "").toUpperCase(), "checked");
        }
      }
    }

    // When user did not pick any product status, band, & category
    if (isStatusListEmpty && isCategoryListEmpty && isBandListEmpty) {
      getProductsByBestSeller(model, bestSeller, newProductList, productRepository.getAllProducts(), priceMin, priceMax,
          rating);
    }

    List<Product> removeDuplicatesProductList = new ArrayList<>(new LinkedHashSet<>(newProductList));
    // List<Product> productsToRemove = new ArrayList<Product>();

    // if (!isStatusListEmpty && !isCategoryListEmpty && isBandListEmpty) {
    // for (Product product : removeDuplicatesProductList) {
    // for (Optional<String> status : statusList) {
    // if (status.isPresent()) {
    // for (Optional<String> category : categoryList){
    // if (category.isPresent()) {
    // if (!product.getCategory().equals(category.get()) &&
    // !product.getStatus().equals(status.get())) {
    // productsToRemove.add(product);
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // else if (!isBandListEmpty && !isCategoryListEmpty && isStatusListEmpty) {
    // for (Product product : removeDuplicatesProductList) {
    // for (Optional<String> band : bandList) {
    // if (band.isPresent()) {
    // for (String tag : product.getTags()) {
    // for (Optional<String> category : categoryList){
    // if (category.isPresent()) {
    // if (!product.getCategory().equals(category.get()) &&
    // !tag.equals(band.get())) {
    // productsToRemove.add(product);
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // else if (!isBandListEmpty && !isStatusListEmpty && isCategoryListEmpty) {
    // for (Product product : removeDuplicatesProductList) {
    // for (Optional<String> band : bandList) {
    // if (band.isPresent()) {
    // for (String tag : product.getTags()) {
    // for (Optional<String> status : statusList){
    // if (status.isPresent()) {
    // if (!product.getStatus().equals(status.get()) &&
    // !tag.equals(band.get())) {
    // productsToRemove.add(product);
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }

    // for (Product product : productsToRemove) {
    // removeDuplicatesProductList.remove(product);
    // }

    List<Product> newDividedProductList = new ArrayList<Product>();
    pagination(model, this.maxItems, changePage, newDividedProductList, removeDuplicatesProductList);

    model.addAttribute("products", newDividedProductList);

    if (rating.isPresent()) {
      model.addAttribute("btnRating", rating.get());
    }

    // uri to put in our page buttons
    model.addAttribute("uri", "addFilters?" + request.getQueryString());

    return "store";
  }

  private void getProductsByBestSeller(Model model, Optional<String> bestSeller, List<Product> newProductList,
      List<Product> filterList, Optional<Long> priceMin, Optional<Long> priceMax, Optional<Integer> rating) {

    if (bestSeller.isPresent()) {
      for (Product product : productRepository.getProductsByBestSeller(filterList)) {
        getProductsByPrice(model, newProductList, product, priceMin, priceMax, rating);
      }

      model.addAttribute(bestSeller.get().toUpperCase(), "checked");
    } else {
      for (Product product : filterList) {
        getProductsByPrice(model, newProductList, product, priceMin, priceMax, rating);
      }
    }
  }

  private void getProductsByPrice(Model model, List<Product> newProductList, Product productToAdd,
      Optional<Long> priceMin, Optional<Long> priceMax, Optional<Integer> rating) {

    if (!priceMin.isPresent() && !priceMax.isPresent()) {
      if (rating.isPresent()) {
        getProductsByRating(rating, newProductList, productToAdd);
      } else {
        newProductList.add(productToAdd);
      }
    } else if (priceMin.isPresent() && !priceMax.isPresent() && productToAdd.getPrice() >= priceMin.get()) {
      if (rating.isPresent()) {
        getProductsByRating(rating, newProductList, productToAdd);
      } else {
        newProductList.add(productToAdd);
      }

      model.addAttribute("priceMin", priceMin.get());
    } else if (!priceMin.isPresent() && priceMax.isPresent() && productToAdd.getPrice() <= priceMax.get()) {
      if (rating.isPresent()) {
        getProductsByRating(rating, newProductList, productToAdd);
      } else {
        newProductList.add(productToAdd);
      }

      model.addAttribute("priceMax", priceMax.get());
    } else if (priceMin.isPresent() && priceMax.isPresent() && productToAdd.getPrice() >= priceMin.get()
        && productToAdd.getPrice() <= priceMax.get()) {
      if (rating.isPresent()) {
        getProductsByRating(rating, newProductList, productToAdd);
      } else {
        newProductList.add(productToAdd);
      }

      model.addAttribute("priceMin", priceMin.get());
      model.addAttribute("priceMax", priceMax.get());
    }
  }

  private void getProductsByRating(Optional<Integer> rating, List<Product> newProductList, Product productToAdd) {

    if (!rating.isPresent()) {
      newProductList.add(productToAdd);
    } else if (rating.isPresent() && rating.get() == 5 && productToAdd.getRating() == rating.get()) {
      newProductList.add(productToAdd);
    } else if (rating.isPresent() && productToAdd.getRating() >= rating.get()) {
      newProductList.add(productToAdd);
    }
  }
}
