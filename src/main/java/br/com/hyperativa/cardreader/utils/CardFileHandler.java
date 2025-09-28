package br.com.hyperativa.cardreader.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CardFileHandler {

    public List<String> extractCardNumbers(MultipartFile file) throws IOException {
        List<String> cardNumbers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches("^C\\d+.*") && line.length() > 7) {
                    int endIndex = Math.min(29, line.length());
                    String cardNumber = line.substring(7, endIndex).trim();

                    if (!cardNumber.isEmpty()) {
                        cardNumbers.add(cardNumber);
                    }
                }
            }
        }
        return cardNumbers;
    }
}
