plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("androidx.navigation.safeargs")
	id("com.google.devtools.ksp") version "1.9.22-1.0.16"
	id("kotlin-kapt")
	id("kotlin-parcelize")
}

android {
	namespace = "com.net3hings.triviagameapp"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.net3hings.triviagameapp"
		minSdk = 26
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
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		dataBinding = true
		viewBinding = true
	}
}

dependencies {
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.google.android.material:material:1.11.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")

	// NavFragment
	implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
	implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

	// Retrofit and Moshi
	implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

	// Room dependencies
	implementation("androidx.room:room-runtime:2.6.1")
	implementation("androidx.legacy:legacy-support-v4:1.0.0")
	implementation("androidx.recyclerview:recyclerview:1.3.2")
	ksp("androidx.room:room-compiler:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")

	// LiveData
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

	// Preference
	implementation("androidx.preference:preference-ktx:1.2.1")

	// Vico
	implementation("com.patrykandpatrick.vico:core:1.13.1")
	implementation("com.patrykandpatrick.vico:views:1.13.1")

	// Test implementations
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}