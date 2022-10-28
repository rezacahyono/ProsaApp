package com.rchyn.prosa.ui.fragments.slide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rchyn.prosa.databinding.FragmentSlideBinding
import com.rchyn.prosa.utils.Constant.DESCRIPTION_BOARD
import com.rchyn.prosa.utils.Constant.IMAGE_BOARD
import com.rchyn.prosa.utils.Constant.TITLE_BOARD


class SlideFragment : Fragment() {

    private var _binding: FragmentSlideBinding? = null
    private val binding get() = _binding as FragmentSlideBinding

    private var imageBoard: Int = 0
    private var titleBoard: Int = 0
    private var descriptionBoard: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageBoard = it.getInt(IMAGE_BOARD)
            titleBoard = it.getInt(TITLE_BOARD)
            descriptionBoard = it.getInt(DESCRIPTION_BOARD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupDisplayBoard(
            imageBoard, titleBoard,
            descriptionBoard
        )
    }

    private fun setupDisplayBoard(
        imageBoard: Int, titleBoard: Int,
        descriptionBoard: Int
    ) {
        binding.apply {
            ivImageBoard.setImageResource(imageBoard)
            tvTitleBoard.text = getString(titleBoard)
            tvDescriptionBoard.text = getString(descriptionBoard)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(
            @DrawableRes imageBoard: Int,
            @StringRes titleBoard: Int,
            @StringRes descriptionBoard: Int
        ) = SlideFragment().apply {
            arguments = bundleOf(
                IMAGE_BOARD to imageBoard,
                TITLE_BOARD to titleBoard,
                DESCRIPTION_BOARD to descriptionBoard
            )
        }
    }

}