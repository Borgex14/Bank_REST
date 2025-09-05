package com.example.bankcards.util;

import java.util.Random;

public class CardNumberGenerator {

    private static final Random random = new Random();

    public static String generate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) sb.append("-");
            sb.append(String.format("%04d", random.nextInt(10000)));
        }
        return sb.toString();
    }

    public static boolean isValidLuhn(String cardNumber) {
        String digits = cardNumber.replaceAll("\\D", "");
        int sum = 0;
        boolean alternate = false;

        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(digits.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) n = (n % 10) + 1;
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}