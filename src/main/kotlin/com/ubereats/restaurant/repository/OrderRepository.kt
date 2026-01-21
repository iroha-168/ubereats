package com.ubereats.restaurant.repository

import com.ubereats.restaurant.entity.Order
import com.ubereats.restaurant.entity.OrderId

interface OrderRepository {
    fun findById(restaurantId: String, orderId: OrderId): Result<Order>

    fun create(order: Order): Result<Unit>

    fun update(order: Order): Result<Unit>

    fun delete(order: Order): Result<Unit>
}