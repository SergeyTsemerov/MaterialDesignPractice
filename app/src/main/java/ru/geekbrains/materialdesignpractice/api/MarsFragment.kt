package ru.geekbrains.materialdesignpractice.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import ru.geekbrains.materialdesignpractice.R
import ru.geekbrains.materialdesignpractice.databinding.FragmentMarsBinding
import ru.geekbrains.materialdesignpractice.view.showSnackBar
import ru.geekbrains.materialdesignpractice.viewmodel.NASAData
import ru.geekbrains.materialdesignpractice.viewmodel.NASAViewModel

class MarsFragment : Fragment() {

    private var isExpanded = false

    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NASAViewModel by lazy {
        ViewModelProvider(this).get(NASAViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.sendMarsRoverImageRequest()
        binding.marsImageView.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                binding.marsContainer, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = binding.marsImageView.layoutParams
            params.height =
                if (isExpanded) {
                    ViewGroup.LayoutParams.MATCH_PARENT
                } else {
                    ViewGroup.LayoutParams.MATCH_PARENT
                }
            binding.marsImageView.layoutParams = params
            binding.marsImageView.scaleType =
                if (isExpanded) {
                    ImageView.ScaleType.CENTER_CROP
                } else {
                    ImageView.ScaleType.FIT_CENTER
                }
        }
    }

    private fun renderData(data: NASAData) {
        when (data) {
            is NASAData.Error -> {
                binding.marsContainer.showSnackBar(getString(R.string.Error))
            }
            is NASAData.Loading -> {
                binding.marsImageView.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is NASAData.MarsSuccess -> {
                binding.marsImageView.load(data.marsResponseData.photos.first().imgSrc) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                }
            }
        }
    }
}