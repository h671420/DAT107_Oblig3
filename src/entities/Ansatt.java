package entities;

import jakarta.persistence.*;
import tekstgrensesnitt.grensesnittentiteter.ProsjektDeltakelseGrensesnitt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema = "Oblig_3",name = "ansatt")
public class Ansatt {
//Deklarasjon av objektvariabler
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ans_id")
    private Integer ansId =null;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "avd_id", referencedColumnName = "avd_id")
    private Avdeling avdeling=null;
    @OneToMany(mappedBy = "ansatt", cascade = CascadeType.PERSIST)
    private List<ProsjektDeltakelse> prosjektDeltakelser = new ArrayList<>();
    @Column(name = "user_name")
    private String brukerNavn =null;
    private String fornavn=null;
    private String etternavn=null;
    @Column(name = "ans_dato")
    private LocalDate ansettelsesDato =null;
    private String stilling=null;
    @Column(name = "mnd_lonn")
    Integer maanedsLonn =null;
//mine metoder
    public String getAnsattNavn(){
        return fornavn+" "+etternavn;
    }
    public void lagBrukerNavn() {
        brukerNavn = "" + fornavn.toLowerCase().charAt(0) + etternavn.toLowerCase().charAt(0) + ansId;
    }
    public boolean erSjef() {
        if (avdeling != null && avdeling.getSjef().equals(this))
            return true;
        return false;
    }
    @Override
//Equals, hash, getters & setters
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ansatt ansatt = (Ansatt) o;
        return ansId == ansatt.ansId;
    }
    @Override
    public int hashCode() {
        return Objects.hash(ansId, avdeling);
    }
    @Override
    public String toString() {
        return "Ansatt{" +
                "ansId=" + ansId +
//                ", avdeling=" + avdeling.getNavn() +
                ", userName='" + brukerNavn + '\'' +
                ", fornavn='" + fornavn + '\'' +
                ", etternavn='" + etternavn + '\'' +
                ", ansDato=" + ansettelsesDato +
                ", stilling='" + stilling + '\'' +
                ", mndLonn=" + maanedsLonn +
                '}';
    }

    public Integer getAnsId() {
        return ansId;
    }

    public void setAnsId(Integer id) {
        this.ansId = id;
    }

    public Avdeling getAvdeling() {
        return avdeling;
    }

    public void setAvdeling(Avdeling avdeling) {
        this.avdeling = avdeling;
    }

    public <T extends ProsjektDeltakelse> List<T> getProsjektDeltakelser() {
        return (List<T>) prosjektDeltakelser;
    }

    public void setProsjektDeltakelser(List<ProsjektDeltakelse> prosjekter) {
        this.prosjektDeltakelser = prosjekter;
    }

    public String getBrukerNavn() {
        return brukerNavn;
    }

    public void setBrukerNavn(String userName) {
        this.brukerNavn = userName;
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

    public LocalDate getAnsettelsesDato() {
        return ansettelsesDato;
    }

    public void setAnsettelsesDato(LocalDate ansDato) {
        this.ansettelsesDato = ansDato;
    }

    public String getStilling() {
        return stilling;
    }

    public void setStilling(String stilling) {
        this.stilling = stilling;
    }

    public Integer getMaanedsLonn() {
        return maanedsLonn;
    }

    public void setMaanedsLonn(Integer mndLonn) {
        this.maanedsLonn = mndLonn;
    }
}
