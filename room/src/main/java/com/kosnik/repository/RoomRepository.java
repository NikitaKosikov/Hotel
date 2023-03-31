package com.kosnik.repository;

import com.kosnik.domain.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<Room, String>, DynamicQueryRoomRepository {
}