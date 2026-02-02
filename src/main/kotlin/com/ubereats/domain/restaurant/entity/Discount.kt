package com.ubereats.domain.restaurant.entity

import java.math.BigDecimal
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Discount(
    val id: Uuid,
    val restaurantId: Uuid,
    val name: String,
    val percentage: Float,
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun create(restaurantId: Uuid, name: String, percentage: Float): Result<Discount> {
            if (name.isBlank()) {
                return Result.failure(IllegalStateException())
            }
            if (percentage < 0) {
                return Result.failure(IllegalStateException())
            }
            if (BigDecimal.valueOf(percentage.toDouble()).stripTrailingZeros().scale() > 2) {
                return Result.failure(IllegalStateException())
            }
            return Result.success(
                Discount(
                    id = Uuid.random(),
                    restaurantId = restaurantId,
                    name = name,
                    percentage = percentage,
                )
            )
        }
    }

    fun delete(id: Uuid): Result<Discount> {
        return Result.success(
            Discount(
                id = id,
                restaurantId = restaurantId,
                name = name,
                percentage = percentage,
            )
        )
    }
}
