plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.outfitorganizer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.outfitorganizer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.support.annotations)
    implementation(libs.firebase.database)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.prolificinteractive:material-calendarview:1.4.3")

    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.prolificinteractive:material-calendarview:1.4.3")
    implementation("com.google.firebase:firebase-storage:20.2.0")  // Firebase Storage
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-storage:20.0.1")
    implementation("com.google.firebase:firebase-auth:${libs.versions.firebase.auth.get()}")  // Firebase Authentication
    implementation("com.google.firebase:firebase-firestore:${libs.versions.firebase.firestore.get()}")  // Firebase Firestore
    implementation("androidx.multidex:multidex:${libs.versions.multidex.get()}") // Multidex support
    implementation("com.github.bumptech.glide:glide:4.15.1")  // Glide core library
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1") // Glide annotation processor
    implementation("com.squareup.picasso:picasso:2.8")

}
