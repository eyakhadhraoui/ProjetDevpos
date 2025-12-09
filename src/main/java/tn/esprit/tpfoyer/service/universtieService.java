package tn.esprit.tpfoyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.repository.UniversiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class universtieService {
    @Autowired
    FoyerRepository foyerRepository;

    @Autowired
    UniversiteRepository universiteRepository;
     public List<Universite> retrieveAllUniversities(){
        return universiteRepository.findAll();
    }
    public Universite addUniversite (Universite u){
        return universiteRepository.save(u);
    }
    Universite updateUniversite (Universite u){
        return universiteRepository.save(u);
    }
    Universite retrieveUniversite (long idUniversite){
        return  universiteRepository.findById(idUniversite).get();
    }

    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {

        Foyer f = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer introuvable"));

        Universite u = universiteRepository.findByNomUniversite(nomUniversite);
        if (u == null) {
            throw new RuntimeException("Université introuvable");
        }


        if (f.getUniversite() != null && f.getUniversite().getIdUniversite() != u.getIdUniversite()) {
            throw new RuntimeException("Ce foyer est déjà affecté à une autre université");
        }


        if (u.getFoyers() != null && u.getFoyers().getIdFoyer() != f.getIdFoyer()) {
            throw new RuntimeException("Cette université a déjà un foyer");
        }


        u.setFoyers(f);
        f.setUniversite(u);


        foyerRepository.save(f);
        universiteRepository.save(u);

        return u;
    }



    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        Universite u = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Université introuvable"));

        Foyer f = u.getFoyers();
        if (f != null) {
            f.setUniversite(null);
            u.setFoyers(null);
            foyerRepository.save(f);
        }

        universiteRepository.save(u);
        return u;
    }

    public List<Chambre> getChambresParNomUniversite(String nomUniversite) {

        if (nomUniversite == null || nomUniversite.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'université ne peut pas être vide.");
        }


        Universite u = universiteRepository.findByNomUniversite(nomUniversite);
        if (u == null) {
            throw new RuntimeException("Université introuvable : " + nomUniversite);
        }

        if (u.getFoyers() == null) {
            throw new RuntimeException("Cette université n'a pas de foyer associé.");
        }


        Optional<Foyer> f1 = foyerRepository.findById(u.getFoyers().getIdFoyer());
        if (f1.isEmpty()) {
            throw new RuntimeException("Foyer introuvable avec l'ID : " + u.getFoyers().getIdFoyer());
        }

        Foyer foyer = f1.get();


        if (foyer.getBlocs() == null || foyer.getBlocs().isEmpty()) {
            throw new RuntimeException("Aucun bloc trouvé dans ce foyer.");
        }

        List<Bloc> blocs = foyer.getBlocs();
        List<Chambre> chambres = new ArrayList<>();

        for (Bloc bloc : blocs) {
            if (bloc.getChambres() != null) {
                for (Chambre chambre : bloc.getChambres()) {
                    chambres.add(chambre);
                }
            }
        }

        return chambres;
    }



}
