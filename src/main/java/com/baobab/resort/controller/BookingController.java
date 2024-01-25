package com.baobab.resort.controller;

import com.baobab.resort.model.BookedRoom;
import com.baobab.resort.payload.BookingResponse;
import com.baobab.resort.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author AmuDaDev
 * @created 15/01/2024
 */
@CrossOrigin("http://localhost:3000") //allowing client application to consume the backed
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest){
        String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
        return ResponseEntity.ok("Room booked successfully, Your booking confirmation code is :"+confirmationCode);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        return ResponseEntity.ok(bookingService.findByBookingConfirmationCode(confirmationCode));
    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingsByUserEmail(email));
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
    }
}
