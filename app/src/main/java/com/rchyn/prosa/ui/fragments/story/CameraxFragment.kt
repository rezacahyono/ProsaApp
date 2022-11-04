package com.rchyn.prosa.ui.fragments.story

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.FragmentCameraxBinding
import com.rchyn.prosa.ui.activities.MainActivity
import com.rchyn.prosa.utils.createFile
import com.rchyn.prosa.utils.uriToFile
import java.io.File


class CameraxFragment : Fragment() {

    private var _binding: FragmentCameraxBinding? = null
    private val binding get() = _binding as FragmentCameraxBinding

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var resultLauncher: ActivityResultLauncher<String>

    private lateinit var act: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    val photoFile = uriToFile(uri, requireContext())
                    navigateToPreviewPhoto(photo = photoFile, rotate = true, isFromFolder = true)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraxBinding.inflate(layoutInflater, container, false)
        binding.toolbar.apply {
            title = getString(R.string.new_story)
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
            setNavigationIconTint(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act.askCameraxPermissionGranted()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        setupCamera()

        binding.apply {
            btnFlipCamera.setOnClickListener {
                setupFlipCamera()
            }
            btnOpenFolderImage.setOnClickListener {
                setupOpenFolderImage()
            }
            btnCapture.setOnClickListener {
                takePhoto()
            }
        }
    }

    private fun setupOpenFolderImage() {
        resultLauncher.launch("image/*")
    }

    private fun setupFlipCamera() {
        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
        setupCamera()
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraViewer.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                act.showSnackBar(getString(R.string.text_message_error_camera))
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(act.application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            onImageSaveCallback(photoFile)
        )
    }

    private fun onImageSaveCallback(photo: File): ImageCapture.OnImageSavedCallback {
        return object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                navigateToPreviewPhoto(photo, cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
            }

            override fun onError(exception: ImageCaptureException) {
                act.showSnackBar(getString(R.string.text_message_error_camera))
            }

        }
    }

    internal fun navigateToPreviewPhoto(
        photo: File,
        rotate: Boolean,
        isFromFolder: Boolean = false
    ) {
        val direction =
            CameraxFragmentDirections.actionCameraxNavToPreviewStoryNav(
                photo,
                rotate,
                isFromFolder
            )
        findNavController().navigate(direction)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}