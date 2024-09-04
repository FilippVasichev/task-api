import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("nu.studer.jooq") version "8.2"
    id("org.flywaydb.flyway") version "9.22.1"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"

    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
}

group = "zionweeds"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.testcontainers:postgresql:1.19.8")
        classpath("org.flywaydb:flyway-database-postgresql:10.10.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")

    implementation("org.postgresql:postgresql:42.3.8")
    jooqGenerator("org.postgresql:postgresql:42.3.8")

    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:1.20.1")
    testImplementation("org.testcontainers:junit-jupiter:1.20.1")

    testImplementation("io.rest-assured:rest-assured:5.3.2")
    testImplementation("io.rest-assured:kotlin-extensions:5.3.2")
    testImplementation("io.rest-assured:json-path:5.3.2")
    testImplementation("io.rest-assured:xml-path:5.3.2")
    testImplementation("org.amshove.kluent:kluent:1.73")
}

tasks.test {
    useJUnitPlatform()
}

val containerInstance: PostgreSQLContainer<Nothing> =
    PostgreSQLContainer<Nothing>("postgres:15.4").apply {
        waitingFor(Wait.forListeningPort())
        start()
    }

flyway {
    url = containerInstance.jdbcUrl
    user = containerInstance.username
    password = containerInstance.password
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = containerInstance.jdbcUrl
                    user = containerInstance.username
                    password = containerInstance.password
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        isIncludeIndexes = false
                        excludes = "flyway.*"
                    }
                    generate.apply {
                        withDaos(true)
                        withSpringAnnotations(true)
                        withKotlinNotNullPojoAttributes(true)
                        withKotlinNotNullRecordAttributes(true)
                        withKotlinNotNullInterfaceAttributes(true)
                    }
                    target.apply {
                        packageName = "com.taskApi.jooq"
                        directory = "src/main/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

ktlint {
    version.set("1.0.1")
    verbose.set(true)
    outputColorName.set("RED")
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
    }
    filter {
        exclude { it.file.absolutePath.contains("/jooq/") }
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.named("generateJooq").configure {
    dependsOn(tasks.named("flywayMigrate"))
    doLast {
        containerInstance.stop()
    }
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin", "src/main/jooq")
    }
}
