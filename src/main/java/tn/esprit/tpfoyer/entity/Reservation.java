package tn.esprit.tpfoyer.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Reservation {
    @Id
    private String idReservation;

    @Temporal(TemporalType.DATE)
    private Date anneeUniversitaire;

    private boolean estValide;

    @ManyToMany(mappedBy = "reservations")
    private List<Etudiant> etudiants;








    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setAnneeUniversitaire(Date anneeUniversitaire) {
        this.anneeUniversitaire = anneeUniversitaire;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public void setEstValide(boolean estValide) {
        this.estValide = estValide;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public Date getAnneeUniversitaire() {
        return anneeUniversitaire;
    }

    public boolean isEstValide() {
        return estValide;
    }

}
