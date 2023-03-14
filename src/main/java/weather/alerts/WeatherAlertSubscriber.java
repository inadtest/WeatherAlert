package weather.alerts;

import java.util.LinkedList;
import java.util.List;

public class WeatherAlertSubscriber implements WeatherObserver{
    private String name;
    private List<String> messages;
    public WeatherAlertSubscriber(String name) {
        this.name = name;
        this.messages = new LinkedList<>();
    }

    @Override
    public void onWeatherAlert(String alertMessage) {
        String message = "Sending message " + alertMessage;
        System.out.println(message);
        messages.add(alertMessage);
    }

    public List<String> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
    }
}
