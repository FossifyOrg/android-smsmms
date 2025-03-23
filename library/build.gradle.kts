plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.klinker.android.send_message"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 22
        targetSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        abortOnError = false
    }

    publishing {
        singleVariant("release")
    }

    useLibrary("org.apache.http.legacy")
}

dependencies {
    implementation("com.klinkerapps:logger:1.0.3")
    implementation("com.squareup.okhttp:okhttp:2.5.0")
    implementation("com.squareup.okhttp:okhttp-urlconnection:2.5.0")
}

// Maven publishing configuration
val isReleaseBuild: Boolean
    get() = !project.version.toString().contains("SNAPSHOT")

val releaseRepositoryUrl: String
    get() = if (project.hasProperty("RELEASE_REPOSITORY_URL")) project.property("RELEASE_REPOSITORY_URL") as String
            else "https://oss.sonatype.org/service/local/staging/deploy/maven2/"

val snapshotRepositoryUrl: String
    get() = if (project.hasProperty("SNAPSHOT_REPOSITORY_URL")) project.property("SNAPSHOT_REPOSITORY_URL") as String
            else "https://oss.sonatype.org/content/repositories/snapshots/"

val repositoryUsername: String
    get() = if (project.hasProperty("NEXUS_USERNAME")) project.property("NEXUS_USERNAME") as String else ""

val repositoryPassword: String
    get() = if (project.hasProperty("NEXUS_PASSWORD")) project.property("NEXUS_PASSWORD") as String else ""

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["release"])
                
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                
                pom {
                    name.set("Android SMS/MMS Library")
                    description.set("A simple library to help with SMS and MMS messaging in Android")
                    url.set("https://github.com/klinker41/android-smsmms")
                    
                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("klinker41")
                            name.set("Jacob Klinker")
                        }
                    }
                    
                    scm {
                        url.set("https://github.com/klinker41/android-smsmms")
                        connection.set("scm:git:git://github.com/klinker41/android-smsmms.git")
                        developerConnection.set("scm:git:git@github.com:klinker41/android-smsmms.git")
                    }
                }
            }
        }
        
        repositories {
            maven {
                url = uri(if (isReleaseBuild) releaseRepositoryUrl else snapshotRepositoryUrl)
                
                credentials {
                    username = repositoryUsername
                    password = repositoryPassword
                }
            }
        }
    }
    
    signing {
        setRequired(isReleaseBuild)
        sign(publishing.publications["mavenJava"])
    }
}