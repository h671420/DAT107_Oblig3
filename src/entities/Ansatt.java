package entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(schema = "Oblig_3")
public class Ansatt {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ans_id")
    private int ansId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "avd_id", referencedColumnName = "avd_id")
    private Avdeling avdeling;
    @Column(name="user_name")
    private String userName;
    private String fornavn;
    private String etternavn;
    @Column(name="ans_dato")
    private LocalDate ansDato;
    private String stilling;
    @Column(name="mnd_lonn")
    int mndLonn;

    public void setAvdeling(Avdeling avdeling){
        this.avdeling=avdeling;
    }
    public boolean erSjef(){
        if (avdeling.getSjef().equals(this))
            return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ansatt ansatt = (Ansatt) o;
        return ansId == ansatt.ansId && Objects.equals(avdeling, ansatt.avdeling);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ansId, avdeling);
    }

    @Override
    public String toString() {
        return "Ansatt{" +
                "ansId=" + ansId +
                ", userName='" + userName + '\'' +
                ", fornavn='" + fornavn + '\'' +
                ", etternavn='" + etternavn + '\'' +
                ", ansDato=" + ansDato +
                ", stilling='" + stilling + '\'' +
                ", mndLonn=" + mndLonn +
                '}';
    }
}