package com.ubereats.interface_adapter.handler

import com.ubereats.restaurant.entity.Restaurant
import com.ubereats.restaurant.repository.RestaurantRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable

@Serializable
data class CreateRestaurantRequestBody(val name: String, val description: String)

fun Route.restaurantHandler(restaurantRepository: RestaurantRepository) {
    route("/restaurant") {
        post {
            val body = call.receive<CreateRestaurantRequestBody>()
            Restaurant.create(
                name = body.name,
                description = body.description,
            ).onSuccess { restaurant ->
                restaurantRepository.create(restaurant)
                    .onSuccess {
                        call.respond(HttpStatusCode.Created)
                    }
                    .onFailure {
                        call.respond(HttpStatusCode.InternalServerError, message = it.message?:"error")
                    }
            }.onFailure {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}