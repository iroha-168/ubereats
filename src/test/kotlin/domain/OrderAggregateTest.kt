import domain.restaurant.entity.DeliveryAddress
import domain.restaurant.entity.Order
import domain.restaurant.entity.Order.OrderStatus
import domain.restaurant.entity.OrderId
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderTest {
    // === accept() ===
    @Test
    fun `REQUESTEDの場合は、accept()できる`() {
        val order = createOrder(OrderStatus.REQUESTED)

        val newOrder = order.accept().getOrThrow()

        assertEquals(
            OrderStatus.ACCEPTED,
            newOrder.status,
        )
    }

    @Test
    fun `ACCEPTEDの場合は、accept()できない`() {
        val order = createOrder(OrderStatus.ACCEPTED)

        val newOrder = order.accept()

        assert(newOrder.isFailure)
        assert(newOrder.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `PREPARINGの場合は、accept()できない`() {
        val order = createOrder(OrderStatus.PREPARING)

        val newOrder = order.accept()

        assert(newOrder.isFailure)
        assert(newOrder.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `COMPLETEDの場合は、accept()できない`() {
        val order = createOrder(OrderStatus.COMPLETED)

        val newOrder = order.accept()

        assert(newOrder.isFailure)
        assert(newOrder.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `CANCELの場合は、accept()できない`() {
        val order = createOrder(OrderStatus.CANCEL)

        val newOrder = order.accept()

        assert(newOrder.isFailure)
        assert(newOrder.exceptionOrNull() is IllegalStateException)
    }

    // === prepare() ===
    @Test
    fun `REQUESTEDの場合は、prepare()できない`() {
        val order = createOrder(OrderStatus.REQUESTED)
        val newOrder = order.prepare()

        assert(newOrder.isFailure)
        assert(newOrder.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `ACCEPTEDの場合は、prepare()できる`() {
        val order = createOrder(OrderStatus.ACCEPTED)

        val newOrder = order.prepare().getOrThrow()

        assertEquals(
            OrderStatus.PREPARING,
            newOrder.status,
        )
    }

    @Test
    fun `PREPARINGの場合は、prepare()できない`() {
        val order = createOrder(OrderStatus.PREPARING)
        val newOrder = order.prepare()

        assert(newOrder.isFailure)
        assert(newOrder.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `COMPLETEDの場合は、prepare()できない`() {
        val order = createOrder(OrderStatus.COMPLETED)
        val newOrder = order.prepare()

        assert(newOrder.isFailure)
        assert(newOrder.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `CANCELの場合は、prepare()できない`() {
        val order = createOrder(OrderStatus.CANCEL)
        val newOrder = order.prepare()

        assert(newOrder.isFailure)
        assert(newOrder.exceptionOrNull() is IllegalStateException)
    }

    // TODO: パラメタライズドテストで書き直してみる
//    @ParameterizedTest
//    @MethodSource("acceptProvider")
//    fun `hoge`(input: OrderStatus, expected: OrderStatus?) {
//
//    }
//
//    companion object {
//        fun acceptProvider() = listOf(
//            arguments(
//                OrderStatus.REQUESTED,
//                OrderStatus.ACCEPTED,
//            ),
//            arguments(
//                OrderStatus.ACCEPTED,
//                null,
//            ),
//            arguments(
//                OrderStatus.PREPARING,
//                null,
//            ),
//            arguments(
//                OrderStatus.COMPLETED,
//                null,
//            ),
//            arguments(
//                OrderStatus.CANCEL,
//                null,
//            )
//        )
//    }

    private fun createOrder(status: OrderStatus): Order {
        return Order(
            id = OrderId(value = "1"),
            customerId = "1",
            deliveryAddress = DeliveryAddress(
                "", "", "", "", ""
            ),
            orderItems = emptyList(),
            totalPrice = 100,
            status = status,
        )
    }
}