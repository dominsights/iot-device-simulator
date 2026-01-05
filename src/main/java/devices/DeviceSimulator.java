package devices;

import actors.DeviceActor;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import org.eclipse.paho.mqttv5.client.MqttClient;

public class DeviceSimulator {
    private final MqttClient mqttClient;

    public DeviceSimulator(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void start(int i, String s) {
        ActorSystem<DeviceActor.Command> system = ActorSystem.create(
                DeviceActor.create(mqttClient, "test/topic"),
                "my-system"
        );

        system.tell(new DeviceActor.Start());
    }
}
