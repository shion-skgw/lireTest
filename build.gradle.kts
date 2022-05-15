import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

group = "com.skgw"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

plugins {
    id("org.springframework.boot") version "2.6.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.sangupta:jopensurf:1.0.0")
    implementation("commons-io:commons-io:2.5")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.lucene:lucene-analyzers-common:6.3.0")
    implementation("org.apache.lucene:lucene-core:6.3.0")
    implementation("org.apache.lucene:lucene-queryparser:6.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

sourceSets {
    main {
        resources.exclude("environments", "static", "templates")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


/* ----- 環境対応 ----- */

/**
 * 環境定義
 */
enum class Environment(val id: String) {
    /** ローカル環境 */
    LOCAL("local"),
    /** 検証環境 */
    DEVELOP("develop"),
    /** 本番環境 */
    PRODUCT("product"),
}

/**
 * 指定した環境に対応する application.properties に置き換える
 *
 * @param env Environment
 */
fun replaceApplicationProperties(env: Environment) {
    // 置き換え対象ファイル
    val envDir: String = "${projectDir.absolutePath}/src/main/resources/environments"
    val envFile: Path = Paths.get("${envDir}/application-${env.id}.properties")

    // 存在チェック
    if (!Files.exists(envFile)) {
        throw GradleException("環境ファイルが存在しない ${envFile.toAbsolutePath()}")
    }

    // application.properties
    val targetFile: Path = Paths.get("${projectDir.absolutePath}/src/main/resources/application.properties")

    // コピーする
    Files.copy(envFile, targetFile, StandardCopyOption.REPLACE_EXISTING)

    // 実行結果出力
    println("application.properties の置き換え完了")
    println("    src : ${envFile.toAbsolutePath()}")
    println("    dist: ${targetFile.toAbsolutePath()}")
}

tasks.register("changeLocal") {
    doFirst {
        replaceApplicationProperties(Environment.LOCAL)
    }
}

tasks.register("changeDevelop") {
    doFirst {
        replaceApplicationProperties(Environment.DEVELOP)
    }
}

tasks.register("changeProduct") {
    doFirst {
        replaceApplicationProperties(Environment.PRODUCT)
    }
}
