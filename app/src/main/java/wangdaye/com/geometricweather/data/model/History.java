package wangdaye.com.geometricweather.data.model;

/**
 * History.
 * */

public class History {
    // data
    public String location;
    public String weather;
    public String maxiTemp;
    public String miniTemp;
    public String time;

    public History(String location, String weather, String maxiTemp, String miniTemp, String time) {
        this.location = location;
        this.weather = weather;
        this.maxiTemp = maxiTemp;
        this.miniTemp = miniTemp;
        this.time = time;
    }
}
