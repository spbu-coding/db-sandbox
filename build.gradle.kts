plugins {
    kotlin("jvm") version "1.4.32"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val exposedVersion: String by project
val sqliteJdbcVersion: String by project
val neo4jDriverVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))

    // SQLite
    // JDBC
    implementation("org.xerial", "sqlite-jdbc", sqliteJdbcVersion)
    // Exposed
    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)

    // Neo4j
    // Driver
    implementation("org.neo4j.driver", "neo4j-java-driver", neo4jDriverVersion)

    // Logging
    implementation("io.github.microutils", "kotlin-logging-jvm", "2.0.6")
    implementation("org.slf4j", "slf4j-simple", "1.7.29")
}
