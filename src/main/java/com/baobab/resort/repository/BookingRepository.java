package com.baobab.resort.repository;

import com.baobab.resort.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author AmuDaDev
 * @created 12/12/2023
 */
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    List<BookedRoom> findByRoomId(Long roomId);
    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);
    List<BookedRoom> findByGuestEmail(String email);
}
