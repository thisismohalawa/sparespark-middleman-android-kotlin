package sparespark.middleman.core.menu

import sparespark.middleman.R
import sparespark.middleman.data.model.category.CategoryMenu

internal const val MENU_PRODUCT = 0
internal const val MENU_BRAND = 1

internal fun getCategoryMenu() = listOf(
    CategoryMenu(
        id = MENU_PRODUCT,
        titleRes = R.string.products,
    ),
    CategoryMenu(
        id = MENU_BRAND,
        titleRes = R.string.brands,
    )
)