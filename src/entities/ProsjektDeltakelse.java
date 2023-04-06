package entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(schema ="Oblig_3")
public class ProsjektDeltakelse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosjekt_deltakelse_id")
    private int prosjDeltId;
    @ManyToOne
    @JoinColumn(name = "ans_id", referencedColumnName = "ans_id")
    private Ansatt ansatt;
    @ManyToOne
    @JoinColumn(name = "prosj_id",referencedColumnName = "prosj_id")
    private Prosjekt prosjekt;
    private String rolle;
    @Column(name = "ant_timer")
    private int timer;

    public int getProsjDeltId() {
        return prosjDeltId;
    }

    public void setProsjDeltId(int prosjDeltId) {
        this.prosjDeltId = prosjDeltId;
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

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public ProsjektDeltakelse(){}

    public ProsjektDeltakelse(Ansatt ansatt, Prosjekt prosjekt, String rolle, int timer) {
        this.ansatt = ansatt;
        this.prosjekt = prosjekt;
        this.rolle = rolle;
        this.timer = timer;
    }

    @Override
    public String toString() {
        return "ProsjektDeltakelse{" +
                "prosjDeltId=" + prosjDeltId +
                ", ansatt=" + ansatt +
                ", prosjekt=" + prosjekt +
                ", rolle='" + rolle + '\'' +
                ", timer=" + timer +
                '}';
    }
}
