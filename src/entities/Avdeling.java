package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema ="Oblig_3")
public class Avdeling  {
    //Deklarasjon av objektvariabler
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avd_id")
    private Integer avdId =null;
    @OneToOne
    @JoinColumn(name = "sjef_id", referencedColumnName = "ans_id")
    private Ansatt sjef=null;
    @OneToMany(mappedBy = "avdeling", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Ansatt> ansatte=new ArrayList<>();
    @Column(name = "navn")
    String avdelingsNavn =null;
//mine metoder
    public void addAnsatt(Ansatt ansatt){
        ansatte.add(ansatt);
    }
//Equals, hash, getters & setters
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avdeling avdeling = (Avdeling) o;
        return avdId == avdeling.avdId;
    }
    @Override
    public int hashCode() {
        return Objects.hash(avdId);
    }
    @Override
    public String toString() {
        return "Avdeling{" +
                "avdId=" + avdId +
                ", ansatte=" + ansatte.size() +
                ", sjef=" + sjef.getFornavn() +" "+sjef.getEtternavn()+
                ", navn='" + avdelingsNavn + '\'' +
                '}';
    }

    public Integer getAvdId() {
        return avdId;
    }

    public void setAvdId(Integer id) {
        this.avdId = id;
    }

    public Ansatt getSjef() {
        return sjef;
    }

    public void setSjef(Ansatt sjef) {
        this.sjef = sjef;
    }

    public <T extends Ansatt> List<T> getAnsatte() {
        return (List<T>) ansatte;
    }

    public void setAnsatte(List<Ansatt> ansatte) {
        this.ansatte = ansatte;
    }

    public String getAvdelingsNavn() {
        return avdelingsNavn;
    }

    public void setAvdelingsNavn(String navn) {
        this.avdelingsNavn = navn;
    }
}
