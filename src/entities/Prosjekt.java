package entities;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema ="Oblig_3")
public class Prosjekt implements asd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosj_id")
    private int id;
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
                "prosjId=" + id +
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
    @Override
    public Integer getId() {
        return id;
    }
    public void setId(int prosjId) {
        this.id = prosjId;
    }
    @Override
    public String getNavn() {
        return navn;
    }

    @Override
    public String getEntId() {
        return "p"+id;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prosjekt prosjekt = (Prosjekt) o;
        return id == prosjekt.id;
    }
    public String info(){
        return "ProsjekdId p"+ getId()+", Navn: "+getNavn()+", Beskrivelse: "+getBeskrivelse()+", Antall Medlemmer: "+getDeltakelser().size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, navn, beskrivelse, deltakelser);
    }
}
