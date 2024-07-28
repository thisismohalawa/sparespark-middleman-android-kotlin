package sparespark.middleman.model

import java.io.Serializable

data class Product(
    var creationDate: String,
    val title: String,
    val des: String,
    val imgUrl: String,
    val brandId: String,
    val price: Double,
    val activated: Boolean,
    val colors: ArrayList<IColor>,
    val related: ArrayList<IRelated>,
) : Serializable
