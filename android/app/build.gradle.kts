import java.util.Properties

val sdkVersionFile = file("../gradle.properties")
val properties = Properties()
properties.load(sdkVersionFile.inputStream())
val agoraSdkVersion = properties.getProperty("rtc_sdk_version")
println("${rootProject.project.name}   agoraSdkVersion: ${agoraSdkVersion}")
val localSdkPath = "${rootProject.projectDir.absolutePath}/../../sdk"

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinSerialization)
    id("com.google.dagger.hilt.android")
    id("com.google.protobuf") version "0.9.4"
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

    id("kotlin-parcelize") // add
    id("kotlin-kapt")
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

        val properties = Properties()
        properties.load(rootProject.file("local.properties").inputStream())
        val AGORA_APP_ID = properties.getProperty("AGORA_APP_ID", "")
        if (AGORA_APP_ID == "") {
            throw GradleException("local.properties：AGORA_APP_ID=<AppId>")
        }
        val AGORA_APP_CERT = properties.getProperty("AGORA_APP_CERT", "")
        buildConfigField("String", "AGORA_APP_ID", "\"$AGORA_APP_ID\"")
        buildConfigField("String", "AGORA_APP_CERT", "\"$AGORA_APP_CERT\"")
    }

    buildFeatures {
        buildConfig = true
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
// https://mvnrepository.com/artifact/io.getstream/stream-webrtc-android
//    implementation("org.webrtc:google-webrtc:1.0.30039@aar")
//    implementation("io.getstream:stream-webrtc-android:1.3.7")
//    implementation("io.agora.rtc:voice-sdk:4.")
//    implementation(libs.webrtc)


    // STOMP
    implementation("org.hildan.krossbow:krossbow-stomp-core:7.0.0")
    implementation("org.hildan.krossbow:krossbow-websocket-builtin:7.0.0")
    implementation("org.hildan.krossbow:krossbow-websocket-okhttp:7.0.0")
    implementation("org.hildan.krossbow:krossbow-stomp-moshi:7.0.0")

    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    implementation(libs.androidx.core.ktx.v1120)  // ✅ Parcelable 확장 함수 포함

    implementation(libs.gson)

    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // ✅ Gson Converter 추가

    // 시스템 UI 조작 가능
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    // Firebase FCM
    implementation(platform(libs.firebase.bom))

    implementation(libs.material)

    // Permission
    implementation(libs.accompanist.permissions)

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
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Web RTC
    if (File(localSdkPath).exists()) {
        implementation(fileTree(localSdkPath).include("*.jar", "*.aar"))
    } else {
        implementation("io.agora.rtc:full-sdk:${agoraSdkVersion}")
        implementation("io.agora.rtc:full-screen-sharing:${agoraSdkVersion}")
//        implementation(libs.agora.full.sdk)
//        implementation(libs.agora.full.screen.sharing)
    }

    //hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.firebase.messaging.ktx)
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
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