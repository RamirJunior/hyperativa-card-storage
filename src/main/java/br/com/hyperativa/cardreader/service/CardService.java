package br.com.hyperativa.cardreader.service;

import br.com.hyperativa.cardreader.model.Card;
import br.com.hyperativa.cardreader.repository.CardRepository;
import br.com.hyperativa.cardreader.utils.CardFileHandler;
import br.com.hyperativa.cardreader.utils.CardHashHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardFileHandler cardFileHandler;
    private final CardHashHandler cardHashHandler;

    public CardService(CardRepository cardRepository, CardFileHandler cardFileHandler, CardHashHandler cardHashHandler) {
        this.cardRepository = cardRepository;
        this.cardFileHandler = cardFileHandler;
        this.cardHashHandler = cardHashHandler;
    }

    public Optional<Card> saveCard(String cardNumber) {
        String hash = cardHashHandler.hashCardNumber(cardNumber.trim());
        var foundCard = cardRepository.findByCardHash(hash);
        if (foundCard.isPresent()) return Optional.empty();

        Card card = new Card(null, hash);
        return Optional.of(cardRepository.save(card));
    }

    public List<Card> saveCardsFromFile(MultipartFile file) throws IOException {
        List<String> cardNumbers = cardFileHandler.extractCardNumbers(file);
        List<Card> savedCards = new ArrayList<>();
        List<Card> batch = new ArrayList<>();
        int batchSize = 10;

        for (String cardNumber : cardNumbers) {
            String hash = cardHashHandler.hashCardNumber(cardNumber.trim());
            Card card = new Card(null, hash);
            batch.add(card);

            if (batch.size() >= batchSize) {
                savedCards.addAll(cardRepository.saveAll(batch)); // salva em lote
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            savedCards.addAll(cardRepository.saveAll(batch));
        }

        return savedCards;
    }


    public Optional<Card> findCardByNumber(String cardNumber) {
        String hash = cardHashHandler.hashCardNumber(cardNumber);
        return cardRepository.findByCardHash(hash);
    }
}
