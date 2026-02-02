package com.ubereats.domain.restaurant.repository

import com.ubereats.domain.restaurant.entity.Restaurant
import kotlin.uuid.Uuid

interface RestaurantRepository {
    fun findById(restaurantId: Uuid): Result<Restaurant>

    fun create(restaurant: Restaurant): Result<Unit>

    fun update(restaurant: Restaurant): Result<Unit>

    fun delete(restaurant: Restaurant): Result<Unit>
}