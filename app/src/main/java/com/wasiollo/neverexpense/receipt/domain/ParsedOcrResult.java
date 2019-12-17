package com.wasiollo.neverexpense.receipt.domain;

import java.util.List;

import lombok.Data;

@Data
public class ParsedOcrResult {
    private String companyName;
    private List<String> products;
}
