package domain.restaurant.repository

import domain.restaurant.entity.Restaurant

interface RestaurantRepository {
    fun findById(restaurantId: String): Result<Restaurant>

    fun create(restaurant: Restaurant): Result<Unit>

    fun update(restaurant: Restaurant): Result<Unit>

    fun delete(restaurant: Restaurant): Result<Unit>
}