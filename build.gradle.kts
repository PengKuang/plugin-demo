import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
//    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    id("org.jetbrains.intellij.platform") version "2.0.1"
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.changelog)
}

group = "org.nex.kui"
version = "0.2.0"


        repositories {
            mavenCentral()

            intellijPlatform {
                defaultRepositories()
            }
        }

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2.0.1")

        bundledPlugin("com.intellij.java")

        pluginVerifier()
        zipSigner()
        instrumentationTools()

        testFramework(TestFrameworkType.Platform)
    }

    testImplementation("junit:junit:4.13.2")
    // other dependencies, e.g., 3rd-party libraries
}


dependencies {
    // https://mvnrepository.com/artifact/com.opencsv/opencsv
    implementation("com.opencsv:opencsv:5.7.1")
    // implementation("org.bytedeco:javacv-platform:1.5.9") // NOTE: This is too large
    implementation("org.bytedeco:javacv:1.5.9")
    implementation("org.bytedeco:ffmpeg:6.0-1.5.9")
    implementation("org.bytedeco:ffmpeg-platform:6.0-1.5.9")


// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
    intellijPlatform {
//    version.set("2024.1.2")
//
////    type.set("IC") // IntelliJ Community Edition
//    type.set("IU") // IntelliJ Ultimate Edition
//    plugins.set(listOf("com.intellij.java"))

        intellijIdeaCommunity("2024.2.0.1")

        bundledPlugin("com.intellij.java")

        pluginVerifier()
        zipSigner()
        instrumentationTools()

    testFramework(TestFrameworkType.Platform)

//    pluginConfiguration {
//        version = providers.gradleProperty("pluginVersion")
//
//        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
//        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
//            val start = "<!-- Plugin description -->"
//            val end = "<!-- Plugin description end -->"
//
//            with(it.lines()) {
//                if (!containsAll(listOf(start, end))) {
//                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
//                }
//                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
//            }
//        }
//
//        ideaVersion {
//            sinceBuild = providers.gradleProperty("pluginSinceBuild")
//            untilBuild = providers.gradleProperty("pluginUntilBuild")
//        }
//    }
    }
}



tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
//        sourceCompatibility = "21"
    }
//    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//        kotlinOptions.jvmTarget = "17"
//    }

    patchPluginXml {
        sinceBuild.set("222") // 2022.2 NOTE Java 17 is now required
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    val createOpenApiSourceJar by registering(Jar::class) {
        from(sourceSets.main.get().java) {
            include("**/api/**/*.java")
        }
        destinationDirectory.set(layout.buildDirectory.dir("libs"))
        archiveClassifier.set("src")
    }

    buildPlugin {
        dependsOn(createOpenApiSourceJar)
        from(createOpenApiSourceJar) { into("lib/src") }
    }
}