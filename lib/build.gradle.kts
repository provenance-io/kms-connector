plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:30.1-jre")
    implementation("com.konghq:unirest-java:3.13.6")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("io.provenance.scope:encryption:0.1.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
