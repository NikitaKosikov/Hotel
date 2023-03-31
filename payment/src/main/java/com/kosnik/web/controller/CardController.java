package com.kosnik.web.controller;

import com.kosnik.service.CardService;
import lombok.RequiredArgsConstructor;
import com.kosnik.service.dto.CardDTO;
import com.kosnik.service.exception.CardNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardController {
    private final CardService cardService;
    @GetMapping
    public ResponseEntity<List<CardDTO>> findAll(
            @RequestParam(required = false) Map<String
            ,String> params, Pageable pageable){
        List<CardDTO> cards = cardService.findAll(params, pageable);
        return ResponseEntity.ok(cards);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> find(@PathVariable("id") String id) throws CardNotFoundException {
        CardDTO card = cardService.find(id);
        return ResponseEntity.ok(card);
    }
    @PostMapping
    public ResponseEntity<CardDTO> save(@RequestBody CardDTO cardDTO){
        cardDTO = cardService.save(cardDTO);
        return ResponseEntity.ok(cardDTO);
    }
}
