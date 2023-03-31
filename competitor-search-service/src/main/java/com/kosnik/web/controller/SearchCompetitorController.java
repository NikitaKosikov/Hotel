package com.kosnik.web.controller;

import com.kosnik.domain.Competitor;
import com.kosnik.service.CompetitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competitors")
@PreAuthorize("permitAll()")
public class SearchCompetitorController {
    private final CompetitorService competitorService;

    @GetMapping
    public ResponseEntity<List<Competitor>> findAllCompetitors(){
        return ResponseEntity.ok(competitorService.findAllCompetitors());
    }
}