package com.kosnik.service.impl;

import com.kosnik.domain.SearchQuery;
import com.kosnik.repository.CardRepository;
import com.kosnik.service.CardService;
import com.kosnik.service.converter.SearchQueryConverter;
import lombok.RequiredArgsConstructor;
import com.kosnik.domain.Card;
import com.kosnik.service.dto.CardDTO;
import com.kosnik.service.exception.CardNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<CardDTO> findAll(Map<String, String> params, Pageable pageable) {
        SearchQuery searchQuery = SearchQueryConverter.convert(params);
        Page<Card> cards = cardRepository.findAll(searchQuery,pageable);
        return modelMapper.map(cards.getContent(), new TypeToken<List<CardDTO>>(){}.getType());
    }

    @Override
    public CardDTO find(String id) throws CardNotFoundException {
        Optional<Card> card = cardRepository.findById(id);
        return card.map(c -> modelMapper.map(c, CardDTO.class))
                .orElseThrow(() -> new CardNotFoundException("Card with id=" + id + " doesn't exist"));
    }

    @Override
    @Transactional
    public CardDTO save(CardDTO cardDTO) {
        Card card = modelMapper.map(cardDTO, Card.class);
        card = cardRepository.save(card);
        return modelMapper.map(card, CardDTO.class);
    }
}
