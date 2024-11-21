package sparespark.middleman.data.repository

import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.brand.Brand

interface BrandRepository {
    suspend fun getBrands(localOnly: Unit? = null): Result<Exception, List<Brand>>
    suspend fun getBrandById(brandId: String): Result<Exception, Brand>
}