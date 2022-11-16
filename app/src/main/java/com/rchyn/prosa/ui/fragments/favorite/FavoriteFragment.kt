package com.rchyn.prosa.ui.fragments.favorite

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rchyn.prosa.R
import com.rchyn.prosa.adapter.ListStoryFavAdapter
import com.rchyn.prosa.components.LoadingDialog
import com.rchyn.prosa.databinding.FragmentFavoriteBinding
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.ui.activities.MainActivity
import com.rchyn.prosa.utils.UiText
import com.rchyn.prosa.utils.hide
import com.rchyn.prosa.utils.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding as FragmentFavoriteBinding

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private lateinit var listStoryFavAdapter: ListStoryFavAdapter

    private lateinit var act: MainActivity
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireContext())

        listStoryFavAdapter = ListStoryFavAdapter(onClickItem = { story ->
            navigateToDetailStory(story = story)
        }, onClickItemFavorite = { story ->
            favoriteViewModel.setStoryFavorite(story = story)
        })

        favoriteViewModel.storiesFavState.observe(viewLifecycleOwner) { state ->
            when {
                state.isError -> {
                    loadingDialog.dismissLoading()
                    val msg = state.messageError?.let { uiText ->
                        when (uiText) {
                            is UiText.DynamicString ->
                                uiText.value
                            is UiText.StringResource ->
                                getString(uiText.id)
                        }
                    } ?: ""
                    act.showSnackBar(msg)
                }
                state.isLoading -> {
                    loadingDialog.startLoading()
                }
                else -> {
                    loadingDialog.dismissLoading()
                    binding.recyclerStoriesFav.run { if (state.listFavorite.isEmpty()) hide() else show() }
                    listStoryFavAdapter.submitList(state.listFavorite)
                    setPlaceholder(visibility = state.listFavorite.isEmpty())
                }
            }
        }

        setupRecyclerFavorite(act)
    }

    private fun setupRecyclerFavorite(activity: MainActivity) {
        val orientation = activity.resources.configuration.orientation
        val layoutManagers = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireContext(), 2)
        } else {
            LinearLayoutManager(requireContext())
        }
        binding.recyclerStoriesFav.apply {
            layoutManager = layoutManagers
            adapter = listStoryFavAdapter
        }
    }

    private fun setPlaceholder(
        @DrawableRes drawableRes: Int = R.drawable.ic_empty,
        @StringRes stringRes: Int = R.string.text_message_favorite_empty,
        visibility: Boolean
    ) {
        if (visibility) {
            binding.layoutState.apply {
                ivPlaceholder.setImageResource(drawableRes)
                tvPlaceholder.text = getString(stringRes)
            }
        }
        binding.layoutState.root.run { if (visibility) show() else hide() }
    }

    private fun navigateToDetailStory(story: Story) {
        val direction = FavoriteFragmentDirections.actionFavoriteNavToDetailStoryNav(story)
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}