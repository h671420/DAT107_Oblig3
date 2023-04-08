package entities;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema = "Oblig_3")
public class Ansatt implements asd{

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ans_id")
    private int id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "avd_id", referencedColumnName = "avd_id")
    private Avdeling avdeling;
    @OneToMany(mappedBy = "ansatt",cascade = CascadeType.PERSIST)
    private List<ProsjektDeltakelse> prosjekter=new ArrayList<>();
    @Column(name="user_name")
    private String userName;
    private String fornavn;
    private String etternavn;
    @Column(name="ans_dato")
    private LocalDate ansDato;
    private String stilling;
    @Column(name="mnd_lonn")
    Integer mndLønn;


    public boolean erSjef(){
        if (avdeling!=null&&avdeling.getSjef().equals(this))
            return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ansatt ansatt = (Ansatt) o;
        return id == ansatt.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, avdeling);
    }

    @Override
    public String toString() {
        return "Ansatt{" +
                "ansId=" + id +
//                ", avdeling=" + avdeling.getNavn() +
                ", userName='" + userName + '\'' +
                ", fornavn='" + fornavn + '\'' +
                ", etternavn='" + etternavn + '\'' +
                ", ansDato=" + ansDato +
                ", stilling='" + stilling + '\'' +
                ", mndLonn=" + mndLønn +
                '}';
    }


    public Ansatt(String fornavn, String etternavn) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
    }

    public Ansatt() {
    }

    public void createUserName() {
        userName= ""+fornavn.toLowerCase().charAt(0)+etternavn.toLowerCase().charAt(0)+ id;
    }

    public void setId(int ansId) {
        this.id = ansId;
    }

    public Avdeling getAvdeling() {
        return avdeling;
    }

    public void setAvdeling(Avdeling avdeling) {
        this.avdeling = avdeling;
    }

    public String getBrukernavn() {
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

    public Integer getMndLonn() {
        return mndLønn;
    }

    public void setMndLonn(int mndLonn) {
        this.mndLønn = mndLonn;
    }

    public List<ProsjektDeltakelse> getProsjekter() {
        return prosjekter;
    }

    public void setProsjekter(List<ProsjektDeltakelse> prosjekter) {
        this.prosjekter = prosjekter;
    }

    public String info() {
        String erSjef="";
        if (erSjef())
            erSjef="(leder)";
        String avdId ="";
        if (getAvdeling().getId()!=1)
            avdId ="(Id:a"+getAvdeling().getId()+")";
        return "\tId: "+getEntId()+", Navn: "+getNavn()+", Stilling: "+getStilling()+", Avdeling"+erSjef+": "+getAvdeling().getNavn()+avdId+", #Prosjektdeltakelser: "+getProsjekter().size()
                +"\n\t\tMånedslønn: "+ getMndLonn()+", Ansettelsesdato: "+getAnsDato();
    }
    @Override
    public String getNavn(){
        return fornavn+" "+etternavn;
    }

    @Override
    public String getEntId() {
        return "a"+ id;
    }
    @Override
    public Integer getId(){
        return id;
    }
}
