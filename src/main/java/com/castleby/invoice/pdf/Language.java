package com.castleby.invoice.pdf;

public enum Language {

    UKR("uk"),
    ENG("en");
    
    String value;
    private Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
}
