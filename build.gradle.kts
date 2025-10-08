// Top-level build file (build.gradle.kts)

plugins {
    // For the main 'app' module
    alias(libs.plugins.android.application) apply false

    // For any library modules in your project
    alias(libs.plugins.android.library) apply false

    // Core Kotlin plugin for Android
    alias(libs.plugins.kotlin.android) apply false

    // Kotlin plugin for Jetpack Compose compiler
    alias(libs.plugins.kotlin.compose) apply false

    // Google Services plugin for Firebase integration
    alias(libs.plugins.google.gms.google.services) apply false

    //id("com.google.gms.google-services") version "4.4.2" apply false
}