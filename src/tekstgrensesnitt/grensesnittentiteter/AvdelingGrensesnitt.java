package tekstgrensesnitt.grensesnittentiteter;

import entities.Ansatt;
import entities.Avdeling;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;


public class AvdelingGrensesnitt extends Avdeling implements asd {

    @Override
    public String getNavn() {
        return getAvdelingsNavn();
    }

    @Override
    public String getEntId() {
        return "a" + getAvdId();
    }

    @Override
    public String info() {
        return "info om " + getNavn();
    }

    @Override
    public Integer getId() {
        return getAvdId();
    }

//    @Override
    public <T extends Ansatt> List<T> getAnsatte() {
        List<AnsattGrensesnitt> ansatte=new Vector<>();
        for (Ansatt a:super.getAnsatte())
            ansatte.add((AnsattGrensesnitt) a);
        return (List<T>) ansatte;
    }
}
