package entities;

import jakarta.persistence.*;
import tekstgrensesnitt.Statics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static tekstgrensesnitt.Statics.hoyrePad;

@Entity
@Table(schema ="Oblig_3")
public class Prosjekt implements asd {
    //<editor-fold desc="objektvariabler">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosj_id")
    private Integer id;
    @Column(name = "prosj_navn")
    private String navn;
    @Column(name = "prosj_beskr")
    private String beskrivelse;
    @OneToMany(mappedBy = "prosjekt",cascade = CascadeType.PERSIST)
    private List<ProsjektDeltakelse> deltakelser=new ArrayList<>();
    //</editor-fold>
    //<editor-fold desc="interface metoder">

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public String getNavn() {
        return navn;
    }

    @Override
    public String entiteterMenyInfo() {
        return hoyrePad("ID: "+getEntId(),9)
                +hoyrePad("Navn: "+getNavn(),25)
                +hoyrePad("#deltakere: "+getDeltakelser().size(),30);
    }
    @Override
    public ArrayList<String> administrasjonsMenyInfo(){
        ArrayList<String> allinfo=new ArrayList<>();
        allinfo.add("\tProsjektnavn:\t"+ navn);
        allinfo.add("\tProsjektID:\t"+ getEntId());
        allinfo.add("\tBeskrivelse:\t"+ beskrivelse);
        if (!deltakelser.isEmpty()){
            allinfo.add("\nProsjektdeltakere:");
            allinfo.add(Statics.deler());
            for (ProsjektDeltakelse pd:deltakelser)
                allinfo.add(pd.kortInfoAnsatt());
            allinfo.add(Statics.deler());
        }
        else
            allinfo.add("\nProsjektet har ingen medlemmer\n");

        return allinfo;
    }
    //</editor-fold>
    //<editor-fold desc="equals, hash, getters & setters">
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getEntId() {
        return "pr"+getId();
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public List<ProsjektDeltakelse> getDeltakelser() {
        return deltakelser;
    }

    public void setDeltakelser(List<ProsjektDeltakelse> deltakelser) {
        this.deltakelser = deltakelser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prosjekt prosjekt = (Prosjekt) o;
        return Objects.equals(id, prosjekt.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    //</editor-fold>
}
