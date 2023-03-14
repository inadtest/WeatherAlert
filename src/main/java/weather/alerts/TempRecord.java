package weather.alerts;

import java.time.LocalDateTime;

public class TempRecord {
    LocalDateTime dt;
    Double temp;
    TempRecord(LocalDateTime dt, Double temp) {
        this.dt = dt;
        this.temp = temp;
    }
    LocalDateTime getDt() {
        return dt;
    }

    Double getTemp() {
        return temp;
    }
}
