package weather.alerts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class WeatherAlertSystemTest {
    private WeatherAlertSystem weatherAlertSystem;

   // @Mock
    private WeatherAlertSubscriber subscriber;

    @Before
    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        weatherAlertSystem = new WeatherAlertSystem();
//        weatherAlertSystem.subscribe(observer);
        weatherAlertSystem = new WeatherAlertSystem();
        subscriber = new WeatherAlertSubscriber("testSubscriber");
        weatherAlertSystem.subscribe(subscriber);
    }

  /*  @Test
    public void testOnRainfallChange() {
        RainfallRecord record1 = new RainfallRecord(LocalDateTime.now(), 0.5);
        RainfallRecord record2 = new RainfallRecord(LocalDateTime.now(), 1.5);
        RainfallRecord record3 = new RainfallRecord(LocalDateTime.now(), 1.0);
        weatherAlertSystem.onRainfallChange(record1);
        verify(observer, times(0)).onWeatherAlert("Rainfall alert: expected rainfall over the next hour is 0.4 inches.");
        weatherAlertSystem.onRainfallChange(record2);
        verify(observer, times(1)).onWeatherAlert("Rainfall alert: expected rainfall over the next hour is 1.4 inches.");
        weatherAlertSystem.onRainfallChange(record3);
        verify(observer, times(1)).onWeatherAlert("Rainfall alert: expected rainfall over the next hour is 1.0 inches.");
    }*/

    @Test
    public void testTemperatureAlert() {
        // no alert
        weatherAlertSystem.onTemperatureChange(new TempRecord(LocalDateTime.now(), 40d));

        // temperature drops to 30 - this should trigger an alert
        weatherAlertSystem.onTemperatureChange(new TempRecord(LocalDateTime.now(), 30d));
        assertEquals(2, subscriber.getMessages().size());

        // temperature goes to 37 - alert should be triggered
        weatherAlertSystem.onTemperatureChange(new TempRecord(LocalDateTime.now(), 37d));
        System.out.println(subscriber.getMessages().get(2));
        assertEquals(3, subscriber.getMessages().size());

        // temperature drops further to 30 - this should trigger another alert
        weatherAlertSystem.onTemperatureChange(new TempRecord(LocalDateTime.now(), 30d));
        assertEquals(4, subscriber.getMessages().size());

        subscriber.clearMessages();
    }

    @Test
    public void testRainFallAlert() {

        weatherAlertSystem.onRainfallChange(new RainfallRecord(LocalDateTime.now(), 1.0));

        weatherAlertSystem.onRainfallChange(new RainfallRecord(LocalDateTime.now().plusMinutes(50), 0.7));
        weatherAlertSystem.onRainfallChange(new RainfallRecord(LocalDateTime.now().plusMinutes(60), 0.9));

        List<String> messages = subscriber.getMessages();
        assertEquals(2, messages.size());
        assertEquals("Rainfall alert: expected rainfall over the next hour is 2.4 inches.", messages.get(1));
    }
}
