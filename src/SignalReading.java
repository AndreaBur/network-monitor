import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignalReading {
    private String accessPointId;
    private double signalValue;
    private LocalDateTime timestamp;

    public SignalReading(String accessPointId, double signalValue) {
        this.accessPointId = accessPointId;
        this.signalValue = signalValue;
        this.timestamp = LocalDateTime.now(); // captura el momento exacto
    }

    public String getAccessPointId() { return accessPointId; }
    public double getSignalValue() { return signalValue; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        String time = timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return String.format("[%s] AP: %-6s | Señal: %.1f dBm",
                time, accessPointId, signalValue);
    }
}