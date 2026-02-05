package com.ubereats

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.ubereats.interface_adapter.handler.restaurantHandler
import com.ubereats.interface_adapter.repository.RestaurantRepositoryImpl
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.bearer
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.slf4j.event.Level
import java.io.File

// ktor dev で動かせる。hot reloadしてくれたり、ログをいい感じに出してくれたりするので、intellijのRunのガターアイコンから実行するのとはまた異なる
fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging) {
        level = Level.INFO
    }
    val serviceAccountKey = environment.classLoader.getResourceAsStream("credentials/service-account-key.json")
    val options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccountKey)).build()
    FirebaseApp.initializeApp(options)
    install(Authentication) {
        bearer {
            realm = "Access to the '/' path"
            authenticate { tokenCredential ->
                println("token: ${tokenCredential.token}")
                FirebaseAuth.getInstance().verifyIdToken(tokenCredential.token)
            }
        }
    }
    val user = FirebaseAuth.getInstance().getUser("kFECJG6Er9SsYhq3KZ39Vcs2Fie2")
    println("user: ${user.email}")
    connectAndMigration()
    val restaurantRepository = RestaurantRepositoryImpl()
    routing {
        restaurantHandler(restaurantRepository)
    }
}

private fun connectAndMigration() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5444/ubereats",
        user = "postgres",
        password = "postgres",
    )
}