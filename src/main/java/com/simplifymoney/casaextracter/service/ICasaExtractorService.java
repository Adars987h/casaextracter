package com.simplifymoney.casaextracter.service;

import com.simplifymoney.casaextracter.dto.AccountDetailsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

public interface ICasaExtractorService {
    AccountDetailsDTO extractDetailsFromPdf(MultipartFile file, String password);

}
