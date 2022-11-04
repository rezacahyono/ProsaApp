package com.rchyn.prosa.ui.fragments.story

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rchyn.prosa.R
import com.rchyn.prosa.components.LoadingDialog
import com.rchyn.prosa.databinding.FragmentAddStoryBinding
import com.rchyn.prosa.ui.activities.MainActivity
import com.rchyn.prosa.utils.UiText
import com.rchyn.prosa.utils.bitmapToFile
import com.rchyn.prosa.utils.reduceFileImage
import com.rchyn.prosa.utils.rotateBitmap
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding as FragmentAddStoryBinding

    private val storyViewModel: StoryViewModel by viewModels()

    private val args: AddStoryFragmentArgs by navArgs()

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var act: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(layoutInflater, container, false)
        binding.layoutMainToolbar.toolbar.apply {
            title = getString(R.string.new_story)
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutMainToolbar.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val photoFile = args.photo
        val rotate = args.rotate
        val result =
            if (args.isFromFolder)
                BitmapFactory.decodeFile(photoFile.path)
            else
                rotateBitmap(
                    BitmapFactory.decodeFile(photoFile.path),
                    rotate
                )

        val reducePhoto = reduceFileImage(result.bitmapToFile(photoFile))
        setupUploadStory(reducePhoto)
        binding.ivPhoto.setImageBitmap(result)
    }

    private fun setupUploadStory(photo: File) {
        binding.btnUploadStory.isEnabled = false

        val description = binding.edtDescription

        binding.btnUploadStory.setOnClickListener {
            loadingDialog = LoadingDialog(requireContext())
            storyViewModel.addStory(
                description.text.toString(),
                photo
            ).observe(viewLifecycleOwner) { state ->
                when {
                    state.isSuccess -> {
                        loadingDialog.dismissLoading()
                        navigateToHome()
                        val fab = act.findViewById<FloatingActionButton>(R.id.fab_add_story)
                        act.showSnackBar(
                            getString(R.string.message_success_upload_story),
                            fab
                        )
                    }
                    state.isLoading -> {
                        loadingDialog.startLoading()
                    }
                    state.isError -> {
                        loadingDialog.dismissLoading()
                        val msg = state.messageError?.let { uiText ->
                            when (uiText) {
                                is UiText.DynamicString ->
                                    uiText.value
                                is UiText.StringResource ->
                                    getString(uiText.id)
                            }
                        } ?: getString(R.string.message_failed_upload_story)
                        act.showSnackBar(msg)
                    }
                }
            }
        }

        var descriptionCorrect: Boolean

        description.doAfterTextChanged {
            val descriptionText = it.toString()
            if (descriptionText.isBlank()) {
                binding.layoutEdtDescription.apply {
                    error = getString(
                        R.string.field_cant_be_empty,
                        getString(R.string.description)
                    )
                    isErrorEnabled = true
                }
                descriptionCorrect = false
            } else {
                binding.layoutEdtDescription.apply {
                    error = null
                    isErrorEnabled = false
                }
                descriptionCorrect = true
            }
            binding.btnUploadStory.isEnabled = descriptionCorrect
        }
    }

    private fun navigateToHome() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.main_nav_graph, true)
            .setLaunchSingleTop(true)
            .build()
        findNavController().navigate(R.id.home_nav, null, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}