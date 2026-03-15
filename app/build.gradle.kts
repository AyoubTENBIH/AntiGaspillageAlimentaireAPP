/**
 * Build configuration for SaveAt app module.
 * Uses ViewBinding, Room, Navigation, Material3, MVVM. Java only (no Kotlin).
 */
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.anti_gaspillagealimentaireapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.anti_gaspillagealimentaireapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.appcompat)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation(libs.material)
    implementation(libs.glide)
    implementation(libs.androidx.work.runtime)
    implementation(libs.mpandroidchart)
    implementation(libs.osmdroid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.0")
}
