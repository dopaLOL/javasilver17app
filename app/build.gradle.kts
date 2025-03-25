import java.util.Properties
import java.io.FileInputStream
plugins {

    alias(libs.plugins.android.application)
}



        android {
            namespace = "com.example.java"
            compileSdk = 35

            defaultConfig {
                applicationId = "com.example.java"
                minSdk = 24
                targetSdk = 34
                versionCode = 1
                versionName = "1.0"
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                // local.properties を読み込む
                val localProperties = Properties()
                val localPropertiesFile = rootProject.file("local.properties")
                if (localPropertiesFile.exists()) {
                    FileInputStream(localPropertiesFile).use { inputStream ->
                        localProperties.load(inputStream)
                    }
                }

                // APIキーを BuildConfig に追加（defaultConfig 内）
                buildConfigField("String", "CONTENTFUL_SPACE_ID", "\"${localProperties.getProperty("CONTENTFUL_SPACE_ID", "")}\"")
                buildConfigField("String", "CONTENTFUL_ACCESS_TOKEN", "\"${localProperties.getProperty("CONTENTFUL_ACCESS_TOKEN", "")}\"")
            }

            // `buildTypes` ブロックは `defaultConfig` の外に記述する
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
        }

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.contentful.java:java-sdk:10.5.21")
    implementation("androidx.appcompat:appcompat:1.6.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
