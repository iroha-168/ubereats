package domain.restaurant.entity

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// レストラン側が管理する、いろんなユーザーからの注文の単位
//data class OrderAggregate(
//    val restaurantId: String,
//    val orders: List<Order>,
//) {
//    // 注文が来る
//    @OptIn(ExperimentalUuidApi::class)
//    fun addOrder(
//        customerId: String,
//        deliveryAddress: DeliveryAddress,
//        orderItems: List<OrderItem>,
//        totalPrice: Int,
//    ) : Result<Pair<OrderAggregate, Order>> {
//        val order = Order(
//            id = OrderId(Uuid.random().toString()),
//            customerId = customerId,
//            deliveryAddress = deliveryAddress,
//            orderItems = orderItems,
//            totalPrice = totalPrice,
//            status = Order.OrderStatus.REQUESTED,
//        )
//
//        val orderAggregate = copy(
//            orders = orders.plus(order)
//        )
//
//        return Result.success(
//            Pair(
//                orderAggregate,
//                order
//            )
//        )
//    }
//
//    // 注文を拒否する
//    fun reject(
//        orderId: OrderId,
//    ) : Result<Pair<OrderAggregate, Order>> {
//        val rejectedOrder = orders.find { it.id == orderId } ?: return Result.failure(
//            IllegalStateException()
//        )
//
//        if(rejectedOrder.status != Order.OrderStatus.REQUESTED) {
//            return Result.failure(IllegalStateException())
//        }
//
//        return Result.success(
//            Pair(
//                copy(orders = orders.filterNot { it.id == orderId }),
//                rejectedOrder,
//            )
//        )
//    }
//
//    // TIPS: OrderAggregateを介して、accept()を呼ぶようにコードから表現したいので、このようにしている
//    fun acceptOrder(id: OrderId) : Result<Pair<OrderAggregate, Order>> {
//        val order = orders.find { it.id == id } ?: return Result.failure(IllegalStateException())
//
//        return order.accept().map { acceptedOrder ->
//            copy(orders = orders.map { order ->
//                if(order.id == id) {
//                    acceptedOrder
//                } else {
//                    order
//                }
//            }) to acceptedOrder
//        }
//    }
//
//    fun prepareOrder(id: OrderId): Result<Pair<OrderAggregate, Order>> {
//        val order = orders.find { it.id == id } ?: return Result.failure(IllegalStateException())
//
//        return order.prepare().map { preparingOrder ->
//            copy(
//                orders = orders.map { order ->
//                    if (order.id == id) {
//                        preparingOrder
//                    } else {
//                        order
//                    }
//                }
//            ) to preparingOrder
//        }
//    }
//
//    fun completeOrder(id: OrderId): Result<Pair<OrderAggregate, Order>> {
//        val order = orders.find { it.id == id } ?: return Result.failure(IllegalStateException())
//
//        return order.complete().map { completedOrder ->
//            copy(
//                orders = orders.map { order ->
//                    if(order.id == id) {
//                        completedOrder
//                    } else {
//                        order
//                    }
//                }
//            ) to completedOrder
//        }
//    }
//
//    fun cancelOrder(id: OrderId): Result<Pair<OrderAggregate, Order>> {
//        val order = orders.find { it.id == id } ?: return Result.failure(IllegalStateException())
//
//        return order.cancel().map { canceledOrder ->
//            copy(
//                orders = orders.map { order ->
//                    if(order.id == id) {
//                        canceledOrder
//                    } else {
//                        order
//                    }
//                }
//            ) to canceledOrder
//        }
//    }
//}

@JvmInline
value class OrderId(
    val value: String,
)

// 一人のユーザーの注文単位
// ラーメン1個 ＋ 半チャーハン1個 + 餃子 みたいな
data class Order(
    val id: OrderId,
    val restaurantId: String,
    val customerId: String,
    val deliveryAddress: DeliveryAddress,
    val orderItems: List<OrderItem>,
    val totalPrice: Int,
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