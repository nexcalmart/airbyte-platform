import java.util.Properties

plugins {
  id("io.airbyte.gradle.jvm.app")
  id("io.airbyte.gradle.jvm.lib")
  id("io.airbyte.gradle.publish")
  id("io.airbyte.gradle.docker")
  kotlin("jvm")
  kotlin("kapt")
}

dependencies {
  kapt(platform(libs.micronaut.platform))
  kapt(libs.bundles.micronaut.annotation.processor)
  kapt(libs.micronaut.jaxrs.processor)

  kaptTest(platform(libs.micronaut.platform))
  kaptTest(libs.bundles.micronaut.test.annotation.processor)
  kaptTest(libs.micronaut.jaxrs.processor)

  annotationProcessor(platform(libs.micronaut.platform))
  annotationProcessor(libs.bundles.micronaut.annotation.processor)
  annotationProcessor(libs.micronaut.jaxrs.processor)

  implementation(project(":airbyte-analytics"))
  implementation(project(":airbyte-api"))
  implementation(project(":airbyte-commons"))
  implementation(project(":airbyte-config:config-models"))
  implementation(platform(libs.micronaut.platform))
  implementation(libs.cron.utils)
  implementation(libs.log4j.slf4j2.impl)
  implementation(libs.bundles.jackson)
  implementation(libs.bundles.micronaut)
  implementation(libs.bundles.micronaut.cache)
  implementation(libs.bundles.micronaut.data.jdbc)
  implementation(libs.bundles.micronaut.metrics)
  implementation(libs.micronaut.jaxrs.server)
  implementation(libs.micronaut.problem.json)
  implementation(libs.micronaut.security)
  implementation(libs.sentry.java)
  implementation(libs.swagger.annotations)
  implementation(libs.jakarta.ws.rs.api)
  implementation(libs.airbyte.protocol)

  runtimeOnly(libs.javax.databind)
  runtimeOnly(libs.snakeyaml)

  testAnnotationProcessor(platform(libs.micronaut.platform))
  testAnnotationProcessor(libs.bundles.micronaut.test.annotation.processor)
  testAnnotationProcessor(libs.micronaut.jaxrs.processor)

  testImplementation(project(":airbyte-test-utils"))
  testImplementation(libs.bundles.micronaut.test)
  testImplementation(libs.postgresql)
  testImplementation(libs.platform.testcontainers.postgresql)
  testImplementation(libs.mockwebserver)
  testImplementation(libs.mockito.inline)
  testImplementation(libs.mockk)
}

kapt {
  correctErrorTypes = true
}

val env = Properties().apply {
  load(rootProject.file(".env.dev").inputStream())
}

airbyte {
  application {
    mainClass = "io.airbyte.api.server.ApplicationKt"
    defaultJvmArgs = listOf("-XX:+ExitOnOutOfMemoryError", "-XX:MaxRAMPercentage=75.0")

    @Suppress("UNCHECKED_CAST")
    localEnvVars.putAll(env.toMutableMap() as Map<String, String>)
    localEnvVars.putAll(
      mapOf(
        "AIRBYTE_ROLE" to (System.getenv("AIRBYTE_ROLE") ?: "undefined"),
        "AIRBYTE_VERSION" to env["VERSION"].toString(),
        "MICRONAUT_ENVIRONMENTS" to "control-plane",
        "SERVICE_NAME" to project.name,
        "TRACKING_STRATEGY" to env["TRACKING_STRATEGY"].toString(),
      )
    )
  }
  docker {
    imageName = "airbyte-api-server"
  }
}

tasks.named<Test>("test") {
  environment(
    mapOf(
      "AIRBYTE_VERSION" to env["VERSION"],
      "MICRONAUT_ENVIRONMENTS" to "test",
      "SERVICE_NAME" to project.name,
    )
  )
}

// Even though Kotlin is excluded on Spotbugs, this projects)
// still runs into spotbug issues. Working theory is that)
// generated code is being picked up. Disable as a short-term fix.)
tasks.named("spotbugsMain") {
  enabled = false
}
