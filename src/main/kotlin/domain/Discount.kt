package domain

import java.math.BigDecimal
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Discount(
    val id: String,
    val name: String,
    val percentage: Double,
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun create(name: String, percentage: Double): Result<Discount> {
            if (name.isBlank()) {
                return Result.failure(IllegalStateException())
            }
            if (percentage < 0) {
                return Result.failure(IllegalStateException())
            }
            if (BigDecimal.valueOf(percentage).stripTrailingZeros().scale() > 2) {
                return Result.failure(IllegalStateException())
            }
            return Result.success(
                Discount(
                    id = Uuid.random().toString(),
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
                name = name,
                percentage = percentage,
            )
        )
    }
}
