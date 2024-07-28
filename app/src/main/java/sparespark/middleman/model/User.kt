package sparespark.middleman.model

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val activated: Boolean,
)
