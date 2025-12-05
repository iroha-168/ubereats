package domain.restaurant.repository

import domain.restaurant.entity.Discount

interface DiscountRepository {
    fun findAll(restaurantId: String): Result<List<Discount>>

    fun findById(restaurantId: String, discountId: String): Result<Discount?>

    fun create(discount: Discount): Result<Unit>

    fun update(discount: Discount): Result<Unit>

    fun delete(discount: Discount): Result<Unit>
}