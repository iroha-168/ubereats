package domain.restaurant.entity

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class RestaurantMenu(
    val restaurantId: String,
    val items: List<RestaurantMenuItem>,
) {
    // 商品の追加
    @OptIn(ExperimentalUuidApi::class)
    fun addItem(
        name: String,
        discountId: String?,
        description: String,
        price: Int,
    ) : Result<RestaurantMenu> {
        if(100 < name.length || name.isBlank()) {
            return Result.failure(IllegalStateException())
        }

        if(200 < description.length || description.isBlank()) {
            return Result.failure(IllegalStateException())
        }

        if(0 > price) {
            return Result.failure(IllegalStateException())
        }

        val hasSameMenuName = items.any { item ->
            item.name == name
        }
        if (hasSameMenuName) {
            return Result.failure(IllegalStateException())
        }

        return Result.success(
            copy(
                items = items.plus(
                    RestaurantMenuItem(
                        id = Uuid.random().toString(),
                        name = name,
                        discountId = discountId,
                        description = description,
                        price = price,
                        isSoldOut = false,
                    )
                )
            )
        )
    }

    // 商品の削除
    fun deleteItem(id: String): Result<RestaurantMenu> {
        return Result.success(
            copy(
                items = items.filterNot { it.id == id }
            )
        )
    }

    // 商品の状態を登録
    fun updateName(id: String, name: String): Result<RestaurantMenu> {
        if (!validateName(name)) {
            return Result.failure(IllegalStateException())
        }
        return Result.success(
            copy(
                items = items.map { menu ->
                    if (menu.id == id) {
                        menu.copy(
                            name = name
                        )
                    } else {
                        menu
                    }
                }
            )
        )
    }

    fun updateDescription(id: String, description: String): Result<RestaurantMenu> {
        if(!validateDescription(description)) {
            return Result.failure(IllegalStateException())
        }
        return Result.success(
            copy(
                items = items.map { menu ->
                    if (menu.id == id) {
                        menu.copy(
                            description = description
                        )
                    } else {
                        menu
                    }
                }
            )
        )
    }

    fun updatePrice(id: String, price: Int) : Result<RestaurantMenu> {
        if(!validatePrice(price)) {
            return Result.failure(IllegalStateException())
        }
        return Result.success(
            copy(
                items = items.map { item ->
                    if(item.id == id) {
                        item.copy(
                            price = price
                        )
                    } else {
                        item
                    }
                }
            )
        )
    }

    fun applyDiscount(id: String, discountId: String): Result<RestaurantMenu> {
        return Result.success(
            copy(
                items = items.map { menu ->
                    if(menu.id == id) {
                        menu.copy(
                            discountId = discountId
                        )
                    } else {
                        menu
                    }
                }
            )
        )
    }

    fun removeDiscount(id: String): Result<RestaurantMenu> {
        return Result.success(
            copy(
                items = items.map { item ->
                    if(item.id == id) {
                        item.copy(
                            discountId = null
                        )
                    } else {
                        item
                    }
                }
            )
        )
    }

    private fun validateName(name: String): Boolean =
        name.length in 1..<100 &&
        name.isNotBlank()

    private fun validateDescription(description: String): Boolean =
        description.length in 1..<200 &&
        description.isNotBlank()

    private fun validatePrice(price: Int): Boolean = 0 <= price
}

data class RestaurantMenuItem(
    val id: String,
    val discountId: String?,
    val name: String,
    val description: String,
    val price: Int,
    val isSoldOut: Boolean,
)
