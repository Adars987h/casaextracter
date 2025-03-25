package com.simplifymoney.casaextracter.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    public static Path downloadFile (MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("uploaded", ".pdf");
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }

    public static String extractTextFromPDF(File file, String password) throws IOException {
        try (PDDocument document = PDDocument.load(file, password)) {
            if (document.isEncrypted()) {
                document.setAllSecurityToBeRemoved(true);
            }

            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            throw new IOException("Failed to read PDF. Ensure the password is correct.", e);
        }
    }
}
