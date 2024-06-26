plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.serialization") version "1.5.21"
  kotlin("kapt")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.aura"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.aura"
    minSdk = 24
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"
    buildConfigField("String", "SERVER", "\"http://10.0.2.2:8080/\"")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    viewBinding = true
    android.buildFeatures.buildConfig = true
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  kapt {
    correctErrorTypes = true
    useBuildCache = true
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.13.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.11.0")
  implementation("androidx.annotation:annotation:1.7.1")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  //implémentation viewmodel et livedata
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
  implementation("androidx.fragment:fragment-ktx:1.6.2")

  //injection
  implementation("com.google.dagger:hilt-android:2.51")
  kapt("com.google.dagger:hilt-android-compiler:2.51")
  //retrofit
  implementation("com.squareup.retrofit2:retrofit:2.10.0")
  //convertisseur ktxserialisation pour Retrofit
  implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
  //OkHttp
  implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
  implementation("com.squareup.okhttp3:okhttp:4.12.0")
  //ktx serialization
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")



  //test
  testImplementation("org.mockito:mockito-core:5.2.0")
  testImplementation("org.mockito:mockito-inline:5.2.0")
  //coroutine Test
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

}