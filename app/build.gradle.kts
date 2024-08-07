import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.github.parking.smartparking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.parking.smartparking"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    var localProperties: Properties? = null
    try {
        localProperties = Properties()
        localProperties.load(project.rootProject.file("local.properties").inputStream())

    } catch (_: Exception) {

    }


    buildTypes {
        debug {
            isDebuggable = true
            buildConfigField(
                "String",
                "CONSUMER_KEY",
                "\"${System.getenv("CONSUMER_KEY") ?: localProperties?.getProperty("CONSUMER_KEY")}\""
            )
            buildConfigField(
                "String",
                "CONSUMER_SECRET",
                "\"${System.getenv("CONSUMER_SECRET") ?: localProperties?.getProperty("CONSUMER_SECRET")}\""
            )
        }
        release {
            buildConfigField(
                "String",
                "CONSUMER_SECRET",
                "\"${System.getenv("CONSUMER_SECRET") ?: localProperties?.getProperty("CONSUMER_SECRET")}\""
            )
            buildConfigField(
                "String",
                "CONSUMER_KEY",
                "\"${System.getenv("CONSUMER_KEY") ?: localProperties?.getProperty("CONSUMER_KEY")}\""
            )
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.destinations.core)
    implementation(libs.destinations.animations.core)
    ksp(libs.ksp)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.androidx.hilt.compiler)

    //    coroutines
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.okhttp)


    //Retrofit-
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)
}
