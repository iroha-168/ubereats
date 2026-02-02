package com.ubereats.domain.restaurant.service

import com.ubereats.domain.restaurant.entity.Order

class MenuStateChangePolicyService {
    operator fun invoke(orders: List<Order>): Result<Unit> {
        return if (!orders.isEmpty()) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(Unit)
        }
    }
}