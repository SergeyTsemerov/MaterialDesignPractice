package ru.geekbrains.materialdesignpractice.view.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.geekbrains.materialdesignpractice.R
import ru.geekbrains.materialdesignpractice.databinding.BottomNavigationLayoutBinding
import ru.geekbrains.materialdesignpractice.view.showToastLong
import ru.geekbrains.materialdesignpractice.view.showToastShort

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.appBarDescription -> {
                    binding.navigationView.showToastShort(getString(R.string.description_of_the_POD))
                }
                R.id.appBarSettings -> {
                    binding.navigationView.showToastShort(getString(R.string.settings))
                }
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}