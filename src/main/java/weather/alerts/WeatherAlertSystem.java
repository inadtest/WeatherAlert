package weather.alerts;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherAlertSystem implements TemperatureStream, RainfallStream {
    private List<WeatherObserver> observers;
    private List<TempRecord> recentTemps;
    private List<RainfallRecord> recentRainfall;
    private double currentRainfall;
    private double currentTemperature;
    private WeatherCondition lastCondition = null;

    WeatherAlertSystem() {
        observers = new ArrayList<>();
        recentTemps = new ArrayList<>();
        recentRainfall = new ArrayList<>();
    }
    public void subscribe(WeatherObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(WeatherObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void onRainfallChange(RainfallRecord record) {
        recentRainfall.add(record);
        // remove records older than 1 hour
        LocalDateTime oldestAllowed = LocalDateTime.now().minusHours(1);
        recentRainfall.removeIf(r -> r.getDt().isBefore(oldestAllowed));

        double rainfall = recentRainfall.stream().mapToDouble(RainfallRecord::getRainfall).sum();
        // adjust for evaporation
        double oldestTimeInHours = Duration.between(oldestAllowed, record.getDt()).toMinutes() / 60.0;
        double adjustedRainfall = rainfall - (0.1 * oldestTimeInHours);
        currentRainfall = adjustedRainfall;
        if (currentRainfall > 2) {
            String message = "Rainfall alert: expected rainfall over the next hour is " + adjustedRainfall + " inches.";
            notifySubscribers(message);
        }

        // Check current weather conditions and send alert if conditions have changed
        WeatherCondition currentCondition = getCurrentWeatherCondition();
        if (!currentCondition.equals(lastCondition)) {
            String message = "Current weather condition: " + currentCondition;
            notifySubscribers(message);
            lastCondition = currentCondition;
        }
        alertIfWeatherConditionsChanged();
    }

    @Override
    public void onTemperatureChange(TempRecord record) {
        recentTemps.add(record);
        // remove any records that are older than 5 mins
        LocalDateTime oldestTimeAllowed = LocalDateTime.now().minusMinutes(5);
        recentTemps.removeIf(r -> r.getDt().isBefore(oldestTimeAllowed));

        double temperature = recentTemps.stream().mapToDouble(TempRecord::getTemp).sum();
        double averageTemp = temperature / recentTemps.size();

        currentTemperature = averageTemp;
        System.out.println("Average temp is " + averageTemp);
        if (averageTemp < 37) {
            String message = "Temperature alert: average temperature in the last 5 minutes is " + averageTemp + " units.";
            notifySubscribers(message);
        }
        alertIfWeatherConditionsChanged();
    }

    private void alertIfWeatherConditionsChanged() {
        // Check current weather conditions and send alert if conditions have changed
        WeatherCondition currentCondition = getCurrentWeatherCondition();
        if (!currentCondition.equals(lastCondition)) {
            String message = "Current weather condition: " + currentCondition;
            notifySubscribers(message);
            lastCondition = currentCondition;
        }
    }


    public WeatherCondition getCurrentWeatherCondition() {
        if (currentRainfall == 0 && currentTemperature <= 32) {
            return WeatherCondition.SNOW;
        } else if (currentRainfall <= 0.1 && currentTemperature > 32 && currentTemperature <= 50) {
            return WeatherCondition.HAIL;
        } else if (currentRainfall <= 0.1) {
            return WeatherCondition.DRIZZLE;
        } else if (currentRainfall < 0.5) {
            return WeatherCondition.RAIN;
        } else if (currentRainfall < 5) {
            return WeatherCondition.POUR;
        } else {
            return WeatherCondition.FLOOD;
        }
    }

    private void notifySubscribers(String message) {
        for(WeatherObserver observer : observers)
            observer.onWeatherAlert(message);
       // observers.stream().filter(Objects::nonNull).forEach(observer -> observer.onWeatherAlert(message));
    }
}
