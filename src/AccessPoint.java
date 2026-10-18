public class AccessPoint {
    private String id;
    private String location;
    private double signalStrength; // en dBm

    public AccessPoint(String id, String location, double signalStrength) {
        this.id = id;
        this.location = location;
        this.signalStrength = signalStrength;
    }

    // Getters
    public String getId() { return id; }
    public String getLocation() { return location; }
    public double getSignalStrength() { return signalStrength; }
    public void setSignalStrength(double signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getStatus() {
        if (signalStrength >= -60) return "EXCELENTE";
        if (signalStrength >= -70) return "BUENA";
        if (signalStrength >= -80) return "REGULAR";
        return "CRITICA";
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-15s | %6.1f dBm | %s",
                id, location, signalStrength, getStatus());
    }
}