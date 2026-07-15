//package com.ecom.pranaya.controller;
//
//import com.ecom.pranaya.model.SareeProduct;
//import com.ecom.pranaya.service.ExcelService;
//import com.ecom.pranaya.soap.GetProductDetailsRequest;
//import com.ecom.pranaya.soap.GetProductDetailsResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
////SOAP
//@Controller
//public class ProductWebController {
//
//
//    @Value("${image.upload.dir}")
//
//    private String uploadDir;
//
//    private final ExcelService excelService;
//
//    public ProductWebController(ExcelService excelService) {
//        this.excelService = excelService;
//    }
//
//
//    /**
//     * Handles the root URL to display the main product catalog gallery view
//     */
//    @GetMapping("/")
//    public String showGalleryPage(Model model) {
//        File folderRoot = new File(uploadDir);
//        List<SareeProduct> catalogList = new ArrayList<>();
//
//        if (folderRoot.exists() && folderRoot.isDirectory()) {
//            File[] subFolders = folderRoot.listFiles(File::isDirectory);
//
//            if (subFolders != null) {
//                for (File folder : subFolders) {
//                    SareeProduct product = new SareeProduct();
//                    product.setFolderName(folder.getName());
//
//                    // Populate prices/metadata from local excel
//                    excelService.populateProductMeta(product);
//
//                    // Find the first image to use as the display card cover preview
//                    File[] images = folder.listFiles((dir, name) ->
//                            name.toLowerCase().endsWith(".jpg") ||
//                                    name.toLowerCase().endsWith(".png") ||
//                                    name.toLowerCase().endsWith(".webp")
//                    );
//
//                    if (images != null && images.length > 0) {
//                        // Point straight to the local resource mapped prefix path
//                        product.setCoverImage("/images/" + folder.getName() + "/" + images[0].getName());
//                    }
//
//                    catalogList.add(product);
//                }
//            }
//        }
//
//        // Send the product list over to your gallery HTML layout file
//        model.addAttribute("products", catalogList);
//        return "gallery";
//    }
//
//    // A standard browser route that loads the dynamic HTML page
//    @GetMapping("/product")
//    public String viewProductPage(@RequestParam("name") String name, Model model) {
//
//        // 1. Manually build the SOAP request payload object
//        GetProductDetailsRequest soapRequest = new GetProductDetailsRequest();
//        soapRequest.setName(name);
//
//        // 2. In a production environment, you would use Spring's WebServiceTemplate.
//        // For a direct test, we call our endpoint logic or use a SOAP client to fetch the data payload:
//        GetProductDetailsResponse soapResponse = callSoapWebService(soapRequest);
//
//        // 3. Map the XML response fields straight to the Thymeleaf model container
//        model.addAttribute("product", soapResponse);
//
//        // 4. Return your local HTML view name
//        return "product-detail";
//    }
//
//    private GetProductDetailsResponse callSoapWebService(GetProductDetailsRequest request) {
//        // This helper method represents sending the XML out to http://localhost:8080/ws
//        // and returning the unmarshalled GetProductDetailsResponse object.
//        // For local single-app simplification, it hooks directly to your data provider.
//        return new GetProductDetailsResponse();
//    }
//}