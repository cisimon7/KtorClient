import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val kotlinVersion = "1.4.0"
val serializationVersion = "1.0.0-RC"
val ktorVersion = "1.4.0"

plugins {
    kotlin("multiplatform") version "1.4.10"
    application
    kotlin("plugin.serialization") version "1.4.10"
}
group = "me.cisimon7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://dl.bintray.com/kotlin/ktor")
    }
    maven {
        url = uri("https://dl.bintray.com/kotlin/kotlinx")
    }
    maven {
        url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers")
    }
}
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
    }
    js {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
                implementation("io.ktor:ktor-client-core:1.4.2")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:1.4.2")
                implementation("io.ktor:ktor-html-builder:1.4.2")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

                implementation("ch.qos.logback:logback-classic:1.2.3")
                implementation("io.ktor:ktor-serialization:1.4.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.2")
                implementation("org.jetbrains:kotlin-react:16.13.1-pre.110-kotlin-1.4.10")
                implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.110-kotlin-1.4.10")
                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.110-kotlin-1.4.10")

                /*KTOR CLIENT*/
                implementation("io.ktor:ktor-client-js:1.4.2")
                implementation("io.ktor:ktor-client-core-js:1.4.2")
                implementation("io.ktor:ktor-client-json-js:1.4.2")
                implementation("io.ktor:ktor-client-serialization-js:1.4.2")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.4.1")
                implementation("io.ktor:ktor-client-mock:1.4.2")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation("io.ktor:ktor-client-mock:1.4.2")
                /*implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.0")*/
            }
        }
    }
}
application {
    mainClassName = "ServerKt"
}
tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack") {
    outputFileName = "output.js"
}
tasks.getByName<Jar>("jvmJar") {
    dependsOn(tasks.getByName("jsBrowserProductionWebpack"))
    val jsBrowserProductionWebpack = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack")
    from(File(jsBrowserProductionWebpack.destinationDirectory, jsBrowserProductionWebpack.outputFileName))
}
tasks.getByName<JavaExec>("run") {
    dependsOn(tasks.getByName<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar"))
}
