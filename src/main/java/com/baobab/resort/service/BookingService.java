package com.baobab.resort.service;

import com.baobab.resort.model.BookedRoom;
import com.baobab.resort.payload.BookingResponse;

import java.util.List;

/**
 * @author AmuDaDev
 * @created 12/12/2023
 */
public interface BookingService {
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);
    void cancelBooking(Long bookingId);
    String saveBooking(Long roomId, BookedRoom bookingRequest);
    BookingResponse findByBookingConfirmationCode(String confirmationCode);
    List<BookingResponse> getAllBookings();
    List<BookingResponse> getBookingsByUserEmail(String email);
}
