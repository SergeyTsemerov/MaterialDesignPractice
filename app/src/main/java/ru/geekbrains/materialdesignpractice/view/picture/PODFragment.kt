package ru.geekbrains.materialdesignpractice.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.geekbrains.materialdesignpractice.R
import ru.geekbrains.materialdesignpractice.databinding.FragmentMainBinding
import ru.geekbrains.materialdesignpractice.view.showSnackBar
import ru.geekbrains.materialdesignpractice.view.showToast
import ru.geekbrains.materialdesignpractice.viewmodel.PODData
import ru.geekbrains.materialdesignpractice.viewmodel.PODViewModel

class PODFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.sendServerRequest()
        binding.inputLayout.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            }
            startActivity(intent)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetInclude.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_DRAGGING
    }

    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Error -> {
                binding.main.showSnackBar(getString(R.string.Error))
            }
            is PODData.Loading -> {
                binding.main.showToast(getString(R.string.Loading))
            }
            is PODData.Success -> {
                binding.imageView.load(data.serverResponseData.url) {
                    error(R.drawable.ic_load_error_vector)
                }
                binding.bottomSheetInclude.textView.text = data.serverResponseData.explanation
            }
        }
    }

    companion object {
        fun newInstance() = PODFragment()
    }
}