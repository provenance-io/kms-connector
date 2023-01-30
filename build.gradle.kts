import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

repositories {
    mavenCentral()
}

allprojects {
    group = "io.provenance.kms-connector"

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

subprojects {
    val subProjectName = name

    apply {
        plugin("maven-publish")
        plugin("signing")
        plugin("java-library")
        plugin("org.jetbrains.kotlin.jvm")
    }

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "io.provenance.kms-connector"
                artifactId = subProjectName

                from(components["java"])

                pom {
                    name.set("Provenance Key Access Library")
                    description.set("A simple key access library for use with provenance onboarding.")
                    url.set("https://provenance.io")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("cworsnop-figure")
                            name.set("Cody Worsnop")
                            email.set("cworsnop@figure.com")
                        }
                    }

                    scm {
                        connection.set("git@github.com:provenance-io/kms-connector.git")
                        developerConnection.set("git@github.com:provenance-io/kms-connector.git")
                        url.set("https://github.com/provenance-io/kms-connector")
                    }
                }
            }
        }

        signing {
            sign(publishing.publications["maven"])
        }

        tasks.javadoc {
            if(JavaVersion.current().isJava9Compatible) {
                (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(findProject("ossrhUsername")?.toString() ?: System.getenv("OSSRH_USERNAME"))
            password.set(findProject("ossrhPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD"))
            stagingProfileId.set("3180ca260b82a7") // prevents querying for the staging profile id, performance optimization
        }
    }
}