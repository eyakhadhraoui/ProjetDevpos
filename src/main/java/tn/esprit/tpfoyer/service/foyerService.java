package tn.esprit.tpfoyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.repository.UniversiteRepository;

import java.util.List;

@Service
public class foyerService {
@Autowired
FoyerRepository foyerRepository;
@Autowired
UniversiteRepository universiteRepository;
    public List<Foyer> retrieveAllFoyers(){
        return foyerRepository.findAll();
    }
    Foyer addFoyer (Foyer f){
        foyerRepository.save(f);
        return f;
    }
    Foyer updateFoyer (Foyer f){
     foyerRepository.save(f);
     return f;
    }
    Foyer retrieveFoyer (long  idFoyer){
     Foyer x =   foyerRepository.findById(idFoyer).get();
     return x;
    }

    void removeFoyer (long idFoyer){
        foyerRepository.deleteById(idFoyer);
    }

    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {

        Universite u = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Université introuvable avec l'id : " + idUniversite));


        if (u.getFoyers() != null) {
            throw new RuntimeException("Cette université a déjà un foyer affecté.");
        }


        if (foyer.getIdFoyer() != null && foyerRepository.existsById(foyer.getIdFoyer())) {
            throw new RuntimeException("Le foyer existe déjà en base avec l'id : " + foyer.getIdFoyer());
        }


        foyer.setUniversite(u);
        u.setFoyers(foyer);

        foyerRepository.save(foyer);
        universiteRepository.save(u);

        return foyer;
    }




}
