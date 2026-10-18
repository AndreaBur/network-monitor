import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignalPredictor {

    // Historial: cada AP tiene una lista de lecturas anteriores
    private Map<String, List<Double>> signalHistory;

    public SignalPredictor() {
        this.signalHistory = new HashMap<>();
    }

    // Agrega una lectura al historial de un AP
    public void recordReading(String apId, double signalValue) {
        signalHistory.putIfAbsent(apId, new ArrayList<>());
        signalHistory.get(apId).add(signalValue);
    }

    // Predice la próxima señal usando regresión lineal simple
    public double predictNextSignal(String apId) {
        List<Double> history = signalHistory.get(apId);

        if (history == null || history.size() < 2) {
            return Double.NaN; // no hay suficientes datos
        }

        int n = history.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            double x = i;                    // tiempo (posición)
            double y = history.get(i);       // valor de señal
            sumX  += x;
            sumY  += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        // Fórmula de regresión lineal: y = mx + b
        double m = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double b = (sumY - m * sumX) / n;

        // Predice el siguiente punto (posición n)
        return m * n + b;
    }

    // Clasifica la tendencia de la señal
    public String getTrend(String apId) {
        List<Double> history = signalHistory.get(apId);
        if (history == null || history.size() < 2) return "SIN DATOS";

        double predicted = predictNextSignal(apId);
        double current   = history.get(history.size() - 1);
        double diff      = predicted - current;

        if (diff > 2)  return "📈 MEJORANDO";
        if (diff < -2) return "📉 EMPEORANDO";
        return "➡️  ESTABLE";
    }

    // Genera recomendación según tendencia y señal actual
    public String getRecommendation(String apId, double currentSignal) {
        String trend = getTrend(apId);
        double predicted = predictNextSignal(apId);

        if (trend.contains("EMPEORANDO") && predicted < -80) {
            return "🚨 ACCION URGENTE: señal caerá a crítica — revisar AP";
        }
        if (trend.contains("EMPEORANDO") && predicted < -70) {
            return "⚠️  PREVENTIVO: señal degradándose — programar mantenimiento";
        }
        if (trend.contains("MEJORANDO")) {
            return "✅ EN RECUPERACION: señal mejorando — continuar monitoreo";
        }
        return "ℹ️  ESTABLE: sin cambios significativos";
    }

    // Imprime el reporte predictivo completo
    public void printPredictiveReport(List<AccessPoint> accessPoints) {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         MODELO PREDICTIVO - ANALISIS DE TENDENCIA   ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        for (AccessPoint ap : accessPoints) {
            String apId     = ap.getId();
            double current  = ap.getSignalStrength();
            double predicted = predictNextSignal(apId);
            String trend    = getTrend(apId);
            String rec      = getRecommendation(apId, current);

            System.out.println("\n  AP: " + apId + " | " + ap.getLocation());
            System.out.printf ("  Señal actual:   %.1f dBm%n", current);

            if (!Double.isNaN(predicted)) {
                System.out.printf("  Señal estimada: %.1f dBm%n", predicted);
            } else {
                System.out.println("  Señal estimada: insuficientes datos");
            }

            System.out.println("  Tendencia:      " + trend);
            System.out.println("  Recomendación:  " + rec);
            System.out.println("  ──────────────────────────────────────────────────");
        }
    }

    public Map<String, List<Double>> getSignalHistory() { return signalHistory; }
}