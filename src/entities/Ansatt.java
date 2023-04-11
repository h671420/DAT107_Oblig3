package entities;

import jakarta.persistence.*;
import tekstgrensesnitt.Statics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static tekstgrensesnitt.Statics.hoyrePad;

@Entity
@Table(schema = "Oblig_3")
public class Ansatt implements asd{
    //<editor-fold desc="objektvariabler">
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ans_id")
    private Integer id;

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
    private Integer mndLonn;
    //</editor-fold>
    //<editor-fold desc="ansatt spesifikke metoder">
    public boolean erSjef(){
        if (avdeling!=null&&avdeling.getSjef().equals(this))
            return true;
        return false;
    }
    public void createUserName() {
        userName= ""+fornavn.toLowerCase().charAt(0)+etternavn.toLowerCase().charAt(0)+ id;
    }
    //</editor-fold>

    //<editor-fold desc="interface metoder">
    @Override
    public String getEntId() {
        if (getId()==null)
            return "--";
        return "an"+getId();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String entiteterMenyInfo() {
        String avdId=avdeling.getEntId();
        if (getAvdeling().getId()==1)
            avdId="";
        String sjef="";
        if (erSjef())
            sjef="Avd.leder: ";
        else sjef="Avdeling:  ";
        return hoyrePad("ID: "+getEntId(),9)
                + hoyrePad("Navn: "+getNavn(),25)
                + hoyrePad(sjef+avdId+" "+getAvdeling().getNavn(),40)
                + hoyrePad("Stilling: "+getStilling(),35)
                + hoyrePad("#prosjekter: "+prosjekter.size(),0);
    }
    @Override
    public ArrayList<String> administrasjonsMenyInfo(){
        ArrayList<String> info=new ArrayList<>();
        String sjef="";
        if (erSjef())
            sjef="/avdelingsleder";
        info.add("\tDato ansatt:\t"+ansDato);
        info.add("\tAvdeling:\t"+avdeling.getNavn());
        info.add("\tStilling:\t"+stilling+sjef);
        info.add("\t"+"Månedslønn:\t" +"kr."+getMndLonn());

        if (prosjekter.size() == 0)
            info.add("\nDen ansatte deltar ikke i noen prosjekt");
        else
            info.add("\nDen ansatte deltar i følgende prosjekter:");
        info.add(Statics.deler());
        for (ProsjektDeltakelse d : prosjekter)
            info.add("\t" + d.kortInfoProsjekt());
        if (!prosjekter.isEmpty())
            info.add(Statics.deler());
        return info;
    }
    @Override
    public String getNavn() {
        return getFornavn()+" "+getEtternavn();
    }
    //</editor-fold>

    //<editor-fold desc ="equals, hash, getters & setters">

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ansatt ansatt = (Ansatt) o;
        return Objects.equals(id, ansatt.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Avdeling getAvdeling() {
        return avdeling;
    }

    public void setAvdeling(Avdeling avdeling) {
        this.avdeling = avdeling;
    }

    public List<ProsjektDeltakelse> getProsjekter() {
        return prosjekter;
    }

    public void setProsjekter(List<ProsjektDeltakelse> prosjekter) {
        this.prosjekter = prosjekter;
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

    public Integer getMndLonn() {
        return mndLonn;
    }

    public void setMndLonn(Integer mndLonn) {
        this.mndLonn = mndLonn;
    }
    //</editor-fold>
}
