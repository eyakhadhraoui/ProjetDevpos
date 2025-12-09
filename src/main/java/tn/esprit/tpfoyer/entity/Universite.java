package tn.esprit.tpfoyer.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Universite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUniversite;

    private String nomUniversite;
    private String adresse;

    // Une université possède plusieurs foyers
@JsonBackReference
    @OneToOne(mappedBy = "universite")
    private Foyer foyers;

    public void setFoyers(Foyer foyers) {
        this.foyers = foyers;
    }

    public Long getIdUniversite() {
        return idUniversite;
    }

    public String getNomUniversite() {
        return nomUniversite;
    }

    public String getAdresse() {
        return adresse;
    }

    public Foyer getFoyers() {
        return foyers;
    }

    @Override
    public String toString() {
        return "Universite{" +
                "idUniversite=" + idUniversite +
                ", nomUniversite='" + nomUniversite + '\'' +
                ", adresse='" + adresse + '\'' +
                ", foyers=" + foyers +
                '}';
    }
}
