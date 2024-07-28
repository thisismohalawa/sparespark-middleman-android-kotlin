package sparespark.middleman.model


data class Brand(
    var creationDate: String,
    val name: String,
    val title: String,
    val imgUrl: String,
    val linkUrl: String,
    val activated: Boolean,
)
