plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.project01_group05"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.project01_group05"
        minSdk = 36
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // Retrofit - used to communicate with the MangaDex API
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Gson converter - converts MangaDex JSON responses into Java objects
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}