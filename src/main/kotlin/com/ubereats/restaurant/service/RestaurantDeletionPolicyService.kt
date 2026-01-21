package com.ubereats.restaurant.service

import com.ubereats.restaurant.entity.Order

class RestaurantDeletionPolicyService {
    // TIPS: Repositoryを介して、SQLの条件式によって、CANCEL以外の注文を全て取得するってことができるから、
    // 引数のordersには、CANCEL以外の状態のorderが入ってきていると考えよう
    operator fun invoke(orders: List<Order>): Result<Unit> {
        return if (!orders.isEmpty()) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(Unit)
        }
    }
}