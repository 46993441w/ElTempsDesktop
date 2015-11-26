package ElTempsDesktop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alumne on 19/11/15.
 */
public class Temps {
    private List<TempUnitari> listaTemps = new ArrayList<>();

    public void add(TempUnitari temps){
        listaTemps.add(temps);
    }

    public double mitjaTempsMin(){
        double min = 0;
        for(int i = 0; i < listaTemps.size(); i++){
            min += listaTemps.get(i).getTempsMin();
        }
        return min/listaTemps.size();
    }

    public double mitjaTempsMax(){
        double max = 0;
        for(int i = 0; i < listaTemps.size(); i++){
            max += listaTemps.get(i).getTempsMax();
        }
        return max/listaTemps.size();
    }

    public double mitjaHumidity(){
        double humitat = 0;
        for(int i = 0; i < listaTemps.size(); i++){
            humitat += listaTemps.get(i).getHumidity();
        }
        return humitat/listaTemps.size();
    }

    public double mitjaPressure(){
        double presio = 0;
        for(int i = 0; i < listaTemps.size(); i++){
            presio += listaTemps.get(i).getPressure();
        }
        return presio/listaTemps.size();
    }
}
