val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val css_version: String by project
val html_version: String by project

plugins {
    kotlin("jvm") version "1.6.10"
    `java-library`
    `maven-publish`
}

group = "com.sergeysav"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-html-builder:$ktor_version")
    api("org.jetbrains.kotlinx:kotlinx-html-jvm:$html_version")
    api("org.jetbrains.kotlin-wrappers:kotlin-css:$css_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "$group"
            artifactId = "website-generator"
            version = version

            pom {
                name.set("Website Generator")
                description.set("A Static Website Generator using Kotlin DSLs")
                url.set("http://www.github.com/SergeySave/WebsiteGenerator")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://raw.githubusercontent.com/SergeySave/WebsiteGenerator/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("sergeysave")
                        name.set("Sergey Savelyev")
                        email.set("me@sergeysav.com")
                    }
                }
            }

            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/SergeySave/WebsiteGenerator")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}
