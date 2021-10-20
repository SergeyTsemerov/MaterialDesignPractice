package ru.geekbrains.materialdesignpractice.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import ru.geekbrains.materialdesignpractice.R
import ru.geekbrains.materialdesignpractice.databinding.FragmentWikipediaStartBinding

class WikipediaSearchFragment : Fragment() {

    private var show = false

    private var _binding: FragmentWikipediaStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWikipediaStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wikiImageButton.setOnClickListener {
            if (show) {
                hideComponents()
            } else {
                showComponents()
            }
        }
        binding.inputLayout.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            }
            startActivity(intent)
        }
    }

    private fun hideComponents() {
        show = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_wikipedia_start)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 200

        TransitionManager.beginDelayedTransition(binding.fragmentWikipediaStart, transition)
        constraintSet.applyTo(binding.fragmentWikipediaStart)
    }

    private fun showComponents() {
        show = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_wikipedia)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(binding.fragmentWikipediaStart, transition)
        constraintSet.applyTo(binding.fragmentWikipediaStart)
    }

    companion object {
        fun newInstance() = WikipediaSearchFragment()
    }
}