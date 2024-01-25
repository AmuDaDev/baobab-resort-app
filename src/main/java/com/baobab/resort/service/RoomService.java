package com.baobab.resort.service;

import com.baobab.resort.model.Room;
import com.baobab.resort.payload.RoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author AmuDaDev
 * @created 11/12/2023
 */
public interface RoomService {
    Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws Exception;
    List<String> getAllRoomTypes();
    List<RoomResponse> getAllRooms() throws Exception;
    byte[] getRoomPhotoByRoomId(Long roomId) throws Exception;
    void deleteRoom(Long roomId);
    RoomResponse updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile photo) throws Exception;
    RoomResponse getRoomResponseById(Long roomId);
    Room getRoomById(Long roomId);
    List<RoomResponse> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) throws Exception;
}
