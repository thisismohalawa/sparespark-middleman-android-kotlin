package sparespark.middleman.core.menu

import sparespark.middleman.R
import sparespark.middleman.data.model.ProfileMenu

internal const val MENU_AUTH = 6
internal const val MENU_HISTORY = 1
internal const val MENU_CACHE = 2

internal fun menuList(userExist: Boolean) = listOf(
    ProfileMenu(
        id = MENU_HISTORY,
        title = R.string.history,
        des = R.string.history_summary
    ),
    ProfileMenu(
        id = MENU_CACHE,
        title = R.string.cache,
        des = R.string.update_summary
    ),
    ProfileMenu(
        id = MENU_AUTH,
        title = if (userExist) R.string.logout else R.string.login,
        des = R.string.login_status,
        isNav = false
    )
)