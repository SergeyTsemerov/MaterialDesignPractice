package ru.geekbrains.materialdesignpractice.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import ru.geekbrains.materialdesignpractice.R
import ru.geekbrains.materialdesignpractice.databinding.FragmentSatelliteBinding
import ru.geekbrains.materialdesignpractice.view.showSnackBar
import ru.geekbrains.materialdesignpractice.viewmodel.NASAData
import ru.geekbrains.materialdesignpractice.viewmodel.NASAViewModel

class SatelliteFragment : Fragment() {

    private var _binding: FragmentSatelliteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NASAViewModel by lazy {
        ViewModelProvider(this).get(NASAViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSatelliteBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.sendSatelliteImageRequest()
    }

    private fun renderData(data: NASAData) {
        when (data) {
            is NASAData.Error -> {
                binding.satelliteContainer.showSnackBar(getString(R.string.Error))
            }
            is NASAData.Loading -> {
                binding.satelliteImageView.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is NASAData.SatelliteSuccess -> {
                binding.satelliteImageView.load(data.satelliteResponseData.url) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                }
            }
        }
    }
}