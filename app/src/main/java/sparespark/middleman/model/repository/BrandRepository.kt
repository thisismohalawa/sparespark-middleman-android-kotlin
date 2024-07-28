package sparespark.middleman.model.repository

import sparespark.middleman.common.Result
import sparespark.middleman.model.Brand

interface BrandRepository {
    suspend fun getBrands(): Result<Exception, List<Brand>>
    suspend fun getLocalBrands(): Result<Exception, List<Brand>>
    suspend fun getBrandById(brandId: String): Result<Exception, Brand>
    suspend fun getBrandNameById(brandId: String): Result<Exception, String?>
}