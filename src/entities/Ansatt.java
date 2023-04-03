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
                ", avdeling=" + avdeling.getNavn() +
                ", userName='" + userName + '\'' +
                ", fornavn='" + fornavn + '\'' +
                ", etternavn='" + etternavn + '\'' +
                ", ansDato=" + ansDato +
                ", stilling='" + stilling + '\'' +
                ", mndLonn=" + mndLonn +
                '}';
    }


    public Ansatt(String fornavn, String etternavn) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
    }

    public Ansatt() {
    }

    public void createUserName() {
        userName= ""+fornavn.toLowerCase().charAt(0)+etternavn.toLowerCase().charAt(0)+ansId;
    }

    public int getAnsId() {
        return ansId;
    }

    public void setAnsId(int ansId) {
        this.ansId = ansId;
    }

    public Avdeling getAvdeling() {
        return avdeling;
    }

    public void setAvdeling(Avdeling avdeling) {
        this.avdeling = avdeling;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public LocalDate getAnsDato() {
        return ansDato;
    }

    public void setAnsDato(LocalDate ansDato) {
        this.ansDato = ansDato;
    }

    public String getStilling() {
        return stilling;
    }

    public void setStilling(String stilling) {
        this.stilling = stilling;
    }

    public int getMndLonn() {
        return mndLonn;
    }

    public void setMndLonn(int mndLonn) {
        this.mndLonn = mndLonn;
    }
}
