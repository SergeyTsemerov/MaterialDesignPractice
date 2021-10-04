package ru.geekbrains.materialdesignpractice.view.picture

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
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
import ru.geekbrains.materialdesignpractice.view.recycler.RecyclerActivity
import ru.geekbrains.materialdesignpractice.view.settings.SettingsFragment
import ru.geekbrains.materialdesignpractice.view.showSnackBar
import ru.geekbrains.materialdesignpractice.view.showToastLong
import ru.geekbrains.materialdesignpractice.view.showToastShort
import ru.geekbrains.materialdesignpractice.viewmodel.NASAData
import ru.geekbrains.materialdesignpractice.viewmodel.NASAViewModel

class PODFragment : Fragment() {

    private var isExpanded = false

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
        setInitialState()
        binding.fab.setOnClickListener {
            if (isMain) {
                expandFAB()
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
                collapseFAB()
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
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

    private fun setInitialState() {
        binding.transparentBackground.apply {
            alpha = 0f
        }
        binding.toDoNotesContainer.apply {
            alpha = 0f
            isClickable = false
        }
        binding.optionTwoContainer.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun expandFAB() {
        isExpanded = true
        binding.optionTwoContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.optionTwoContainer.isClickable = true
                    binding.optionTwoContainer.setOnClickListener {
                        binding.optionTwoContainer.showToastShort(getString(R.string.option_two))
                    }
                }
            })

        binding.toDoNotesContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.toDoNotesContainer.isClickable = true
                    binding.toDoNotesContainer.setOnClickListener {
                        startActivity(Intent(context, RecyclerActivity::class.java))
                    }
                }
            })

        binding.transparentBackground.animate()
            .alpha(0.9f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.transparentBackground.isClickable = true
                }
            })
    }

    private fun collapseFAB() {
        isExpanded = false
        binding.optionTwoContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.optionTwoContainer.isClickable = false
                }
            })

        binding.toDoNotesContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.toDoNotesContainer.isClickable = false
                }
            })

        binding.transparentBackground.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.transparentBackground.isClickable = false
                }
            })
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
                data.serverResponseData.explanation?.let {
                    binding.descriptionTextView.text = it
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        binding.descriptionTextView.typeface =
                            resources.getFont(R.font.x_files_font)
                    }
                    val spannable = SpannableStringBuilder(it)

                    spannable.setSpan(
                        ForegroundColorSpan(resources.getColor(R.color.red_A700)),
                        0,
                        200,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    spannable.setSpan(
                        ForegroundColorSpan(resources.getColor(R.color.colorAccent)),
                        200,
                        400,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    spannable.setSpan(object : ClickableSpan() {
                        override fun onClick(view: View) {
                            view.showToastShort("Click")
                        }
                    }, 56, 100, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.descriptionTextView.movementMethod = LinkMovementMethod.getInstance()
                    binding.descriptionTextView.text = spannable
                }
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