package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema = "Oblig_3")
public class Prosjekt {
//Deklarasjon av objektvariabler
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosj_id")
    private Integer prosjektId =null;
    @Column(name = "prosj_navn")
    private String prosjektNavn =null;
    @Column(name = "prosj_beskr")
    private String beskrivelse=null;
    @OneToMany(mappedBy = "prosjekt", cascade = CascadeType.PERSIST)
    private List<ProsjektDeltakelse> prosjektDeltakelser = new ArrayList<>();
//mine metoder

//Equals, hash, getters & setters
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prosjekt prosjekt = (Prosjekt) o;
        return prosjektId == prosjekt.prosjektId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prosjektId, prosjektNavn, beskrivelse, prosjektDeltakelser);
    }

    @Override
    public String toString() {
        return "Prosjekt{" +
                "prosjId=" + prosjektId +
                ", navn='" + prosjektNavn + '\'' +
                ", beskrivelse='" + beskrivelse + '\'' +
                '}';
    }

    public Integer getProsjektId() {
        return prosjektId;
    }

    public void setProsjektId(Integer id) {
        this.prosjektId = id;
    }

    public String getProsjektNavn() {
        return prosjektNavn;
    }

    public void setProsjektNavn(String navn) {
        this.prosjektNavn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public <T extends ProsjektDeltakelse> List<T> getProsjektDeltakelser() {
        return (List<T>) prosjektDeltakelser;
    }

    public void setProsjektDeltakelser(List<ProsjektDeltakelse> deltakelser) {
        this.prosjektDeltakelser = deltakelser;
    }
}
