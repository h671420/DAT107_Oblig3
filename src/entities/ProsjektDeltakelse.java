package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(schema ="Oblig_3")
public class ProsjektDeltakelse {
//Deklarasjon av objektvariabler
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosjekt_deltakelse_id")
    private Integer id=null;
    @ManyToOne
    @JoinColumn(name = "ans_id", referencedColumnName = "ans_id")
    private Ansatt ansatt=null;
    @ManyToOne
    @JoinColumn(name = "prosj_id",referencedColumnName = "prosj_id")
    private Prosjekt prosjekt=null;
    private String rolle=null;
    @Column(name = "ant_timer")
    private Integer timer=null;
//mine metoder
//Equals, hash, getters & setters
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProsjektDeltakelse that = (ProsjektDeltakelse) o;
        return id == that.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProsjektDeltakelse{" +
                "prosjDeltId=" + id +
                ", ansatt=" + ansatt +
                ", prosjekt=" + prosjekt +
                ", rolle='" + rolle + '\'' +
                ", timer=" + timer +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ansatt getAnsatt() {
        return ansatt;
    }

    public void setAnsatt(Ansatt ansatt) {
        this.ansatt = ansatt;
    }

    public Prosjekt getProsjekt() {
        return prosjekt;
    }

    public void setProsjekt(Prosjekt prosjekt) {
        this.prosjekt = prosjekt;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }
}
