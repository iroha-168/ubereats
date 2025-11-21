package domain

import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi


class DiscountTest {
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun throwsError_WhenPercentageHas3OrMoreDecimalPlaces() {
        // given
        val name = "キャンペーン割引"
        val percentage = 0.123

        // when
        val result = Discount.create(
            name = name,
            percentage = percentage
        )

        // then
        assert(result.isFailure)
        assert(result.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun return_isSuccess_WhenPercentageHasLessThan3DecimalPlaces() {
        // given
        val name = "キャンペーン割引"
        val percentage = 0.15

        // when
        val result = Discount.create(
            name = name,
            percentage = percentage
        )

        // then
        assert(result.isSuccess)
    }
}