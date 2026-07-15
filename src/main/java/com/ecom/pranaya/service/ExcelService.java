package com.ecom.pranaya.service;

import com.ecom.pranaya.model.SareeProduct;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;

@Service
public class ExcelService {

    // Injecting the specific File ID for your products.xlsx sheet on Google Drive
    @Value("${google.drive.file.id}")
    private String excelFileId;

    private static final String APPLICATION_NAME = "Pranaya Silks E-Commerce";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // =========================================================================
    // 1. OLD LOCAL DRIVE LOGIC
    // =========================================================================

//    @Value("${image.upload.dir}")
//    private String uploadDir;
//
//    public void populateProductMetaLocal(SareeProduct product) {
//        String excelPath = uploadDir + "products.xlsx";
//        java.io.File file = new java.io.File(excelPath);
//
//        if (!file.exists()) {
//            System.out.println("Excel file not found at: " + excelPath);
//            return;
//        }
//
//        try (java.io.FileInputStream fis = new java.io.FileInputStream(file);
//             Workbook workbook = WorkbookFactory.create(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) continue;
//
//                Cell folderCell = row.getCell(0);
//                if (folderCell != null && folderCell.getStringCellValue().trim().equalsIgnoreCase(product.getFolderName().trim())) {
//                    if (row.getCell(1) != null) product.setProductCode(getCellValueAsString(row.getCell(1)));
//                    if (row.getCell(2) != null) product.setDescription(row.getCell(2).getStringCellValue());
//                    if (row.getCell(3) != null) product.setPrice(row.getCell(3).getNumericCellValue());
//                    if (row.getCell(4) != null) product.setStock((int) row.getCell(4).getNumericCellValue());
//                    if (row.getCell(5) != null) product.setVendor(row.getCell(5).getStringCellValue());
//                    if (row.getCell(6) != null) product.setProductType(row.getCell(6).getStringCellValue());
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Safe string parsing cell wrapper matching your original requirements
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.format("%.0f", cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }

    // =========================================================================
    // 2. GOOGLE DRIVE METHODS+6
    // =========================================================================

    /**
     * Authorized Google Drive connection client setup
     */
    public Drive getDriveService() throws Exception {
        InputStream in = ExcelService.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new java.io.FileNotFoundException("Resource /credentials.json not found in classpath.");
        }
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE_READONLY));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Retrieve directory structural details / child files from Google Drive
     */
    public FileList getFilesFromDriveFolder(String query) throws Exception {
        Drive driveService = getDriveService();
        return driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setOrderBy("name") // <-- THIS ENSURES s1.webp IS ALWAYS FIRST IN THE LIST
                .setFields("nextPageToken, files(id, name, mimeType)")
                .execute();
    }

    /**
     * Streams Excel workbook data straight from Google Drive and populates SareeProduct models
     */
    public void populateProductMeta(SareeProduct product) {
        try {
            Drive driveService = getDriveService();

            // Download the spreadsheet from Drive straight into memory streams
            try (InputStream excelStream = driveService.files().get(excelFileId).executeMediaAsInputStream();
                 Workbook workbook = new XSSFWorkbook(excelStream)) {

                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row == null || row.getRowNum() == 0) continue;

                    Cell folderCell = row.getCell(0);
                    if (folderCell != null && getCellValueAsString(folderCell).trim().equalsIgnoreCase(product.getFolderName().trim())) {

                        // Maps your columns using your exact business data structure
                        if (row.getCell(1) != null) product.setProductCode(getCellValueAsString(row.getCell(1)));
                        if (row.getCell(2) != null) product.setDescription(getCellValueAsString(row.getCell(2)));
                        if (row.getCell(3) != null) product.setPrice(row.getCell(3).getNumericCellValue());
                        if (row.getCell(4) != null) product.setStock((int) row.getCell(4).getNumericCellValue());
                        if (row.getCell(5) != null) product.setVendor(getCellValueAsString(row.getCell(5)));
                        if (row.getCell(6) != null) product.setProductType(getCellValueAsString(row.getCell(6)));

                        break; // Match confirmed, break out early
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading Excel file from Google Drive:");
            e.printStackTrace();
        }
    }




}//class close