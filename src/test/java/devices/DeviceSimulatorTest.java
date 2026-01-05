package devices;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.awaitility.Awaitility.await;

@ExtendWith(MockitoExtension.class)
class DeviceSimulatorTest {

    String telemetryJson = """
            {
              "deviceId": "MOTOR-001",
              "timestamp": "2025-01-15T14:30:00Z",
              "temperature": 72.5,
              "voltage": 220.2,
              "current": 15.3,
              "vibration": 0.05,
              "rpm": 1450,
              "operatingHours": 2543,
              "powerConsumption": 3.2
            }
            """;

    @Mock
    private MqttClient mqttClient;

    @InjectMocks
    private DeviceSimulator deviceSimulator;

    @Test
    public void should_return_telemetry_for_one_device_once() throws MqttException {
        deviceSimulator.start(1, "simulation 1");
        var mqttMessage = new MqttMessage("test message".getBytes());
        mqttMessage.setQos(1);
        mqttMessage.setRetained(true);

        await().atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(mqttClient).publish(eq("test/topic"), argThat(actual ->
                        "test message".equals(actual.toString()))));
    }

    @Test
    public void should_return_correct_json_format() throws JsonProcessingException {
        deviceSimulator.start(1, "simulation 1");
        var mqttMessage = new MqttMessage("test message".getBytes());
        mqttMessage.setQos(1);
        mqttMessage.setRetained(true);

        await().atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(mqttClient).publish(
                                eq("test/topic"),
                                argThat(actual -> {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    try {
                                        JsonNode jsonNode = objectMapper.readTree(actual.toString());

                                        return jsonNode.has("deviceId") &&
                                                jsonNode.has("timestamp") &&
                                                jsonNode.has("temperature") &&
                                                jsonNode.has("voltage") &&
                                                jsonNode.has("current") &&
                                                jsonNode.has("vibration") &&
                                                jsonNode.has("rpm") &&
                                                jsonNode.has("operatingHours") &&
                                                jsonNode.has("powerConsumption") &&
                                                jsonNode.get("deviceId").isTextual() &&
                                                jsonNode.get("timestamp").isTextual() &&
                                                jsonNode.get("temperature").isNumber() &&
                                                jsonNode.get("voltage").isNumber() &&
                                                jsonNode.get("current").isNumber() &&
                                                jsonNode.get("vibration").isNumber() &&
                                                jsonNode.get("rpm").isInt() &&
                                                jsonNode.get("operatingHours").isInt() &&
                                                jsonNode.get("powerConsumption").isNumber();

                                    } catch (JsonProcessingException e) {
                                        return false;
                                    }
                                })));

    }
}