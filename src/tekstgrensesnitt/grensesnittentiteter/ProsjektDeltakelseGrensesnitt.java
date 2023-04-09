package tekstgrensesnitt.grensesnittentiteter;

import entities.ProsjektDeltakelse;
import jakarta.persistence.*;

import java.util.Objects;


public class ProsjektDeltakelseGrensesnitt extends ProsjektDeltakelse implements asd{

    @Override
    public String getNavn() {
        return getAnsatt().getAnsattNavn()+" "+getProsjekt().getProsjektNavn();
    }

    @Override
    public String getEntId() {
        return getEntId();
    }

    @Override
    public String info() {
        return "info om "+getNavn();
    }
}
