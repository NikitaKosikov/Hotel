package com.kosnik.service;

import com.kosnik.service.exception.RoomNotFoundException;
import org.springframework.data.domain.Pageable;
import com.kosnik.service.dto.RoomDTO;

import java.util.List;
import java.util.Map;

public interface RoomService {
    List<RoomDTO> findAll(Map<String, String> params, Pageable pageable);

    RoomDTO find(String id) throws RoomNotFoundException;
    RoomDTO save(RoomDTO roomDTO);

    RoomDTO update(RoomDTO updateRoomDTO, String id) throws RoomNotFoundException;


    void delete(String id);
}
