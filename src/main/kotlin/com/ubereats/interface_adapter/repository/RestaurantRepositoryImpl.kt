package com.ubereats.interface_adapter.repository

import com.ubereats.interface_adapter.schema.Restaurants
import com.ubereats.domain.restaurant.entity.Restaurant
import com.ubereats.domain.restaurant.repository.RestaurantRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock
import kotlin.uuid.Uuid

class RestaurantRepositoryImpl: RestaurantRepository {
    override fun findById(restaurantId: Uuid): Result<Restaurant> = runCatching {
        transaction {
            Restaurants.selectAll().where { Restaurants.id eq restaurantId }.map {
                Restaurant(
                    id = it[Restaurants.id],
                    name = it[Restaurants.name],
                    description = it[Restaurants.description],
                )
            }.first()
        }
    }

    override fun create(restaurant: Restaurant): Result<Unit> = runCatching {
        transaction {
            Restaurants.insert {
                it[id] = restaurant.id
                it[name] = restaurant.name
                it[description] = restaurant.description
            }
        }
    }

    override fun update(restaurant: Restaurant): Result<Unit> = runCatching {
        transaction {
            Restaurants.update({ Restaurants.id eq restaurant.id}) {
                it[name] = restaurant.name
                it[description] = restaurant.description
                it[updatedAt] = Clock.System.now()
            }
        }
    }

    override fun delete(restaurant: Restaurant): Result<Unit> = runCatching {
        transaction {
            Restaurants.deleteWhere { Restaurants.id eq restaurant.id }
        }
    }
}