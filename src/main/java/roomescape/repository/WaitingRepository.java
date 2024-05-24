package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Waiting;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findByMemberEmail(String email);

    @Query("""
            SELECT w from Waiting w
            JOIN Reservation r ON r.id = w.reservation.id
            JOIN ReservationTime rt ON rt.id = w.reservation.time.id
            JOIN Theme t ON t.id = w.reservation.theme.id
            WHERE t.id = :themeId AND r.date = :startDate AND rt.startAt = :startTime
            """)
    List<Waiting> findByThemeIdAndStartAt(Long themeId, LocalDate startDate, LocalTime startTime);

    // TODO: 이걸 붙이면 왜 되고 안붙이면 왜 안될까
    @Transactional
    void deleteByReservationIdAndMemberEmail(Long reservationId, String email);
}
