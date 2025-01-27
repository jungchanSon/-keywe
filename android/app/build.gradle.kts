plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinSerialization)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.protobuf") version "0.9.4"
}

val protobufVersion = "3.21.7"

android {
    namespace = "com.ssafy.keywe"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ssafy.keywe"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.material)

    implementation(libs.navigation.compose)


    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization.converter)
    implementation(libs.converter.scalars)

    // Kotlinx-Serialization
    implementation(libs.kotlinx.serialization.json)
    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel)

    //hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
//    implementation(libs.hilt.android)
//    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation("com.google.protobuf:protobuf-javalite:$protobufVersion")

    // jwt decode
    implementation("com.auth0.android:jwtdecode:2.0.2")

    // splash
    implementation(libs.androidx.core.splashscreen)

    // logging
    implementation(libs.timber)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.coil.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}


// Allow references to generated code
kapt {
    correctErrorTypes = true
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        generateProtoTasks {
            all().forEach {
                it.builtins {
                    create("java") {
                        option("lite")
                    }
                }
            }
        }
    }
}