plugins {
    id("java")
}

group = "org.dominsights"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        implementation("org.eclipse.paho:org.eclipse.paho.mqttv5.client:1.2.5")
        // Akka Actor Typed (modern API)
        implementation("com.typesafe.akka:akka-actor_2.13:2.8.8")
        // SLF4J logging (required by Akka)
        implementation("ch.qos.logback:logback-classic:1.5.13")
        // Optional: Akka Streams
        implementation("com.typesafe.akka:akka-stream-typed_2.13:2.8.8")
        // Optional: Testing
        testImplementation("com.typesafe.akka:akka-actor-testkit-typed_2.13:2.8.8")
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    }
}

tasks.test {
    useJUnitPlatform()
}