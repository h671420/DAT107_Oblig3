package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema ="Oblig_3")
public class Prosjekt {
    public int getProsjId() {
        return prosjId;
    }

    public void setProsjId(int prosjId) {
        this.prosjId = prosjId;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosj_id")
    private int prosjId;
    @OneToMany(mappedBy = "prosjekt",cascade = CascadeType.PERSIST)
    private List<ProsjektDeltakelse> deltakelser;
    @Column(name = "prosj_navn")
    private String navn;
    @Column(name = "prosj_beskr")
    private String beskrivelse;

    public Prosjekt(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public Prosjekt(){}

    @Override
    public String toString() {
        return "Prosjekt{" +
                "prosjId=" + prosjId +
                ", navn='" + navn + '\'' +
                ", beskrivelse='" + beskrivelse + '\'' +
                '}';
    }

    public List<ProsjektDeltakelse> getDeltakelser() {
        return deltakelser;
    }

    public void setDeltakelser(List<ProsjektDeltakelse> deltakelser) {
        this.deltakelser = deltakelser;
    }
}
