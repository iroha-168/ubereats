package com.ubereats.interface_adapter.handler

import com.ubereats.domain.restaurant.entity.Restaurant
import com.ubereats.domain.restaurant.repository.RestaurantRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable

@Serializable
data class CreateRestaurantRequestBody(val name: String, val description: String)

// curl -X POST http://localhost:8080/restaurant -d '{"name":"test","description":"test"}' -H "Content-Type: application/json"
fun Route.restaurantHandler(restaurantRepository: RestaurantRepository) {
    // レストランを作成する
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

        // TODO: レストラン情報の更新
        // 認証ありのAPI
        authenticate {
            put {
                call.respond(HttpStatusCode.OK, message = "ok")
            }
        }
    }




}