package com.ubereats.restaurant.repository

import com.ubereats.restaurant.entity.Restaurant

interface RestaurantRepository {
    fun findById(restaurantId: String): Result<Restaurant>

    fun create(restaurant: Restaurant): Result<Unit>

    fun update(restaurant: Restaurant): Result<Unit>

    fun delete(restaurant: Restaurant): Result<Unit>
}