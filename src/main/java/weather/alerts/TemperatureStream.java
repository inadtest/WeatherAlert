package weather.alerts;

public interface TemperatureStream {
    void onTemperatureChange(TempRecord r);
}
