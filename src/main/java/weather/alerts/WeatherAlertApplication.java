package weather.alerts;

import java.time.LocalDateTime;

public class WeatherAlertApplication {
    public static void main(String[] args) {

        WeatherAlertSystem alert = new WeatherAlertSystem();
        WeatherAlertSubscriber subscriber1 = new WeatherAlertSubscriber("Subscriber 1");
        WeatherAlertSubscriber subscriber2 = new WeatherAlertSubscriber("Subscriber 2");

        alert.subscribe(subscriber1);
        alert.subscribe(subscriber2);

        // Receive temperature and rainfall updates and notify subscribers
        TempRecord tempRecord1 = new TempRecord(LocalDateTime.now(), 20.0);
        TempRecord tempRecord2 = new TempRecord(LocalDateTime.now().plusMinutes(1), 22.0);

        alert.onTemperatureChange(tempRecord1);
        alert.onTemperatureChange(tempRecord2);

        RainfallRecord rainfallRecord1 = new RainfallRecord(LocalDateTime.now(), 0.2);
        RainfallRecord rainfallRecord2 = new RainfallRecord(LocalDateTime.now().plusMinutes(1), 0.3);
        alert.onRainfallChange(rainfallRecord1);
        alert.onRainfallChange(rainfallRecord2);

        // Unsubscribe a subscriber
        alert.unsubscribe(subscriber2);

        // Receive temperature and rainfall updates and notify subscribers (without s2)
        TempRecord tempRecord3 = new TempRecord(LocalDateTime.now().plusMinutes(2), 24.0);
        alert.onTemperatureChange(tempRecord3);

        RainfallRecord rainfallRecord3 = new RainfallRecord(LocalDateTime.now().plusMinutes(2), 1.0);
        alert.onRainfallChange(rainfallRecord3);
    }
}
