package com.ubereats.restaurant.repository

import com.ubereats.restaurant.entity.RestaurantMenuItem

interface RestaurantMenuItemRepository {
//    fun findByRestaurantId(restaurantId: String): Result<RestaurantMenuItem>

    fun create(restaurantMenuItem: RestaurantMenuItem): Result<Unit>

    fun update(restaurantMenuItem: RestaurantMenuItem): Result<Unit>

    fun delete(restaurantMenuItem: RestaurantMenuItem): Result<Unit>
}