import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    `kotlin-dsl`
    java
}

repositories {
    maven("https://plugins.gradle.org/m2/")
    maven("https://developer.huawei.com/repo/")
    google()
    mavenCentral()

    maven("https://maven.pkg.github.com/Rocket-Beer/*") {
        credentials {
            println("**** Rocket Beer maven ****")

            var userName: String?
            var token: String?

            try {
                val path = "$projectDir/../local.properties"

                println("local properties path = $path")

                val properties = loadProperties(path)

                userName = properties.getProperty("github.username")
                if (userName.isEmpty()) userName = System.getenv("GITHUB_ACTOR")

                token = properties.getProperty("github.token")
                if (token.isEmpty()) token = System.getenv("GITHUB_TOKEN")
            } catch (e: Exception) {
                userName = System.getenv("GITHUB_ACTOR")
                token = System.getenv("GITHUB_TOKEN")
            }

            println("username = $userName :: token = $token")

            username = userName ?: ""
            password = token ?: ""
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:4.2.2")
    implementation(kotlin("gradle-plugin", version = "1.5.20"))
    implementation("rocket-gradle:rocket-plugin:1.0-dev02")
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
    implementation("com.huawei.agconnect:agcp:1.4.2.300")
}