package interface_adapter.repository

import domain.restaurant.entity.Order
import domain.restaurant.entity.OrderId
import domain.restaurant.repository.OrderRepository

class OrderRepositoryImpl: OrderRepository {
    override fun findAll(restaurantId: String): Result<Order> {
        TODO("Not yet implemented")
    }

    override fun findById(
        restaurantId: String,
        orderId: OrderId
    ): Result<Order> {
        TODO("Not yet implemented")
    }

    override fun create(order: Order): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun update(order: Order): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun delete(order: Order): Result<Unit> {
        TODO("Not yet implemented")
    }
}