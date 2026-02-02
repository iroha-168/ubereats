package com.ubereats.domain.restaurant.entity

import kotlin.uuid.Uuid

@JvmInline
value class OrderId(
    val value: String,
)

// 一人のユーザーの注文単位
// ラーメン1個 ＋ 半チャーハン1個 + 餃子 みたいな
data class Order(
    val id: OrderId,
    val restaurantId: Uuid,
    val customerId: String,
    val deliveryAddress: DeliveryAddress,
    val orderItems: List<OrderItem>,
//    val totalPrice: Int,
    val status: OrderStatus,
) {
    internal fun accept(): Result<Order> {
        return if (status == OrderStatus.REQUESTED) {
            Result.success(
                copy(
                    status = OrderStatus.ACCEPTED
                )
            )
        } else {
            Result.failure(IllegalStateException())
        }
    }

    internal fun prepare(): Result<Order> {
        return if(status == OrderStatus.ACCEPTED) {
            Result.success(
                copy(
                    status = OrderStatus.PREPARING
                )
            )
        } else {
            Result.failure(IllegalStateException())
        }
    }

    internal fun complete(): Result<Order> {
        return if(status == OrderStatus.PREPARING) {
            Result.success(
                copy(
                    status = OrderStatus.COMPLETED
                )
            )
        } else {
            Result.failure(IllegalStateException())
        }
    }

    internal fun cancel(): Result<Order> {
        return if(status == OrderStatus.REQUESTED || status == OrderStatus.ACCEPTED) {
            Result.success(
                copy(
                    status = OrderStatus.CANCEL
                )
            )
        } else {
            Result.failure(IllegalStateException())
        }
    }

    enum class OrderStatus {
        REQUESTED, // 注文が来ただけでレストラン側が受け付けていない状態
        ACCEPTED,
        PREPARING,
        COMPLETED,
        CANCEL,
    }
}

data class DeliveryAddress(
    val postalCode: String,
    val prefecture: String,
    val city: String,
    val street: String,
    val building: String,
)

data class OrderItem(
    val menuItemId: String,
    val quantity: Int,
)