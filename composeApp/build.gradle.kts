import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleGmsGoogleServices)
    kotlin("plugin.serialization") version "1.9.24"
}

kotlin {
    androidTarget() // Removed invalid compilerOptions block

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("com.google.firebase:firebase-firestore-ktx:24.10.1")
            implementation("io.ktor:ktor-client-core:2.3.10")
            implementation("io.ktor:ktor-client-cio:2.3.10")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            implementation("io.coil-kt:coil-compose:2.4.0")
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "org.finAware.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.finAware.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.compose.material)
    implementation(libs.firebase.messaging.ktx)
    debugImplementation(compose.uiTooling)
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // Compose
    implementation("org.jetbrains.compose.runtime:runtime:1.5.10")
    implementation("org.jetbrains.compose.material3:material3:1.5.10")

    // Lifecycle ViewModel (Android only)
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")

    // For Decompose (KMP)
    implementation("com.arkivanov.decompose:decompose:2.2.0")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.0")

    // Coroutines (shared logic)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    implementation("com.google.firebase:firebase-firestore")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // UI
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
}
