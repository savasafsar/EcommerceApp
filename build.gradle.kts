buildscript {
    val agp_version by extra("8.0.2")
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

    }
    dependencies {
        classpath ("com.android.tools.build:gradle:$agp_version")
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0") // Kotlin sürümünü 1.5.31 olarak değiştirin
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
    }
}

plugins {
    id("com.android.application") version "8.1.0-rc01" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}
