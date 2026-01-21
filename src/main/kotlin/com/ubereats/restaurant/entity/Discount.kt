package domain.restaurant.entity

import java.math.BigDecimal
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Discount(
    val id: String,
    val restaurantId: String,
    val name: String,
    val percentage: Float,
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun create(restaurantId: String, name: String, percentage: Float): Result<Discount> {
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
                    id = Uuid.random().toString(),
                    restaurantId = restaurantId,
                    name = name,
                    percentage = percentage,
                )
            )
        }
    }

    fun delete(id: String): Result<Discount> {
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
