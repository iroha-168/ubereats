package com.ubereats.domain.restaurant.repository

import com.ubereats.domain.restaurant.entity.Order
import com.ubereats.domain.restaurant.entity.OrderId
import kotlin.uuid.Uuid

interface OrderRepository {
    fun findById(restaurantId: Uuid, orderId: OrderId): Result<Order>

    fun create(order: Order): Result<Unit>

    fun update(order: Order): Result<Unit>

    fun delete(order: Order): Result<Unit>
}