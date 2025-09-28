package br.com.hyperativa.cardreader.repository;

import br.com.hyperativa.cardreader.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    Optional<Card> findByCardHash(String cardHash);
}
