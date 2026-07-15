package com.ecom.pranaya.model;

import java.util.List;

public class SareeProduct {
    private String folderName;
    private String coverImage;
    private List<String> allImages;

    // Excel Meta Fields
    private String productCode;
    private String description;
    private double price;
    private int stock;
    private String vendor;
    private String productType;

    // Constructors
    public SareeProduct() {}

    public SareeProduct(String folderName, String coverImage) {
        this.folderName = folderName;
        this.coverImage = coverImage;
    }

    // Getters and Setters
    public String getFolderName() { return folderName; }
    public void setFolderName(String folderName) { this.folderName = folderName; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public List<String> getAllImages() { return allImages; }
    public void setAllImages(List<String> allImages) { this.allImages = allImages; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
}