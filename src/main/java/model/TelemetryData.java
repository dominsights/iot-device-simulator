package model;

public record TelemetryData(
    String deviceId,
    String timestamp,
    double temperature,
    double voltage,
    double current,
    double vibration,
    int rpm,
    int operatingHours,
    double powerConsumption
) {
}
