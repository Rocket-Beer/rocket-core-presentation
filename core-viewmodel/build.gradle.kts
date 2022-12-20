plugins {
    id("com.android.library")
    id("kotlin-android")

}

tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val mainSrc = "${project.projectDir}/src/main/kotlin"
    sourceDirectories.setFrom(files(listOf(mainSrc)))

    val debugTree = fileTree(baseDir = "${project.buildDir}/tmp/kotlin-classes/debug") {
        val fileFilter =
            setOf("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*")
        setExcludes(fileFilter)
    }
    classDirectories.setFrom(files(listOf(debugTree)))

    executionData.setFrom(fileTree(project.buildDir) { include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec") })
}


android {
    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")

    implementation("androidx.appcompat:appcompat:1.3.1")

    implementation("com.rocket.core:core-domain:0.0.3-alpha6")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
