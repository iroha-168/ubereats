package com.ubereats.domain.restaurant.repository

import com.ubereats.domain.restaurant.entity.Discount
import kotlin.uuid.Uuid

interface DiscountRepository {
    fun findById(restaurantId: Uuid, discountId: Uuid): Result<Discount?>

    fun create(discount: Discount): Result<Unit>

    fun update(discount: Discount): Result<Unit>

    fun delete(discount: Discount): Result<Unit>
}