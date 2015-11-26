package ElTempsDesktop;

/**
 * Created by alumne on 19/11/15.
 */
public class TempUnitari {
    double tempsMin;
    double tempsMax;
    int humidity;
    double pressure;

    public double getTempsMin() {
        return tempsMin;
    }

    public void setTempsMin(double tempsMin) {
        this.tempsMin = tempsMin;
    }

    public double getTempsMax() {
        return tempsMax;
    }

    public void setTempsMax(double tempsMax) {
        this.tempsMax = tempsMax;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
