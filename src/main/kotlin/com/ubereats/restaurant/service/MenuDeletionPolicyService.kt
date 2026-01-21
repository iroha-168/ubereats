package com.ubereats.restaurant.service

import com.ubereats.restaurant.entity.Order

class MenuDeletionPolicyService {
    // メニュー削除した時に、渡されてきたメニューidで、Order DBを検索して、cancel以外のものが返ってくる（これはドメインサービスの外でやる）
    // cancel以外のステータスのものが一個でもあれば、そのメニューは削除できない
    operator fun invoke(orders: List<Order>): Result<Unit> {
        return if (!orders.isEmpty()) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(Unit)
        }
    }
}