dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:30.1-jre")
    implementation("com.konghq:unirest-java:3.13.6")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("io.provenance.scope:encryption:0.6.4")
    implementation("io.provenance.scope:util:0.6.4")
    implementation("io.provenance.hdwallet:hdwallet:0.1.15")
    implementation("io.provenance.hdwallet:hdwallet-bip39:0.1.15")
    implementation("com.fortanix:sdkms-client:3.23.1408")
    implementation("com.google.protobuf:protobuf-java:3.21.12")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
    
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.5")
    testImplementation("io.kotest:kotest-property-jvm:5.5.5")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.5")
    testImplementation("io.kotest:kotest-framework-datatest-jvm:5.5.5")
    testImplementation("org.slf4j:slf4j-simple:1.7.36")
    testImplementation("io.mockk:mockk:1.12.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}