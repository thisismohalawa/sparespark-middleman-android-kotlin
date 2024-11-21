package sparespark.middleman.data.model.user

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val activated: Boolean,
)
