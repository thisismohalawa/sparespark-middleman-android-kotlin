package sparespark.middleman.data.model.user

import sparespark.middleman.core.DEACTIVATED

data class RemoteUser(
    val uid: String? = "",
    val name: String? = "",
    val email: String? = "",
    val activated: Boolean? = DEACTIVATED
)
