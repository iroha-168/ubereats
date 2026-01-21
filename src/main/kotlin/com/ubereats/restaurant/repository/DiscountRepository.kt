package com.ubereats.restaurant.repository

import com.ubereats.restaurant.entity.Discount

interface DiscountRepository {
    fun findById(restaurantId: String, discountId: String): Result<Discount?>

    fun create(discount: Discount): Result<Unit>

    fun update(discount: Discount): Result<Unit>

    fun delete(discount: Discount): Result<Unit>
}