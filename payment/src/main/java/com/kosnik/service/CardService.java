package com.kosnik.service;

import com.kosnik.service.dto.CardDTO;
import com.kosnik.service.exception.CardNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CardService {
    List<CardDTO> findAll(Map<String, String> params, Pageable pageable);
    CardDTO find(String id) throws CardNotFoundException;
    CardDTO save(CardDTO cardDTO);
}
