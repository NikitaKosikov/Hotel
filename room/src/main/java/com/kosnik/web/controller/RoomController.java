package com.kosnik.web.controller;

import com.kosnik.service.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.kosnik.service.RoomService;
import com.kosnik.service.dto.RoomDTO;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomService roomService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String findAll(Map<String, String> params, Pageable pageable, Model model){
        List<RoomDTO> rooms = roomService.findAll(params, pageable);
        model.addAllAttributes(rooms);
        return "rooms";
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String find(@PathVariable("id") String id, Model model) throws RoomNotFoundException {
        RoomDTO room = roomService.find(id);
        model.addAttribute(room);
        return "rooms";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String save(@RequestBody RoomDTO room, Model model){
        room = roomService.save(room);
        model.addAttribute(room);
        return "rooms";
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String update(@PathVariable("id") String id, @RequestBody RoomDTO room, Model model) throws RoomNotFoundException {
        room = roomService.update(room, id);
        model.addAttribute(room);
        return "rooms";
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete(@PathVariable("id") String id){
        roomService.delete(id);
        return "rooms";
    }
}
