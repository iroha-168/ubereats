package com.ubereats.restaurant.service

import com.ubereats.restaurant.entity.RestaurantMenuItem

class DiscountDeletionPolicyService {
    operator fun invoke(menuList: List<RestaurantMenuItem>): Result<Unit> {
        return if (!menuList.isEmpty()) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(Unit)
        }
    }
}