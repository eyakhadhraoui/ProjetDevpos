package tn.esprit.tpfoyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;

import java.util.List;

@Service
public class etudiantService {
    @Autowired
    EtudiantRepository etudiantRepository;
    public Etudiant addEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }
    List<Etudiant> retrieveAllEtudiants(){
        return etudiantRepository.findAll();
    }
    List<Etudiant> addEtudiants (List<Etudiant> etudiants){
        etudiantRepository.saveAll(etudiants);
        return etudiants;
    }
    Etudiant updateEtudiant (Etudiant e){
        return etudiantRepository.save(e);
    }
    Etudiant retrieveEtudiant(long idEtudiant){
       return etudiantRepository.findById(idEtudiant).get();
    }
    void removeEtudiant(long idEtudiant){
        etudiantRepository.deleteById(idEtudiant);
    }


}
