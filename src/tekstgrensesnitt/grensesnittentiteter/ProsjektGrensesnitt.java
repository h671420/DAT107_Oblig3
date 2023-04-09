package tekstgrensesnitt.grensesnittentiteter;

import entities.Prosjekt;
import entities.ProsjektDeltakelse;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;


public class ProsjektGrensesnitt extends Prosjekt implements asd {

    @Override
    public String getNavn() {
        return getProsjektNavn();
    }

    @Override
    public String getEntId() {
        return "p"+getProsjektId();
    }

    @Override
    public String info() {
        return "info om prosjekt "+getNavn();
    }

    @Override
    public Integer getId() {
        return getProsjektId();
    }

    @Override
    public <T extends ProsjektDeltakelse> List<T> getProsjektDeltakelser() {
        List<ProsjektDeltakelseGrensesnitt> deltakelser = new Vector<>();
        for (ProsjektDeltakelse p:super.getProsjektDeltakelser())
            deltakelser.add((ProsjektDeltakelseGrensesnitt) p);
        return (List<T>) deltakelser;
    }
}
