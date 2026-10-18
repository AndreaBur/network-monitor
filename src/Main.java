public class Main {
    public static void main(String[] args) {

        // 1. Crear monitor y predictor
        NetworkMonitor monitor   = new NetworkMonitor();
        SignalPredictor predictor = new SignalPredictor();

        // 2. Agregar Access Points
        monitor.addAccessPoint(new AccessPoint("AP-01", "Oficina Central", -55.0));
        monitor.addAccessPoint(new AccessPoint("AP-02", "Bodega",          -85.0));
        monitor.addAccessPoint(new AccessPoint("AP-03", "Recepcion",       -67.0));
        monitor.addAccessPoint(new AccessPoint("AP-04", "Sala Reuniones",  -72.0));
        monitor.addAccessPoint(new AccessPoint("AP-05", "Parqueadero",     -91.0));

        // 3. Simular historial de lecturas anteriores (últimas 5 mediciones)
        double[][] historicalData = {
            // AP-01: señal estable
            {-58.0, -57.0, -56.0, -55.5, -55.0},
            // AP-02: señal empeorando
            {-74.0, -76.0, -79.0, -82.0, -85.0},
            // AP-03: señal mejorando
            {-75.0, -73.0, -71.0, -69.0, -67.0},
            // AP-04: señal levemente empeorando
            {-68.0, -69.0, -70.0, -71.0, -72.0},
            // AP-05: señal crítica y empeorando
            {-83.0, -85.0, -87.0, -89.0, -91.0}
        };

        // 4. Cargar historial en el predictor
        var accessPoints = monitor.getAccessPoints();
        for (int i = 0; i < accessPoints.size(); i++) {
            String apId = accessPoints.get(i).getId();
            for (double reading : historicalData[i]) {
                predictor.recordReading(apId, reading);
            }
        }

        // 5. Tomar lecturas actuales
        monitor.takeReadings();

        // 6. Reporte actual
        monitor.printReport();

        // 7. Alertas críticas
        System.out.println("\n⚠️  ALERTAS:");
        var critical = monitor.getCriticalAccessPoints();
        if (critical.isEmpty()) {
            System.out.println("  ✅ Todos los Access Points funcionan correctamente.");
        } else {
            for (var ap : critical) {
                System.out.printf("  🚨 ALERTA: %s en '%s' requiere atención! (%.1f dBm)%n",
                        ap.getId(), ap.getLocation(), ap.getSignalStrength());
            }
        }

        // 8. Reporte predictivo
        predictor.printPredictiveReport(monitor.getAccessPoints());

        System.out.println("\n══════════════════════════════════════════════════════\n");
    }
}