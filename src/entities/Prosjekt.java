package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema ="Oblig_3")
public class Prosjekt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosj_id")
    private int prosjId;
    @Column(name = "prosj_navn")
    private String navn;
    @Column(name = "prosj_beskr")
    private String beskrivelse;
    @OneToMany(mappedBy = "prosjekt",cascade = CascadeType.PERSIST)
    private List<ProsjektDeltakelse> deltakelser=new ArrayList<>();



    public String getBeskrivelse() {
        return beskrivelse;
    }
    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prosjekt prosjekt = (Prosjekt) o;
        return prosjId == prosjekt.prosjId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prosjId, navn, beskrivelse, deltakelser);
    }
}
