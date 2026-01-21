package com.ubereats

import com.ubereats.interface_adapter.handler.restaurantHandler
import com.ubereats.interface_adapter.repository.RestaurantRepositoryImpl
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.slf4j.event.Level

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging) {
        level = Level.INFO
    }
    Database.connect(
        url = "jdbc:postgresql://localhost:5444/ubereats",
        user = "postgres",
        password = "postgres",
    )

    val restaurantRepository = RestaurantRepositoryImpl()
    routing {
        restaurantHandler(restaurantRepository)
    }
}