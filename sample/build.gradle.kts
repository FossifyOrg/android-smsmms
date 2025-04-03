plugins {
    id("com.android.application")
}

android {
    namespace = "com.klinker.android.messaging_sample"
    compileSdk = 31

    defaultConfig {
        applicationId = "com.klinker.android.send_message.sample"
        minSdk = 22
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        abortOnError = false
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {
    implementation(project(":library"))
    implementation("com.android.support:recyclerview-v7:28.0.0")
    implementation("com.klinkerapps:logger:1.0.3")
}