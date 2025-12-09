package tn.esprit.tpfoyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class reservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BlocRepository blocRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private ChambreRepository chambreRepository;
    @Autowired
    universtieService universtieService;


    List<Reservation> retrieveAllReservation(){
        return reservationRepository.findAll();
    }
    Reservation updateReservation (Reservation  res){
        return reservationRepository.save(res);
    }
    Reservation retrieveReservation (String idReservation){
        return reservationRepository.findById(idReservation).get();
    }
    public Reservation ajouterReservation (long idBloc, long cinEtudiant) {
                        Bloc bloc = blocRepository.findById(idBloc)
                                .orElseThrow(() -> new RuntimeException("Bloc introuvable"));
                        Etudiant etudiant = etudiantRepository.findByCin(cinEtudiant);
                        List<Chambre> chambres = bloc.getChambres();
                        if (chambres == null || chambres.isEmpty()) {
                            throw new RuntimeException("Aucune chambre dans ce bloc");
                        }
                        Chambre chambreDisponible = null;
                        for (Chambre chambre : chambres) {

                            int nbEtudiants = chambre.getReservations().stream()
                                    .mapToInt(r -> r.getEtudiants().size())
                                    .sum();

                            int capaciteMax = switch (chambre.getTypeC()) {
                                case SIMPLE -> 1;
                                case DOUBLE -> 2;
                                case TRIPLE -> 3;
                            };

                            if (nbEtudiants < capaciteMax) {
                                chambreDisponible = chambre;
                                break;
                            }
                        }

                        if (chambreDisponible == null) {
                            throw new RuntimeException("Aucune chambre disponible dans ce bloc");
                        }


                        Reservation reservation = new Reservation();
                        Date aujourdhui = new Date();
                        String idReservation = chambreDisponible.getNumeroChambre()
                                + "-" + bloc.getNomBloc()
                                + "-" + (1900 + aujourdhui.getYear());
                        reservation.setIdReservation(idReservation);
                        reservation.setAnneeUniversitaire(aujourdhui);
                        reservation.setEstValide(true);
                        reservation.setEtudiants(List.of(etudiant));
                        reservationRepository.save(reservation);
                        chambreDisponible.getReservations().add(reservation);
                        chambreRepository.save(chambreDisponible);
                        etudiant.getReservations().add(reservation);
                        etudiantRepository.save(etudiant);

                        return reservation;
    }

    public Reservation annulerReservation(long cinEtudiant) {

        // 1. Récupérer étudiant
        Etudiant etudiant = etudiantRepository.findByCin(cinEtudiant);

        // 2. Trouver une réservation active (estValide = true)
        Reservation reservationActive = etudiant.getReservations()
                .stream()
                .filter(r -> r.isEstValide())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucune réservation active pour cet étudiant"));

        // 3. Désactiver la réservation
        reservationActive.setEstValide(false);

        // 4. Désaffecter l’étudiant de la réservation
        reservationActive.getEtudiants().remove(etudiant);

        // 5. Désaffecter la réservation de l’étudiant
        etudiant.getReservations().remove(reservationActive);

        // 6. Récupérer la chambre associée à la réservation
        Chambre chambre = chambreRepository.findChambreByReservations(reservationActive);

        if (chambre != null) {
            chambre.getReservations().remove(reservationActive);
            chambreRepository.save(chambre);
        }


        etudiantRepository.save(etudiant);
        reservationRepository.save(reservationActive);

        return reservationActive;
    }

    public List<Reservation>  getReservationParAnneeUniversitaireEtNomUniversite( Date anneeUniversite, String nomUniversite) {
    List<Chambre> chambres =  universtieService.getChambresParNomUniversite(nomUniversite);
    List<Reservation> reservations = new ArrayList<>();
    for(Chambre chambre : chambres) {
          for (Reservation reservation : chambre.getReservations()) {
              if( reservation.getAnneeUniversitaire().getYear() == anneeUniversite.getYear() ) {
                  reservations.add(reservation);
              }
          }
    }
    return reservations;


    }


}
