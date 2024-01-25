plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.app_yeongmi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app_yeongmi"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }






    packaging {
        resources {
            exclude ("META-INF/INDEX.LIST")
            exclude  ("META-INF/proguard/androidx-annotations.pro")
            exclude   ("META-INF/proguard/androidx-component_annotations.pro")
            exclude   ("META-INF/io.netty.versions.properties")
            exclude("META-INF/io.netty.versions.properties")
            exclude ("META-INF/LICENSE")


            // Add more exclusions if needed
        }
    }




}








dependencies {




    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.hivemq:hivemq-mqtt-client:1.3.0")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation ("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation ("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}