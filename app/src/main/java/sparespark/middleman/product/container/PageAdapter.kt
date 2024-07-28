package sparespark.middleman.product.container

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sparespark.middleman.brandlist.BrandListView
import sparespark.middleman.product.productlist.ProductListView

class PageAdapter(fm: FragmentManager, private val numOfTabs: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProductListView()
            1 -> BrandListView()
            else -> ProductListView()
        }
    }

    override fun getCount(): Int {
        return numOfTabs
    }
}