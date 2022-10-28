package com.rchyn.prosa.ui.activities.boarding

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rchyn.prosa.ui.fragments.slide.SlideFragment

class BoardingPageAdapter(
    act: AppCompatActivity,
    private val slides: List<SlideFragment> = emptyList()
) : FragmentStateAdapter(act) {
    override fun getItemCount() = slides.size

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        return slides.forEachIndexed { index, slideFragment ->
            if (position == index) {
                fragment = slideFragment
            }
        }.run {
            fragment as Fragment
        }
    }
}