package com.ecom.pranaya.endpoint;

import com.ecom.pranaya.model.SareeProduct;
import com.ecom.pranaya.service.ExcelService;
import com.ecom.pranaya.soap.GetProductDetailsRequest;
import com.ecom.pranaya.soap.GetProductDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.File;
import java.util.ArrayList;

@Endpoint
public class ProductEndpoint {

    private static final String NAMESPACE_URI = "http://com.ecom.pranaya/soap";

    @Value("${image.upload.dir}")
    private String uploadDir;

    private final ExcelService excelService;

    public ProductEndpoint(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductDetailsRequest")
    @ResponsePayload
    public GetProductDetailsResponse getProductDetails(@RequestPayload GetProductDetailsRequest request) {
        GetProductDetailsResponse response = new GetProductDetailsResponse();

        File folder = new File(uploadDir + request.getName());
        if (folder.exists() && folder.isDirectory()) {

            // Build our standard data object mapping logic
            SareeProduct product = new SareeProduct();
            product.setFolderName(folder.getName());

            // Read metadata properties from your local Excel file
            excelService.populateProductMeta(product);

            // Fetch physical file contents out of local folders
            File[] images = folder.listFiles((dir, fileName) ->
                    fileName.toLowerCase().endsWith(".jpg") ||
                            fileName.toLowerCase().endsWith(".png") ||
                            fileName.toLowerCase().endsWith(".webp")
            );

            // Map variables safely into your explicit SOAP response payload
            response.setFolderName(product.getFolderName());
            response.setProductCode(product.getProductCode());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setStock(product.getStock());
            response.setVendor(product.getVendor());
            response.setProductType(product.getProductType());

            if (images != null && images.length > 0) {
                response.setCoverImage("/images/" + folder.getName() + "/" + images[0].getName());
                for (File img : images) {
                    response.getAllImages().add("/images/" + folder.getName() + "/" + img.getName());
                }
            }
        }
        return response;
    }
}