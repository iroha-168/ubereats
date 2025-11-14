package domain

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
            // === バリデーションチェック ===
            // name 1文字以上、100文字以内
            // description 1文字以上、200文字以内
            // 空文字無し、特殊文字なし
            if (
                !validateName(name) || !validateDescription(description)
            ) {
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

        private fun validateDescription(description: String): Boolean {
            return !(200 < description.length || description.isBlank())
        }

        private fun validateName(name: String): Boolean {
            return !(100 < name.length || name.isBlank())
        }
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
