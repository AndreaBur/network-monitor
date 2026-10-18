import java.util.ArrayList;
import java.util.List;

public class NetworkMonitor {
    private List<AccessPoint> accessPoints;
    private List<SignalReading> readings;

    public NetworkMonitor() {
        this.accessPoints = new ArrayList<>();
        this.readings = new ArrayList<>();
    }

    public void addAccessPoint(AccessPoint ap) {
        accessPoints.add(ap);
    }

    public void takeReadings() {
        readings.clear();
        for (AccessPoint ap : accessPoints) {
            SignalReading reading = new SignalReading(ap.getId(), ap.getSignalStrength());
            readings.add(reading);
        }
    }

    public void printReport() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         NETWORK MONITOR - REPORTE DE SEÑAL          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        for (AccessPoint ap : accessPoints) {
            String emoji = getEmoji(ap.getStatus());
            System.out.printf("  %s  %s%n", emoji, ap.toString());
        }

        System.out.println("──────────────────────────────────────────────────────");
        printStats();
    }

    private void printStats() {
        double total = 0;
        double min = accessPoints.get(0).getSignalStrength();
        double max = accessPoints.get(0).getSignalStrength();

        for (AccessPoint ap : accessPoints) {
            double signal = ap.getSignalStrength();
            total += signal;
            if (signal < min) min = signal;
            if (signal > max) max = signal;
        }

        double average = total / accessPoints.size();
        System.out.printf("  Promedio: %.1f dBm | Mejor: %.1f dBm | Peor: %.1f dBm%n",
                average, max, min);
    }

    public List<AccessPoint> getCriticalAccessPoints() {
        List<AccessPoint> critical = new ArrayList<>();
        for (AccessPoint ap : accessPoints) {
            if (ap.getStatus().equals("CRITICA")) {
                critical.add(ap);
            }
        }
        return critical;
    }

    private String getEmoji(String status) {
        switch (status) {
            case "EXCELENTE": return "✅";
            case "BUENA":     return "🟢";
            case "REGULAR":   return "🔶";
            case "CRITICA":   return "🔴";
            default:          return "❓";
        }
    }

    public List<AccessPoint> getAccessPoints() { return accessPoints; }
    public List<SignalReading> getReadings() { return readings; }
}