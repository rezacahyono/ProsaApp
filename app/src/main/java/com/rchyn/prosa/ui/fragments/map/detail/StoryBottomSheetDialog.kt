package com.rchyn.prosa.ui.fragments.map.detail


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.DialogBottomStoryBinding
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.utils.convertTimeToLocal
import com.rchyn.prosa.utils.getLocationName
import com.rchyn.prosa.utils.hide
import kotlinx.coroutines.launch

class StoryBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: DialogBottomStoryBinding? = null
    private val binding get() = _binding as DialogBottomStoryBinding

    private val args: StoryBottomSheetDialogArgs by navArgs()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener { dialogInterface ->

            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                setupFullHeight(it)
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBottomStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            btnFavorite.hide()

            ivPhoto.load(story.photo) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder_image)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
}