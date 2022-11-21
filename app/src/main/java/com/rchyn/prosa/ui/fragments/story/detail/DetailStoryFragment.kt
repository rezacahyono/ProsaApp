package com.rchyn.prosa.ui.fragments.story.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.load
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.FragmentDetailStoryBinding
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.ui.fragments.home.HomeViewModel
import com.rchyn.prosa.utils.convertTimeToLocal
import com.rchyn.prosa.utils.getLocationName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding as FragmentDetailStoryBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private val args: DetailStoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailStoryBinding.inflate(layoutInflater, container, false)
        binding.layoutMainToolbar.toolbar.apply {
            title = getString(R.string.detail)
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutMainToolbar.toolbar.setNavigationOnClickListener {
            navigateBack(findNavController())
        }

        val story = args.story
        setupDataDetail(story)
    }

    private fun setupDataDetail(story: Story) {
        binding.apply {
            tvUserName.text = story.name
            tvDate.text = story.date.convertTimeToLocal()
            if (story.lat != null && story.lon != null) {
                lifecycleScope.launch {
                    tvLocation.text = getLocationName(
                        this,
                        requireContext(),
                        story.lat,
                        story.lon
                    )
                }
            } else {
                tvLocation.isVisible = false
            }
            tvDescription.text = story.description


            btnFavorite.isChecked = story.isFavorite
            btnFavorite.apply {
                setOnClickListener {
                    homeViewModel.setStoryFavorite(story)
                }
            }
            ivPhoto.apply {
                transitionName = "iv_photo${story.id}"
                load(story.photo) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder_image)
                }
            }
        }
    }


    private fun navigateBack(navController: NavController) {
        navController.navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}