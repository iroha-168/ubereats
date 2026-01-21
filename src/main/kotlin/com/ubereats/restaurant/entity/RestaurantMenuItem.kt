package com.ubereats.restaurant.entity

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class RestaurantMenuItem(
    val id: String,
    val restaurantId: String,
    val discountId: String?,
    val name: String,
    val description: String,
    val price: Int,
    val isSoldOut: Boolean,
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun create(
            restaurantId: String,
            name: String,
            discountId: String?,
            description: String,
            price: Int,
        ) : Result<RestaurantMenuItem> {
            if(100 < name.length || name.isBlank()) {
                return Result.failure(IllegalStateException())
            }

            if(200 < description.length || description.isBlank()) {
                return Result.failure(IllegalStateException())
            }

            if(0 > price) {
                return Result.failure(IllegalStateException())
            }

            return Result.success(
                RestaurantMenuItem(
                    id = Uuid.random().toString(),
                    restaurantId = restaurantId,
                    name = name,
                    discountId = discountId,
                    description = description,
                    price = price,
                    isSoldOut = false,
                )
            )
        }
    }

    // 商品の削除
    fun delete(): Result<RestaurantMenuItem> {
        return Result.success(this)
    }

    // 商品の状態を登録
    fun updateName(name: String): Result<RestaurantMenuItem> {
        if (!validateName(name)) {
            return Result.failure(IllegalStateException())
        }
        return Result.success(copy(name = name))
    }

    fun updateDescription(description: String): Result<RestaurantMenuItem> {
        if(!validateDescription(description)) {
            return Result.failure(IllegalStateException())
        }
        return Result.success(copy(description = description))
    }

    fun updatePrice(price: Int) : Result<RestaurantMenuItem> {
        if(!validatePrice(price)) {
            return Result.failure(IllegalStateException())
        }
        return Result.success(copy(price = price))
    }

    fun applyDiscount(discountId: String): Result<RestaurantMenuItem> {
        return Result.success(copy(discountId = discountId))
    }

    fun removeDiscount(): Result<RestaurantMenuItem> {
        return Result.success(copy(discountId = null))
    }

    private fun validateName(name: String): Boolean =
        name.length in 1..<100 && name.isNotBlank()

    private fun validateDescription(description: String): Boolean =
        description.length in 1..<200 && description.isNotBlank()

    private fun validatePrice(price: Int): Boolean = 0 <= price
}
