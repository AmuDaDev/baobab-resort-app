package com.baobab.resort.service;

import com.baobab.resort.exception.BaobabAPIException;
import com.baobab.resort.exception.ResourceNotFoundException;
import com.baobab.resort.model.BookedRoom;
import com.baobab.resort.model.Room;
import com.baobab.resort.payload.BookingResponse;
import com.baobab.resort.payload.RoomResponse;
import com.baobab.resort.repository.BookingRepository;
import com.baobab.resort.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AmuDaDev
 * @created 11/12/2023
 */
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws Exception{
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<RoomResponse> getAllRooms() throws Exception {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photoBytes = getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);
            }
        }

        return roomResponses;
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws Exception {
        Room theRoom = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId.toString()));
        Blob photoBlob = theRoom.getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room theRoom = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId.toString()));
        roomRepository.deleteById(roomId);
    }

    @Override
    public RoomResponse updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile photo) throws Exception {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId.toString()));
        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : getRoomPhotoByRoomId(roomId);
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (photoBytes != null && photoBytes.length > 0) {
            room.setPhoto(new SerialBlob(photoBytes));
        }
        Room updatedRoom = roomRepository.save(room);
        return getRoomResponse(updatedRoom);
    }

    @Override
    public RoomResponse getRoomResponseById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId.toString()));
        return getRoomResponse(room);
    }

    @Override
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId.toString()));
    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) throws Exception {
        List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();

        for (Room room : availableRooms){
            byte[] photoBytes = getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0){
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(photoBase64);
                roomResponses.add(roomResponse);
            }
        }
        return roomResponses;
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new BaobabAPIException(HttpStatus.BAD_REQUEST,"Error retrieving photo");
            }
        }
        return new RoomResponse(room.getId(),
                room.getRoomType(), room.getRoomPrice(),
                room.isBooked(), photoBytes, bookingInfo);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }
}
