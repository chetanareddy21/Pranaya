package com.ecom.pranaya.controller;

import com.ecom.pranaya.model.Cart;
import com.ecom.pranaya.model.SareeProduct;
import com.ecom.pranaya.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private Cart shoppingCart; // Automatically injects the user's specific session cart

    @Autowired
    private ExcelService excelService;

    @org.springframework.beans.factory.annotation.Value("${google.drive.parent.folder.id}")
    private String uploadDir;

    // Handle adding items from product detail submission form
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productName") String productName,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity) {

        // Reconstruct the product entity using your image finder and excel reader logic
        List<String> allImages = new ArrayList<>();
        File productFolder = new File(uploadDir + productName);
        if (productFolder.exists() && productFolder.isDirectory()) {
            File[] files = productFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile() && f.getName().toLowerCase().endsWith(".webp")) allImages.add(f.getName());
                }
            }
        }

        if (!allImages.isEmpty()) {
            SareeProduct product = new SareeProduct();
            product.setFolderName(productName);
            product.setCoverImage(allImages.get(0));
            excelService.populateProductMeta(product);

            // Save to session cart!
            shoppingCart.addItem(product, quantity);
        }

        return "redirect:/cart"; // Direct user over to see their updated dashboard shopping bag
    }

    // View Cart endpoint
    @GetMapping("/cart")
    public String viewCart(Model model) {
        // Only pass the list; JavaScript takes care of the math matrix on page load
        model.addAttribute("cartItems", shoppingCart.getItems());
        return "cart";
    }
}