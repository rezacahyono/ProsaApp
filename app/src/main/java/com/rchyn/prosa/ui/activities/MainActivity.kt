package com.rchyn.prosa.ui.activities

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.rchyn.prosa.MainNavGraphDirections
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.ActivityMainBinding
import com.rchyn.prosa.ui.activities.boarding.BoardingActivity
import com.rchyn.prosa.ui.fragments.login.LoginViewModel
import com.rchyn.prosa.utils.margin
import com.rchyn.prosa.utils.resolveColorAttr
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val loginViewModel: LoginViewModel by viewModels()
    var theme: String? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        askCameraxPermissionGranted()
        askLocationPermissionGranted()

        if (sharedPreferences.getBoolean(getString(R.string.first), true)) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(getString(R.string.first), false)
            editor.apply()
            val intent = Intent(this, BoardingActivity::class.java)
            startActivity(intent)
            finish()
        }

        theme = sharedPreferences.getString(
            getString(R.string.theme_key),
            getString(R.string.system_theme)
        )

        val accent = sharedPreferences.getString(
            getString(R.string.accent_key),
            getString(R.string.system_accent)
        )

        when (theme) {
            getString(R.string.dark_theme) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            getString(R.string.light_theme) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        when (accent) {
            getString(R.string.peach_accent) -> setTheme(R.style.Theme_Prosa_Peach)
            getString(R.string.beige_accent) -> setTheme(R.style.Theme_Prosa_Beige)
            getString(R.string.blue_accent) -> setTheme(R.style.Theme_Prosa_Blue)
            getString(R.string.purple_accent) -> setTheme(R.style.Theme_Prosa_Purple)
            else -> setTheme(R.style.Theme_Prosa)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavMenu(navController)
        setupBottomNavVisible(navController)
        setupFabAddStory(navController)

        loginViewModel.userPref.observe(this) { isLogin ->
            val refresh = sharedPreferences.getBoolean(
                getString(R.string.refresh_key), false
            )
            if (!isLogin) {
                sharedPreferences.edit().putBoolean(getString(R.string.refresh_key), false).apply()
                navigateToLogin(navController)
            } else {
                if (!refresh) {
                    navigateToHome(navController)
                }
            }
        }
    }

    private fun setupFabAddStory(navController: NavController) {
        binding.fabAddStory.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.nav_enter_anim)
                .setExitAnim(R.anim.nav_exit_anim)
                .setPopEnterAnim(R.anim.nav_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_pop_exit_anim)
                .build()
            askCameraxPermissionGranted()
            navController.navigate(R.id.camerax_nav, null, navOptions = navOptions)
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = binding.bottomNav
        bottomNav.setupWithNavController(navController)
    }

    private fun setupBottomNavVisible(navController: NavController) {
        val bottomAppBar = binding.bottomAppBar
        val fabAddStory = binding.fabAddStory
        val bottomNav = binding.bottomNav
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.home_nav &&
                destination.id != R.id.favorite_nav &&
                destination.id != R.id.setting_nav
            ) {
                binding.fragmentContainerView.margin(bottom = 0F)
                bottomAppBar.isVisible = false
                bottomNav.isVisible = false
                fabAddStory.hide()
            } else {
                binding.fragmentContainerView.margin(bottom = 56F)
                bottomAppBar.isVisible = true
                bottomNav.isVisible = true
                fabAddStory.show()
            }
        }
    }

    private fun navigateToLogin(navController: NavController) {
        val direction = MainNavGraphDirections.actionToLoginNav()
        navController.navigate(direction)
    }

    private fun navigateToHome(navController: NavController) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.main_nav_graph, true)
            .setLaunchSingleTop(true)
            .build()
        navController.navigate(R.id.home_nav, null, navOptions)
    }

    fun showSnackBar(
        content: String,
        attachView: View? = null,
        action: (() -> Unit)? = null,
        actionText: String? = null,
    ) {
        val snackBar = Snackbar.make(binding.root, content, 5000)
        snackBar.isGestureInsetBottomIgnored = true
        if (attachView != null)
            snackBar.anchorView = attachView
        if (action != null) {
            snackBar.setActionTextColor(this.resolveColorAttr(android.R.attr.colorAccent))
            snackBar.setAction(actionText) {
                action()
            }
        }
        snackBar.show()
    }


    fun askCameraxPermissionGranted(code: Int = 10): Boolean {
        return if (checkPermission(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                code
            )
            checkPermission(Manifest.permission.CAMERA)
        } else true
    }

    fun askLocationPermissionGranted(code: Int = 20): Boolean {
        return if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                code
            )
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else true
    }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            10 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (askCameraxPermissionGranted())
                        showSnackBar(
                            getString(R.string.missing_permission_camera),
                            binding.fabAddStory
                        )
                }
            }
            20 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (askLocationPermissionGranted())
                        showSnackBar(
                            getString(R.string.missing_permission_location),
                            binding.fabAddStory
                        )
                }
            }
        }
    }
}