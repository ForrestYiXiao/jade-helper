plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.7.0"
    kotlin("jvm") version "1.9.20-RC"
}

group = "com.zhipin.jadehelper"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    repositories {
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://oss.sonatype.org/content/repositories/releases/")
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.3")
    type.set("IU")  // Target IDE Platform

    plugins.set(listOf("com.intellij.java", "com.intellij.database"))

}

dependencies {
    implementation("org.projectlombok:lombok:1.18.22")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.6")
    implementation("com.github.jsqlparser:jsqlparser:4.7")
    // lombok 支持
    compileOnly("org.projectlombok:lombok:1.18.2")
    annotationProcessor("org.projectlombok:lombok:1.18.2")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.2")
    // 测试用例
    testImplementation("junit:junit:4.12")
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        sinceBuild.set("213")
        untilBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
