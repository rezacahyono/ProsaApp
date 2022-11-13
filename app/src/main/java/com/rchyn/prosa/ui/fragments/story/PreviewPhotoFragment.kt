package com.rchyn.prosa.ui.fragments.story

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.FragmentPreviewPhotoBinding
import com.rchyn.prosa.utils.bitmapToFile
import com.rchyn.prosa.utils.reduceFileImage
import com.rchyn.prosa.utils.rotateBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class PreviewPhotoFragment : Fragment() {

    private var _binding: FragmentPreviewPhotoBinding? = null
    private val binding get() = _binding as FragmentPreviewPhotoBinding

    private val args: PreviewPhotoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewPhotoBinding.inflate(layoutInflater, container, false)
        binding.layoutMainToolbar.toolbar.apply {
            title = getString(R.string.preview_photo)
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutMainToolbar.toolbar.setNavigationOnClickListener {
            navigateToBack()
        }

        val photoFile = args.photo
        val rotate = args.rotate
        val isFromFolder = args.isFromFolder

        val photo = if (isFromFolder) {
            BitmapFactory.decodeFile(photoFile.path)
        } else rotateBitmap(
            BitmapFactory.decodeFile(photoFile.path),
            rotate
        )

        lifecycleScope.launch {
            val deferred = this.async(Dispatchers.Default) {
                return@async reduceFileImage(photo.bitmapToFile(photoFile))
            }
            val reducePhoto = deferred.await()
            binding.btnAccepted.setOnClickListener {
                navigateToAddStory(reducePhoto)
            }
        }

        binding.apply {
            ivPhotoPreview.setImageBitmap(photo)
            btnCancel.setOnClickListener {
                navigateToBack()
            }
        }

    }

    private fun navigateToBack() {
        findNavController().navigateUp()
    }

    private fun navigateToAddStory(photo: File) {
        val direction = PreviewPhotoFragmentDirections.actionPreviewPhotoNavToAddStoryNav(photo)
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}