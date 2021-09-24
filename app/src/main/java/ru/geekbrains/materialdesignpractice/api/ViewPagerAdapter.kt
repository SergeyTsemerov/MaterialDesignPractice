package ru.geekbrains.materialdesignpractice.api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val EARTH = 0
private const val MARS = 1
private const val SYSTEM = 2

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = arrayOf(SatelliteFragment(), MarsFragment(), EarthFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> fragments[EARTH]
            1 -> fragments[MARS]
            2 -> fragments[SYSTEM]
            else -> fragments[EARTH]
        }
    }
}