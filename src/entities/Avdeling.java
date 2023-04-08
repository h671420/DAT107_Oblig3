package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema ="Oblig_3")
public class Avdeling implements asd{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avd_id")
    private int id;
    @OneToOne
    @JoinColumn(name = "sjef_id", referencedColumnName = "ans_id")
    private Ansatt sjef;
    @OneToMany(mappedBy = "avdeling", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Ansatt> ansatte=new ArrayList<>();
    String navn;
    public void setAnsatte(List<Ansatt> ansatte) {
        this.ansatte = ansatte;
    }
    public void setNavn(String navn) {
        this.navn = navn;
    }
    public void setId(int avdId) {
        this.id = avdId;
    }
    @Override
    public Integer getId() {
        return id;
    }
    public Avdeling(String navn) {
        this.navn=navn;
        this.ansatte = new ArrayList<>();
    }
    public Avdeling(){}
    public void addAnsatt(Ansatt ansatt) {
        ansatte.add(ansatt);
    }
    public void setSjef(Ansatt sjef) {
        this.sjef = sjef;
    }
    public Ansatt getSjef() {
        return sjef;
    }
    @Override
    public String getNavn() {
        return navn;
    }

    @Override
    public String getEntId() {
        return "a"+id;
    }

    public List<Ansatt> getAnsatte() {
        return ansatte;
    }
    @Override
    public String toString() {
        return "Avdeling{" +
                "avdId=" + id +
                ", ansatte=" + ansatte.size() +
                ", sjef=" + sjef.getFornavn() +" "+sjef.getEtternavn()+
                ", navn='" + navn + '\'' +
                '}';
    }

    public String info() {
        return "\tId: a"+ getId()+",\t Navn: "+getNavn()+", Sjef: "+getSjef().getFornavn()+" "+getSjef().getEtternavn()+"("+getSjef().getBrukernavn()+")"+", #Ansatte: "+getAnsatte().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avdeling avdeling = (Avdeling) o;
        return id == avdeling.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
