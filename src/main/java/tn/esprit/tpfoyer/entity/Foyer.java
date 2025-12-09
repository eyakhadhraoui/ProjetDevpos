package tn.esprit.tpfoyer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Foyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFoyer;


    private String nomFoyer;
    private Long capaciteFoyer;
@JsonManagedReference
    @OneToOne
    private Universite universite;

    @OneToMany(mappedBy = "foyer")
    private List<Bloc> blocs;

    public Long getIdFoyer() {
        return idFoyer;
    }

    public void setUniversite(Universite universite) {
        this.universite = universite;
    }


    public String getNomFoyer() {
        return nomFoyer;
    }

    public Universite getUniversite() {
        return universite;
    }

    public Long getCapaciteFoyer() {
        return capaciteFoyer;
    }

    public List<Bloc> getBlocs() {
        return blocs;
    }
}
