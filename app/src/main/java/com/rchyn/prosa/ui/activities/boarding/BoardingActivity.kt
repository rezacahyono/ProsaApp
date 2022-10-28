package com.rchyn.prosa.ui.activities.boarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.ActivityBoardingBinding
import com.rchyn.prosa.ui.activities.MainActivity
import com.rchyn.prosa.ui.fragments.slide.SlideFragment
import com.rchyn.prosa.utils.ZoomOutPageTransformer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoardingActivity : AppCompatActivity() {
    private var _binding: ActivityBoardingBinding? = null
    private val binding get() = _binding as ActivityBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUi()
        setupBoardingPage()
    }

    private fun setupBoardingPage() {
        val slideOne = SlideFragment.newInstance(
            R.drawable.board_one,
            R.string.title_board_one,
            R.string.description_board_one
        )

        val slideTwo = SlideFragment.newInstance(
            R.drawable.board_two,
            R.string.title_board_two,
            R.string.description_board_two
        )

        val listSlide: List<SlideFragment> = listOf(
            slideOne,
            slideTwo
        )

        val boardingPageAdapter = BoardingPageAdapter(this, listSlide)

        binding.apply {
            vpBoard.adapter = boardingPageAdapter
            vpBoard.setPageTransformer(ZoomOutPageTransformer())
            indicatorView.attachTo(vpBoard)
        }

        val changePage = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                navigateToMainAct(position, listSlide.size - 1)
            }
        }
        binding.vpBoard.registerOnPageChangeCallback(changePage)
    }

    internal fun navigateToMainAct(position: Int, last: Int) {
        binding.btnNext.setOnClickListener {
            if (position == last) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.vpBoard.currentItem = position + 1
                }
            }
        }
    }

    private fun hideSystemUi() {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        val statusBar = WindowInsetsCompat.Type.statusBars()
        val navBars = WindowInsetsCompat.Type.navigationBars()
        insetsController.hide(statusBar)
        insetsController.hide(navBars)
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}