package tn.esprit.tpfoyer.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class chambreService {
@Autowired
 ChambreRepository chambreRepository;
@Autowired
BlocRepository blocRepository;
@Autowired
ReservationRepository reservationRepository;
    @Autowired
    universtieService universtieService;
    public List<Chambre> retrieveAllChambres() {
        return chambreRepository.findAll();
    }

    public Chambre retrieveChambre(Long id) {
        return chambreRepository.findById(id).orElse(null);
    }

    public Chambre addChambre(Chambre c) {
        return chambreRepository.save(c);
    }
    public void removeChambre(Long id) {
        chambreRepository.deleteById(id);
    }

    public Chambre modifyChambre(Chambre chambre) {
        return chambreRepository.save(chambre);
    }

// méthode 1
    public List<Chambre> getChambresParBlocEtType (long idBloc, TypeChambre typeC) {
        Bloc b = blocRepository.findById(idBloc).orElse(null);
        List<Chambre> chambres = b.getChambres().stream().filter(r -> r.getTypeC() == typeC).toList();
        return chambres;
    }
//méthode 2 avec query
public List<Chambre> getChambresParBlocEtTypeJPQL(long idBloc, TypeChambre typeC) {
    return chambreRepository.getChambresParBlocEtTypeJPQL(idBloc, typeC);
}

//  service avancé 22
    public List<Chambre>  getChambresNonReserveParNomUniversiteEtTypeChambre( String nomUniversite,TypeChambre type) {
        List<Chambre> chambres =  universtieService.getChambresParNomUniversite(nomUniversite).stream().filter(r -> r.getTypeC() == type).collect(Collectors.toList());
        List<Chambre> chambresretour = new ArrayList<>();

        for (Chambre c : chambres) {
            boolean test = true;
            for (Reservation r : c.getReservations()) {
                 if(r.isEstValide()){
                     test = false;
                     break;
                 }
            }
            if(test){
                chambresretour.add(c);
            }

        }
        return chambresretour;
    }

    // CRON (Seconde Minute Heure Jour Mois Jour en Lettre)
    //Dernier Service
    @Scheduled(cron = "0 30 10 * * *")
    public List<Chambre> getChambrenonReservePourCetteAnne()
    {
        int currentYear = LocalDate.now().getYear();
        List<Chambre> chambres = reservationRepository.findChambresWithValidReservationsForYear(currentYear);

        return chambres;

        }



    }




