package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema ="Oblig_3")
public class Avdeling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avd_id")
    private int avdId;
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
    public void setAvdId(int avdId) {
        this.avdId = avdId;
    }
    public int getAvdId() {
        return avdId;
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
    public String getNavn() {
        return navn;
    }
    public List<Ansatt> getAnsatte() {
        return ansatte;
    }
    @Override
    public String toString() {
        return "Avdeling{" +
                "avdId=" + avdId +
                ", ansatte=" + ansatte.size() +
                ", sjef=" + sjef.getFornavn() +" "+sjef.getEtternavn()+
                ", navn='" + navn + '\'' +
                '}';
    }
}
