package com.baobab.resort.service;

import com.baobab.resort.model.BookedRoom;

import java.util.List;

/**
 * @author AmuDaDev
 * @created 12/12/2023
 */
public interface BookingService {
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);
}
