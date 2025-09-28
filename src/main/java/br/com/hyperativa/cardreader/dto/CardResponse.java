package br.com.hyperativa.cardreader.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CardResponse {
    private String cardId;
    private boolean exists;
}
