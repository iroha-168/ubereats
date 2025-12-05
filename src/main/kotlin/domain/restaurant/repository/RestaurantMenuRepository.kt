package domain.restaurant.repository

import domain.restaurant.entity.RestaurantMenu

interface RestaurantMenuRepository {
    fun findAll(restaurantId: String): Result<RestaurantMenu>

    fun findById(restaurantId: String, menuId: String): Result<RestaurantMenu>

    fun create(restaurantMenu: RestaurantMenu): Result<Unit>

    fun update(restaurantMenu: RestaurantMenu): Result<Unit>

    fun delete(restaurantMenu: RestaurantMenu): Result<Unit>
}