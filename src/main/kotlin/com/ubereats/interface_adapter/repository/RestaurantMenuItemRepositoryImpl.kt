package com.ubereats.interface_adapter.repository

import com.ubereats.interface_adapter.schema.RestaurantMenuItems
import com.ubereats.domain.restaurant.entity.RestaurantMenuItem
import com.ubereats.domain.restaurant.repository.RestaurantMenuItemRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class RestaurantMenuItemRepositoryImpl: RestaurantMenuItemRepository {
    // TODO: クエリ（読み込み）はQueryServiceに書く
//    override fun findByRestaurantId(restaurantId: String): Result<RestaurantMenuItem> = runCatching {
//        RestaurantMenuItems.selectAll().where {
//            RestaurantMenuItems.id eq restaurantId
//        }.map {
//            RestaurantMenuItem(
//                id = it[RestaurantMenuItems.id],
//                restaurantId = it[RestaurantMenuItems.restaurantId],
//                discountId = it[RestaurantMenuItems.discountId],
//                name = it[RestaurantMenuItems.name],
//                description = it[RestaurantMenuItems.description],
//                price = it[RestaurantMenuItems.price],
//                isSoldOut = it[RestaurantMenuItems.isSoldOut]
//            )
//        }.first()
//    }

    override fun create(restaurantMenuItem: RestaurantMenuItem): Result<Unit> = runCatching {
        transaction {
            RestaurantMenuItems.insert {
                it[RestaurantMenuItems.id] = restaurantMenuItem.id
                it[RestaurantMenuItems.restaurantId] = restaurantMenuItem.restaurantId
                it[RestaurantMenuItems.discountId] = restaurantMenuItem.discountId
                it[RestaurantMenuItems.name] = restaurantMenuItem.name
                it[RestaurantMenuItems.description] = restaurantMenuItem.description
                it[RestaurantMenuItems.price] = restaurantMenuItem.price
                it[RestaurantMenuItems.isSoldOut] = restaurantMenuItem.isSoldOut
            }
        }
    }

    override fun update(restaurantMenuItem: RestaurantMenuItem): Result<Unit> = runCatching {
        transaction {
            RestaurantMenuItems.update({ RestaurantMenuItems.id eq restaurantMenuItem.id}) {
                it[RestaurantMenuItems.id] = restaurantMenuItem.id
                it[RestaurantMenuItems.restaurantId] = restaurantMenuItem.restaurantId
                it[RestaurantMenuItems.discountId] = restaurantMenuItem.discountId
                it[RestaurantMenuItems.name] = restaurantMenuItem.name
                it[RestaurantMenuItems.description] = restaurantMenuItem.description
                it[RestaurantMenuItems.price] = restaurantMenuItem.price
                it[RestaurantMenuItems.isSoldOut] = restaurantMenuItem.isSoldOut
            }
        }
    }

    override fun delete(restaurantMenuItem: RestaurantMenuItem): Result<Unit> = runCatching {
        transaction {
            RestaurantMenuItems.deleteWhere { RestaurantMenuItems.id eq restaurantMenuItem.id }
        }
    }
}