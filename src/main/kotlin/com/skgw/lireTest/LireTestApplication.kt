package com.skgw.lireTest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class LireTestApplication

fun main(args: Array<String>) {
    runApplication<LireTestApplication>(*args)
}
