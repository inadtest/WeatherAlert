package weather.alerts;

import java.time.LocalDateTime;

public class RainfallRecord {
    LocalDateTime dt;
    Double rainfall;  // new amount since last recording

    RainfallRecord(LocalDateTime dt, Double rainfall) {
        this.dt = dt;
        this.rainfall = rainfall;
    }
    LocalDateTime getDt() {
        return dt;
    }

    Double getRainfall() {
        return rainfall;
    }
}
