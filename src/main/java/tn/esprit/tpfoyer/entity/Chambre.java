package tn.esprit.tpfoyer.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity

@Getter
@NoArgsConstructor
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChambre;

    private Long numeroChambre;

    @Enumerated(EnumType.STRING)
    private TypeChambre typeC;

    @OneToMany
    private  List<Reservation> reservations;

    @ManyToOne

    Bloc  bloc;

    public void setIdChambre(Long idChambre) {
        this.idChambre = idChambre;
    }

    public void setNumeroChambre(Long numeroChambre) {
        this.numeroChambre = numeroChambre;
    }

    public void setTypeC(TypeChambre typeC) {
        this.typeC = typeC;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setBloc(Bloc bloc) {
        this.bloc = bloc;
    }

    public Long getNumeroChambre() {
        return numeroChambre;
    }

    public Long getIdChambre() {
        return idChambre;
    }

    public TypeChambre getTypeC() {
        return typeC;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public Bloc getBloc() {
        return bloc;
    }

    @Override
    public String toString() {
        return "Chambre{" +
                "idChambre=" + idChambre +
                ", numeroChambre=" + numeroChambre +
                ", typeC=" + typeC +
                ", reservations=" + reservations +
                ", bloc=" + bloc +
                '}';
    }


}

/*
  One To One
              Unidirectionnelle    ->    2 Tables
              Bidiricetionelle     ->    2 Tables

  One To Many
              Unidirectionnelle    ->    3 Tables
              Bidiricetionelle     ->    2 Tables

  Many To One
              Unidirectionnelle    ->    2 Tables
              Bidiricetionelle     ->    2 Tables

  Many to Many
              Unidirectionnelle    ->    3 Tables
              Bidiricetionelle     ->    3 Tables






































 */