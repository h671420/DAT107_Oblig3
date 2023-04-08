package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(schema ="Oblig_3")
public class ProsjektDeltakelse implements asd{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosjekt_deltakelse_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "ans_id", referencedColumnName = "ans_id")
    private Ansatt ansatt;
    @ManyToOne
    @JoinColumn(name = "prosj_id",referencedColumnName = "prosj_id")
    private Prosjekt prosjekt;
    private String rolle;
    @Column(name = "ant_timer")
    private int timer;
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int prosjDeltId) {
        this.id = prosjDeltId;
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
                "prosjDeltId=" + id +
                ", ansatt=" + ansatt +
                ", prosjekt=" + prosjekt +
                ", rolle='" + rolle + '\'' +
                ", timer=" + timer +
                '}';
    }
    public String ansattInfo(){
        return "Brukernavn: "+ansatt.getBrukernavn()+", Navn: '"+ansatt.getFornavn()+" "+ansatt.getEtternavn()+", Rolle: "+getRolle()+", Timer: "+getTimer();
    }

    public String prosjektInfo() {
        return "ProsjektId p"+prosjekt.getId()+", Navn"+prosjekt.getNavn()+", Rolle: "+getRolle()+", Timer: "+getTimer();
    }
    public String info(){
        return "Brukernavn: "+ansatt.getBrukernavn()+", Navn: '"+ansatt.getFornavn()+" "+ansatt.getEtternavn()+"', ProsjektId p"+prosjekt.getId()+", Navn"+prosjekt.getNavn()+", Rolle: "+getRolle()+", Timer: "+getTimer();
    }

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
    public String getNavn() {
        return "'" +ansatt.getNavn()+"-"+prosjekt.getNavn()+ "'";
    }

    @Override
    public String getEntId() {
        return "pd"+id;
    }
}
