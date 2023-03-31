package com.kosnik.service.impl;

import com.kosnik.domain.Room;
import com.kosnik.domain.SearchQuery;
import com.kosnik.repository.RoomRepository;
import com.kosnik.service.RoomService;
import com.kosnik.service.converter.SearchQueryConverter;
import com.kosnik.service.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kosnik.service.dto.RoomDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<RoomDTO> findAll(Map<String, String> params, Pageable pageable) {
        SearchQuery searchQuery = SearchQueryConverter.convert(params);
        Page<Room> rooms = roomRepository.findAll(searchQuery, pageable);
        return modelMapper.map(rooms.getContent(), new TypeToken<List<RoomDTO>>(){}.getType());
    }

    @Override
    public RoomDTO find(String id) throws RoomNotFoundException {
        Optional<Room> room = roomRepository.findById(id);
        return room.map(r -> modelMapper.map(r, RoomDTO.class))
                .orElseThrow(() -> new RoomNotFoundException("Room with id=" + id + " isn't exist"));
    }

    @Override
    @Transactional
    public RoomDTO save(RoomDTO roomDTO) {
        Room room = modelMapper.map(roomDTO, Room.class);
        room = roomRepository.save(room);
        return modelMapper.map(room, RoomDTO.class);
    }

    @Override
    @Transactional
    public RoomDTO update(RoomDTO updateRoomDTO, String id) throws RoomNotFoundException {
        RoomDTO roomDTO = find(id);
        Room room = modelMapper.map(roomDTO, Room.class);
        room.setCost(updateRoomDTO.getCost());
        room.setPhoto(updateRoomDTO.getPhoto());
        room.setApartmentType(updateRoomDTO.getApartmentType());
        room.setNumberOfBeds(updateRoomDTO.getNumberOfBeds());
        room = roomRepository.save(room);
        return modelMapper.map(room, RoomDTO.class);
    }

    @Override
    public void delete(String id) {
        roomRepository.deleteById(id);
    }
}
