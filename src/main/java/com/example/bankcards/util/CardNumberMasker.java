package com.example.bankcards.util;

import org.springframework.stereotype.Component;

@Component
public class CardNumberMasker {

    private static final String MASK_PATTERN = "**** **** **** ";
    private static final int VISIBLE_DIGITS = 4;

    public static String mask(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < VISIBLE_DIGITS) {
            return cardNumber;
        }

        String cleanedNumber = cardNumber.replaceAll("\\s+", "");
        if (cleanedNumber.length() < VISIBLE_DIGITS) {
            return cleanedNumber;
        }

        String lastDigits = cleanedNumber.substring(cleanedNumber.length() - VISIBLE_DIGITS);
        return MASK_PATTERN + lastDigits;
    }
}