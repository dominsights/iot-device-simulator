package actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.TelemetryData;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import javax.naming.OperationNotSupportedException;
import java.time.Duration;

public class DeviceActor extends AbstractBehavior<DeviceActor.Command> {
    private final MqttClient mqttClient;
    private final String topic;

    public DeviceActor(ActorContext<Command> context, MqttClient mqttClient, String topic) {
        super(context);
        this.mqttClient = mqttClient;
        this.topic = topic;
    }

    public record Start() implements Command {}
    private record Tick() implements Command {}

    public static Behavior<Command> create(MqttClient mqttClient, String topic) {
        return Behaviors.setup(context -> new DeviceActor(context, mqttClient, topic));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Start.class, this::onStart)
                .onMessage(Tick.class, this::onTick)
                .build();
    }

    public interface Command {}

    private Behavior<Command> onStart(Start command) {
        getContext().scheduleOnce(Duration.ZERO, getContext().getSelf(), new Tick());
        return this;
    }

    private Behavior<Command> onTick(Tick command) throws MqttException, JsonProcessingException {
        var mqttMessage = new MqttMessage();

        var telemetryData = new TelemetryData("test-id", "123", 10, 10, 10, 190, 10, 15, 20);

        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = mapper.writeValueAsBytes(telemetryData);

        mqttMessage.setPayload(bytes);
        mqttMessage.setQos(1);
        mqttMessage.setRetained(true);
        this.mqttClient.publish(topic, mqttMessage);
        return this;
    }

}
