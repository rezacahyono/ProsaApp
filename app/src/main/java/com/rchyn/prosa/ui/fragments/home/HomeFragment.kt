package com.rchyn.prosa.ui.fragments.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rchyn.prosa.adapter.ListStoryAdapter
import com.rchyn.prosa.adapter.LoadingStateAdapter
import com.rchyn.prosa.databinding.FragmentHomeBinding
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var listStoryAdapter: ListStoryAdapter

    private lateinit var act: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listStoryAdapter = ListStoryAdapter(
            onClickItem = { story ->
                navigateToDetailStory(story = story)
            },
            onClickItemFavorite = { story ->
                homeViewModel.setStoryFavorite(story)
            }
        )

        with(homeViewModel) {
            userPref.observe(viewLifecycleOwner) { user ->
                binding.tvUserName.text = user.name
            }

            storiesState.observe(viewLifecycleOwner) { state ->
                listStoryAdapter.submitData(lifecycle, state)
            }
        }
        setupRecyclerStory(act)

        refreshStory()

        binding.btnMap.setOnClickListener {
            if (act.askLocationPermissionGranted()) {
                navigateToMap()
            }
        }
    }

    private fun refreshStory() {
        binding.refresh.setOnRefreshListener {
            listStoryAdapter.refresh()
            binding.refresh.isRefreshing = false
            binding.recyclerStories.scrollToPosition(0)
        }
    }

    private fun setupRecyclerStory(activity: MainActivity) {
        val orientation = activity.resources.configuration.orientation
        val layoutManagers = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireContext(), 2)
        } else {
            LinearLayoutManager(requireContext())
        }
        binding.recyclerStories.apply {
            layoutManager = layoutManagers
            adapter = listStoryAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    listStoryAdapter.retry()
                }
            )
        }
    }

    private fun navigateToDetailStory(story: Story) {
        val direction = HomeFragmentDirections.actionHomeNavToDetailStoryNav(story)
        findNavController().navigate(direction)
    }

    private fun navigateToMap() {
        val direction = HomeFragmentDirections.actionHomeNavToMapNav()
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}