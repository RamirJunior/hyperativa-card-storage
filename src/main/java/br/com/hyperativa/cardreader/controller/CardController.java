package br.com.hyperativa.cardreader.controller;

import br.com.hyperativa.cardreader.dto.CardRequest;
import br.com.hyperativa.cardreader.dto.CardResponse;
import br.com.hyperativa.cardreader.model.Card;
import br.com.hyperativa.cardreader.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/hyperativa/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody CardRequest cardRequest) {
        var savedCard = cardService.saveCard(cardRequest.getCardNumber());
        if (savedCard.isEmpty()) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCard.get());
    }

    @PostMapping("/upload")
    public ResponseEntity<List<Card>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        List<Card> savedCardList = cardService.saveCardsFromFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCardList);
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<?> findCard(@PathVariable String cardNumber) {
        var card = cardService.findCardByNumber(cardNumber);
        if (card.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).body(new CardResponse(card.get().getId(), true));
    }
}
