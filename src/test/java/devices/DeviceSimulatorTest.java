package devices;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class DeviceSimulatorTest {

    @Mock
    private MqttClient mqttClient;

    @InjectMocks
    private DeviceSimulator deviceSimulator;

    @Test
    public void should_return_telemetry_for_one_device_once() {
        deviceSimulator.start(1, "simulation 1");
        
    }
}