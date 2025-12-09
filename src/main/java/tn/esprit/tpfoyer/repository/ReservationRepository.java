package tn.esprit.tpfoyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    @Query("SELECT DISTINCT c FROM Chambre c JOIN c.reservations r "
            + "WHERE r.estValide = true "
            + "AND FUNCTION('YEAR', r.anneeUniversitaire) = :year")
    List<Chambre> findChambresWithValidReservationsForYear(@Param("year") int year);


}