package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static tekstgrensesnitt.Statics.deler;
import static tekstgrensesnitt.Statics.hoyrePad;

@Entity
@Table(schema ="Oblig_3")
public class Avdeling implements asd{
    //<editor-fold desc="objektvariabler">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avd_id")
    private Integer id;
    @OneToOne
    @JoinColumn(name = "sjef_id", referencedColumnName = "ans_id")
    private Ansatt sjef;
    @OneToMany(mappedBy = "avdeling", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Ansatt> ansatte=new ArrayList<>();
    String navn;

    //</editor-fold>
    //<editor-fold desc="avdeling spesifikke metoder">
    //</editor-fold>
    //<editor-fold desc="interface metoder">
    @Override
    public String getNavn() {
        return navn;
    }

    @Override
    public String getEntId() {
        return "av"+getId();
    }
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String entiteterMenyInfo() {
        return hoyrePad("ID: "+getEntId(),9)
                +hoyrePad("Navn: "+getNavn(),25)
                +hoyrePad("#ansatte: "+getAnsatte().size(),15)
                + hoyrePad("avdelingsleder: "+getSjef().getEntId()+" "+getSjef().getNavn(),0);
    }
    @Override
    public ArrayList<String> administrasjonsMenyInfo(){
        ArrayList<String> allinfo=new ArrayList<>();
        allinfo.add("\tNavn:\t"+getNavn());
        allinfo.add("\tID:\t"+getEntId());
        allinfo.add("\tAvdelingsleder:\t"+getSjef().getNavn());
        allinfo.add("\t#Ansatte:\t"+getAnsatte().size());
        if (!ansatte.isEmpty()){
            allinfo.add("\nAnsatte:");
            allinfo.add(deler());
            for (Ansatt a:ansatte)
                allinfo.add("\t"+a.entiteterMenyInfo());
            allinfo.add(deler());
        }
        return allinfo;
    }
    //</editor-fold>

    //<editor-fold desc = "equals, hash, getters & setters">

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avdeling avdeling = (Avdeling) o;
        return Objects.equals(id, avdeling.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ansatt getSjef() {
        return sjef;
    }

    public void setSjef(Ansatt sjef) {
        this.sjef = sjef;
    }

    public List<Ansatt> getAnsatte() {
        return ansatte;
    }

    public void setAnsatte(List<Ansatt> ansatte) {
        this.ansatte = ansatte;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
    //</editor-fold>
}
