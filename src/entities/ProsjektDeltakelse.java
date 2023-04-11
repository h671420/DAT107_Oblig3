package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;

import static tekstgrensesnitt.Statics.hoyrePad;

@Entity
@Table(schema ="Oblig_3")
public class ProsjektDeltakelse implements asd{
    //<editor-fold desc="objektvariabler">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prosjekt_deltakelse_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "ans_id", referencedColumnName = "ans_id")
    private Ansatt ansatt;
    @ManyToOne
    @JoinColumn(name = "prosj_id",referencedColumnName = "prosj_id")
    private Prosjekt prosjekt;
    private String rolle;
    @Column(name = "ant_timer")
    private Integer timer;

    //</editor-fold>
    //<editor-fold desc="interface metoder">
    @Override
    public String getNavn() {
        return null;
    }

    @Override
    public String getEntId() {
        return null;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String entiteterMenyInfo() {
        return null;
    }
    @Override
    public ArrayList<String> administrasjonsMenyInfo(){
        ArrayList<String> allinfo=new ArrayList<>();
        allinfo.add("Ansatt "+ ansatt.getNavn()+"(ID:"+ansatt.getEntId()+") Deltar i prosjekt "+prosjekt.getNavn()+"(ID:"+prosjekt.getEntId()+")");
        allinfo.add("\tRolle:\t"+rolle);
        allinfo.add("\t#Timer:\t"+timer);
        return allinfo;
    }
    //</editor-fold>
    //<editor-folding desc="prosjektdeltakelse spesifikke metoder">
    public String kortInfoProsjekt() {
        return hoyrePad("ID: "+prosjekt.getEntId(),9)
                +hoyrePad("Navn: "+prosjekt.getNavn(),30)
                +hoyrePad("Rolle: "+rolle,30)
                +hoyrePad("\tTimer: "+timer,0);
    }

    public String kortInfoAnsatt() {
        return hoyrePad("ID: "+ansatt.getEntId(),9)
                +hoyrePad("Navn: "+ansatt.getNavn(),30)
                +hoyrePad("Rolle: "+rolle,30)
                +hoyrePad("\tTimer: "+timer,0);
    }
    //</editor-folding>
    //<editor-fold desc="equals, hash, getters & setters">

    public void setId(Integer id) {
        this.id = id;
    }

    public Ansatt getAnsatt() {
        return ansatt;
    }

    public void setAnsatt(Ansatt ansatt) {
        this.ansatt = ansatt;
    }

    public Prosjekt getProsjekt() {
        return prosjekt;
    }

    public void setProsjekt(Prosjekt prosjekt) {
        this.prosjekt = prosjekt;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProsjektDeltakelse that = (ProsjektDeltakelse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    //</editor-fold>
}
