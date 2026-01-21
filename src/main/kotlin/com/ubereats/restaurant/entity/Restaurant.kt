package domain.restaurant.entity

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
) {
    // 作成
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        // memo: 外部からのアクセス時に、エラーを返すメソッドであることが判断できるように型をResultにする
        fun create(name: String, description: String): Result<Restaurant> {
            if (!validateName(name)) {
                return Result.failure(IllegalStateException())
            }
            if (!validateDescription(description)) {
                return Result.failure(IllegalStateException())
            }
            return Result.success(
                Restaurant(
                    id = Uuid.random().toString(),
                    name = name,
                    description = description,
                )
            )
        }

        private fun validateName(name: String): Boolean =
            name.length in 1..<100 && name.isNotBlank()

        private fun validateDescription(description: String): Boolean =
            description.length in 1..<200 && description.isNotBlank()
    }

    // 削除
    fun delete(): Result<Restaurant> {
        return Result.success(
            this
        )
    }

    // 更新
    fun updateName(name: String): Result<Restaurant> {
        if(!validateName(name)) {
            return Result.failure(IllegalStateException())
        }
        return Result.success(
            copy(name = name)
        )
    }

    fun updateDescription(description: String): Result<Restaurant> {
        if(!validateDescription(description)) {
            return Result.failure(IllegalStateException())
        }
        return Result.success(
            copy(description = description)
        )
    }
}
