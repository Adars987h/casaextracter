package com.simplifymoney.casaextracter.controller;

import com.simplifymoney.casaextracter.dto.AccountDetailsDTO;
import com.simplifymoney.casaextracter.service.ICasaExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@RestController
@RequestMapping("/casa/pdf")
public class CasaExtractorController {

    @Autowired
    private ICasaExtractorService casaExtractorService;

    @PostMapping("/parse")
    public ResponseEntity<Object> parsePDF(@RequestParam("file") MultipartFile file, @RequestParam(value = "password", required = false) String password) {
        AccountDetailsDTO data = casaExtractorService.extractDetailsFromPdf(file, password);
        if (Objects.isNull(data))
            return ResponseEntity.internalServerError().body("Something went wrong");
        return ResponseEntity.ok(data);
    }
}
