package tn.esprit.tpfoyer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entity.*;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class mainController {
    @Autowired
  chambreService chService;
    @Autowired
    blocService blService;
    @Autowired
    BlocRepository blocRepository;
    @Autowired
    etudiantService etService;
    @Autowired
    universtieService univService;
    @Autowired
    foyerService foService;


    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from MainController!";
    }
    @PostMapping("/add-chambre")
    public Chambre addChambre(@RequestBody Chambre c) {
        Bloc bloc = blocRepository.findById(c.getIdChambre())
                .orElseThrow(() -> new RuntimeException("Bloc introuvable"));
        c.setBloc(bloc);
        Chambre chambre = chService.addChambre(c);

        return chambre;
    }
    @GetMapping("/allChambres")
    public List<Chambre> getAllChambres() {
        return chService.retrieveAllChambres();
    }
    @DeleteMapping("/deleteChambre/{id}")
    public Chambre deleteChambre(@PathVariable Long id) {
        chService.removeChambre(id);
        return chService.retrieveChambre(id);
    }
    @PostMapping("/add-bloc")
    public Bloc addBloc(@RequestBody Bloc b) {
      Bloc  bl =  blService.addBloc(b);
        return bl;
    }

    @PostMapping("/add-etudiant")
    public Etudiant addEtudiant(@RequestBody Etudiant et) {
        return etService.addEtudiant(et);
    }
@PostMapping("/add-universite")
    public Universite addUniversity(@RequestBody Universite u) {
        return univService.addUniversite(u);
}

 @GetMapping("/getuniversities")
    public List<Universite> getAllUniversities() {
        return univService.retrieveAllUniversities();
    }
    @GetMapping("/getfoyers")
    public List<Foyer> getAllfoyers() {
        return foService.retrieveAllFoyers();
    }
    @GetMapping("/affectfoyeruniversite")
    public Universite affectationfoyerUniversite() {
        return univService.affecterFoyerAUniversite(3,"Esprit");
    }

    @GetMapping("/desaffectfoyeruniversite")
    public Universite desaffectationfoyerUniversite() {
        return univService.desaffecterFoyerAUniversite(1);
    }
    @GetMapping("/scheduler")
    public List<Chambre> getchambresnonReserve(){
        return chService.getChambrenonReservePourCetteAnne();
    }


}
// CRON (Seconde Minute Heure Jour Mois Jour en Lettre)
