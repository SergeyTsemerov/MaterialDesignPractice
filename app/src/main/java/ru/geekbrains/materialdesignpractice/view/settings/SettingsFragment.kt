package ru.geekbrains.materialdesignpractice.view.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.geekbrains.materialdesignpractice.R
import ru.geekbrains.materialdesignpractice.databinding.FragmentSettingsBinding
import ru.geekbrains.materialdesignpractice.view.MainActivity
import ru.geekbrains.materialdesignpractice.view.FirstTheme
import ru.geekbrains.materialdesignpractice.view.SecondTheme
import ru.geekbrains.materialdesignpractice.view.ThirdTheme

class SettingsFragment : Fragment() {

    private lateinit var parentActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = (context as MainActivity)
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.amazingTheme.setOnClickListener {
            parentActivity.setCurrentTheme(FirstTheme)
            parentActivity.recreate()
        }
        binding.anotherAmazingTheme.setOnClickListener {
            parentActivity.setCurrentTheme(SecondTheme)
            parentActivity.recreate()
        }
        binding.defaultTheme.setOnClickListener {
            parentActivity.setCurrentTheme(ThirdTheme)
            parentActivity.recreate()
        }

        when (parentActivity.getCurrentTheme()) {
            1 -> binding.radioGroup.check(R.id.amazingTheme)
            2 -> binding.radioGroup.check(R.id.anotherAmazingTheme)
            3 -> binding.radioGroup.check(R.id.defaultTheme)
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}