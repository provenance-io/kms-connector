import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import net.swiftzer.semver.SemVer

buildscript {
    dependencies {
        classpath("net.swiftzer.semver:semver:1.1.2")
        classpath("com.github.breadmoirai:github-release:2.2.12")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("com.github.breadmoirai.github-release") version "2.2.12"
    id("io.github.nefilim.gradle.semver-plugin") version "0.3.10"
}

semver {
    tagPrefix("v")
    initialVersion("0.2.2")
    val semVerModifier = findProperty("semver.modifier")?.toString()?.let { buildVersionModifier(it) } ?: { nextPatch() }
    versionModifier(semVerModifier)
}

/**
 * The code below is a workaround for [gradle/gradle#20016](https://github.com/gradle/gradle/issues/20016) derived from
 * [platform-portal@14319ed/docs/semver.md](https://github.com/FigureTechnologies/platform-portal/blob/14319ed7e97d88a1b8cbb2f2f7708cc0660dc518/docs/semver.md#limiting-dependencies-to-release-versions)
 * to prevent version strings like `"1.0.+"` from resolving to pre-release versions.
 * You can adjust the string set constants or comment the code out entirely to allow pre-release versions to be used.
 */

val invalidQualifiers = setOf("alpha", "beta", "rc", "nightly")
val onlyReleaseArtifacts = setOf("originator-key-access-lib")
val whiteListedMavenGroups = setOf("com.figure", "tech.figure", "io.provenance")

configurations.all {
    resolutionStrategy {
        componentSelection {
            all {
                when {
                    (
                            onlyReleaseArtifacts.any { candidate.moduleIdentifier.name.startsWith(it) } &&
                                    !candidate.version.toSemVer()?.preRelease.isNullOrEmpty()
                            ) -> {
                        reject("Rejecting prerelease version for OnlyReleaseArtifact[$candidate]")
                    }
                    (
                            whiteListedMavenGroups.none { candidate.group.startsWith(it) } &&
                                    invalidQualifiers.any { candidate.version.contains(it) }
                            ) -> {
                        reject("Invalid qualifier versions for $candidate")
                    }
                }
            }
        }
    }
}

fun String?.toSemVer(): SemVer? =
    try {
        this?.let { versionString ->
            SemVer.parse(versionString)
        }
    } catch (e: Exception) {
        project.logger.info("Failed to parse semantic version from string '$this'")
        null
    }

repositories {
    mavenCentral()
}

val semVersion = semver.version
allprojects {
    group = "io.provenance.originator-key-access-lib"
    version = semVersion

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
                groupId = "io.provenance.originator-key-access-lib"
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
                        connection.set("git@github.com:provenance-io/originator-key-access-lib.git")
                        developerConnection.set("git@github.com:provenance-io/originator-key-access-lib.git")
                        url.set("https://github.com/provenance-io/originator-key-access-lib")
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

val githubTokenValue = findProperty("githubToken")?.toString() ?: System.getenv("GITHUB_TOKEN")

githubRelease {
    token(githubTokenValue)
    owner("provenance-io")
    targetCommitish("main")
    draft(false)
    prerelease(false)
    repo("originator-key-access-lib")
    tagName(semver.versionTagName)
//    body(changelog())

    overwrite(false)
    dryRun(false)
    apiEndpoint("https://api.github.com")
    client
}