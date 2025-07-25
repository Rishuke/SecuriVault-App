plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    id("com.google.gms.google-services") version "4.4.3"
    id("com.google.dagger.hilt.android") version "2.51"

    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.esgi.securivault"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.esgi.securivault"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    signingConfigs {
        create("release") {
            storeFile = file("../my-release-key.keystore")
            storePassword = "securivault"
            keyAlias = "alias"
            keyPassword = "securivault"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }




    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"
    }
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-stdlib")) {
                useVersion("2.0.0")
            }
        }
    }

}
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.compose.foundation:foundation:1.6.1")

    implementation(libs.googleid)
    implementation(libs.play.services.auth.v2041)

    implementation(libs.koin.androidx.compose)

    implementation(libs.converter.gson)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter.gson)

    // Hilt
    // Hilt
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.animation.core.lint)
    implementation(libs.firebase.firestore.ktx)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.9")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Firebase Auth
    implementation("com.google.firebase:firebase-auth-ktx")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    implementation (libs.androidx.material.icons.extended)
    testImplementation("io.mockk:mockk:1.13.9")

    implementation (libs.maps.compose)
    implementation (libs.play.services.maps)
    implementation (libs.osmdroid.android)
    implementation("androidx.activity:activity-compose:1.8.0")

    // Pour les requêtes HTTP (si pas déjà présent)
    implementation (libs.kotlinx.coroutines.android)
    testImplementation(kotlin("test"))
}
