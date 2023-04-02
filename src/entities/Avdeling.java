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

    @OneToMany(mappedBy = "avdeling",fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private List<Ansatt> ansatte;

    @OneToOne
    @JoinColumn(name = "sjef_id",referencedColumnName = "ans_id")
    private Ansatt sjef;

    public Avdeling() {
        this.ansatte = new ArrayList<>();
    }

    public void addAnsatt(Ansatt ansatt){
        ansatte.add(ansatt);
    }

    public void setSjef(Ansatt sjef) {
        this.sjef=sjef;
    }
    public Ansatt getSjef(){
        return sjef;
    }

    @Override
    public String toString() {
        return "Avdeling{" +
                "avdId=" + avdId +
                ", ansatte=" + ansatte +
                ", sjef=" + sjef +
                '}';
    }
}
