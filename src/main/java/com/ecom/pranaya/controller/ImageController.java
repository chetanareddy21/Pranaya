package com.ecom.pranaya.controller;

import com.ecom.pranaya.model.SareeProduct;
import com.ecom.pranaya.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageController {


//    @Value("${image.upload.dir}")
//    private String uploadDir;
//
//
//    // for local drive
//    @GetMapping("/")
//    public String displayMainGallery(Model model) {
//        List<SareeProduct> products = new ArrayList<>();
//        File baseFolder = new File(uploadDir);
//
//        if (baseFolder.exists() && baseFolder.isDirectory()) {
//            File[] subFolders = baseFolder.listFiles(File::isDirectory); // Get only folders
//
//            if (subFolders != null) {
//                for (File folder : subFolders) {
//                    File[] files = folder.listFiles();
//                    if (files != null && files.length > 0) {
//                        // Pick the very first image inside that folder as the cover image
//                        String coverImage = files[0].getName();
//
//                        // Using setters to avoid constructor mismatches with your model
//                        SareeProduct product = new SareeProduct();
//                        product.setFolderName(folder.getName());
//                        product.setCoverImage(coverImage);
//                        products.add(product);
//                    }
//                }
//            }
//        }
//        model.addAttribute("products", products);
//        return "gallery";
//    }
//
//    /**
//     * CODE TO DISPLAY PRODUCT DETAIL (LOCAL)
//     */
//
//    @GetMapping("/product")
//    public String showProductDetails(@RequestParam("name") String name, Model model) {
//        File folder = new File(uploadDir + name);
//        if (folder.exists() && folder.isDirectory()) {
//            SareeProduct product = new SareeProduct();
//            product.setFolderName(folder.getName());
//
//            // Read metadata from local excel sheet
//            excelService.populateProductMeta(product);
//
//            // Fetch all alternate images in local folder
//            File[] images = folder.listFiles((dir, fileName) ->
//                    fileName.toLowerCase().endsWith(".jpg") ||
//                            fileName.toLowerCase().endsWith(".png") ||
//                            fileName.toLowerCase().endsWith(".webp")
//            );
//
//            List<String> allImages = new ArrayList<>();
//            if (images != null) {
//                product.setCoverImage("/images/" + folder.getName() + "/" + images[0].getName());
//                for (File img : images) {
//                    allImages.add("/images/" + folder.getName() + "/" + img.getName());
//                }
//            }
//            product.setAllImages(allImages);
//            model.addAttribute("product", product);
//        }
//        return "product-detail";
//    }
//



    @Value("${google.drive.parent.folder.id}")
    private String parentFolderId;

    /*
    Local Drive
     */
    @Autowired
    private ExcelService excelService;


    // --- 1. MAIN GALLERY PAGE ROUTE ---
    @GetMapping("/")
    public String displayMainGallery(Model model) {
        List<SareeProduct> products = new ArrayList<>();
        try {
            String query = "'" + parentFolderId + "' in parents and mimeType = 'application/vnd.google-apps.folder'";
            com.google.api.services.drive.model.FileList result = excelService.getFilesFromDriveFolder(query);
            List<com.google.api.services.drive.model.File> subfolders = result.getFiles();

            if (subfolders != null) {
                for (com.google.api.services.drive.model.File folder : subfolders) {

                    String imgQuery = "'" + folder.getId() + "' in parents and mimeType contains 'image/'";
                    com.google.api.services.drive.model.FileList imagesResult = excelService.getFilesFromDriveFolder(imgQuery);
                    List<com.google.api.services.drive.model.File> images = imagesResult.getFiles();

                    if (images != null && !images.isEmpty()) {
                        SareeProduct product = new SareeProduct();
                        product.setFolderName(folder.getName());

                        // EXACT CHANGE HERE: Get the ID and convert it to a direct rendering URL
                        String fileId = images.get(0).getId();
                        String directEmbedUrl =
                                "https://drive.google.com/thumbnail?id=" + fileId + "&sz=w1200";
                        product.setCoverImage(directEmbedUrl);

                        products.add(product);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching gallery folder structural data from Google Drive:");
            e.printStackTrace();
        }

        model.addAttribute("products", products);
        return "gallery";
    }

    // --- 2. INDIVIDUAL PRODUCT DETAILS PAGE ROUTE ---
    @GetMapping("/product")
    public String showProductDetails(@RequestParam("name") String productName, Model model) {
        try {
            String folderQuery = "'" + parentFolderId + "' in parents and name = '" + productName + "' and mimeType = 'application/vnd.google-apps.folder'";
            com.google.api.services.drive.model.FileList folderResult = excelService.getFilesFromDriveFolder(folderQuery);
            List<com.google.api.services.drive.model.File> matchFolders = folderResult.getFiles();

            if (matchFolders != null && !matchFolders.isEmpty()) {
                String targetFolderId = matchFolders.get(0).getId();

                String imgQuery = "'" + targetFolderId + "' in parents and mimeType contains 'image/'";
                com.google.api.services.drive.model.FileList imagesResult = excelService.getFilesFromDriveFolder(imgQuery);
                List<com.google.api.services.drive.model.File> images = imagesResult.getFiles();

                if (images != null && !images.isEmpty()) {
                    List<String> allImageUrls = new ArrayList<>();

                    // EXACT CHANGE HERE: Loop over files and transform their IDs into embed URLs
                    for (com.google.api.services.drive.model.File imgFile : images) {
                        String fileId = imgFile.getId();
                        String directEmbedUrl =
                                "https://drive.google.com/thumbnail?id=" + fileId + "&sz=w1200";
                        allImageUrls.add(directEmbedUrl);
                    }

                    SareeProduct product = new SareeProduct();
                    product.setFolderName(productName);
                    product.setCoverImage(allImageUrls.get(0));
                    product.setAllImages(allImageUrls);

                    excelService.populateProductMeta(product);

                    model.addAttribute("product", product);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading item details from cloud subfolders:");
            e.printStackTrace();
        }

        return "product-detail";
    }



}



