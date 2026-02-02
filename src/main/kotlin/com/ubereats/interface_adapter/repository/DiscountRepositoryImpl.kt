package com.ubereats.interface_adapter.repository

import com.ubereats.interface_adapter.schema.Discounts
import com.ubereats.domain.restaurant.entity.Discount
import com.ubereats.domain.restaurant.repository.DiscountRepository
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.uuid.Uuid

class DiscountRepositoryImpl: DiscountRepository {
    override fun findById(
        restaurantId: Uuid,
        discountId: Uuid,
    ): Result<Discount?> = runCatching {
        transaction {
            Discounts.selectAll().where{ (Discounts.id eq discountId) and (Discounts.restaurantId eq restaurantId) }.map {
                Discount(
                    id = it[Discounts.id],
                    restaurantId = it[Discounts.restaurantId],
                    name = it[Discounts.name],
                    percentage = it[Discounts.percentage]
                )
            }.first()
        }
    }

    override fun create(discount: Discount): Result<Unit> = runCatching {
        transaction {
            Discounts.insert {
                it[Discounts.id] = discount.id
                it[Discounts.restaurantId] = discount.restaurantId
                it[Discounts.name] = discount.name
                it[Discounts.percentage] = discount.percentage
            }
        }
    }

    override fun update(discount: Discount): Result<Unit> = runCatching {
        transaction {
            Discounts.update({ Discounts.id eq discount.id }) {
                it[Discounts.name] = discount.name
                it[Discounts.percentage] = discount.percentage
            }
        }
    }

    override fun delete(discount: Discount): Result<Unit> = runCatching {
        transaction {
            Discounts.deleteWhere { Discounts.id eq discount.id }
        }
    }
}