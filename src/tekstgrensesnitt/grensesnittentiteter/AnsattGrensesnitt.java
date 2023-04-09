package tekstgrensesnitt.grensesnittentiteter;

import entities.Ansatt;
import entities.ProsjektDeltakelse;
import java.util.List;
import java.util.Vector;


public class AnsattGrensesnitt extends Ansatt implements asd {


    @Override
    public String getNavn() {
        return getFornavn()+" "+getEtternavn();
    }

    @Override
    public String getEntId() {
        return "a"+getAnsId();
    }

    @Override
    public String info() {
        return "ansattinfo om "+getNavn();
    }

    @Override
    public Integer getId() {
        return getAnsId();
    }

    @Override
    public <T extends ProsjektDeltakelse> List<T> getProsjektDeltakelser() {
        List<ProsjektDeltakelseGrensesnitt> deltakelser=new Vector<>();
        for (ProsjektDeltakelse pd:super.getProsjektDeltakelser())
            deltakelser.add((ProsjektDeltakelseGrensesnitt) pd);
        return (List<T>) deltakelser;
    }
}
