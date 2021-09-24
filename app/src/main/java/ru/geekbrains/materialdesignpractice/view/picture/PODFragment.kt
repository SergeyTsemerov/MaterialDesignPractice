package ru.geekbrains.materialdesignpractice.view.picture

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import ru.geekbrains.materialdesignpractice.R
import ru.geekbrains.materialdesignpractice.api.ApiActivity
import ru.geekbrains.materialdesignpractice.databinding.FragmentMainBinding
import ru.geekbrains.materialdesignpractice.view.MainActivity
import ru.geekbrains.materialdesignpractice.view.settings.SettingsFragment
import ru.geekbrains.materialdesignpractice.view.showSnackBar
import ru.geekbrains.materialdesignpractice.view.showToastLong
import ru.geekbrains.materialdesignpractice.viewmodel.NASAData
import ru.geekbrains.materialdesignpractice.viewmodel.NASAViewModel

class PODFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NASAViewModel by lazy {
        ViewModelProvider(this).get(NASAViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        setActionBar()
        return binding.root
    }

    private var isMain = true

    private fun setActionBar() {
        (context as MainActivity).setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.sendPODServerRequest()
    }

    private fun renderData(data: NASAData) {
        when (data) {
            is NASAData.Error -> {
                binding.main.showSnackBar(getString(R.string.Error))
            }
            is NASAData.Loading -> {
                binding.imageView.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is NASAData.PODSuccess -> {
                binding.imageView.load(data.serverResponseData.url) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                }
                binding.descriptionTextView.text = data.serverResponseData.explanation
            }
        }
    }

    companion object {
        fun newInstance() = PODFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.appBarDescription -> {
                binding.main.showToastLong(binding.descriptionTextView.text.toString())
            }
            R.id.appBarSettings -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment().show(requireActivity().supportFragmentManager, "")
            }
            R.id.additionalNASAContent -> {
                startActivity(Intent(context, ApiActivity::class.java))
            }
            R.id.wikipediaSearch -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, WikipediaSearchFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}