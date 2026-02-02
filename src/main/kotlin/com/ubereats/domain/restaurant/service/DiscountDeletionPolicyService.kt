package com.ubereats.domain.restaurant.service

import com.ubereats.domain.restaurant.entity.RestaurantMenuItem

class DiscountDeletionPolicyService {
    operator fun invoke(menuList: List<RestaurantMenuItem>): Result<Unit> {
        return if (!menuList.isEmpty()) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(Unit)
        }
    }
}