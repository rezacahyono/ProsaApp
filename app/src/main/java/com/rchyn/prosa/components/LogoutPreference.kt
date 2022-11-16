package com.rchyn.prosa.components

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.rchyn.prosa.databinding.ItemRowLogoutBinding
import com.rchyn.prosa.model.user.User
import com.rchyn.prosa.ui.activities.MainActivity

class LogoutPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {
    private lateinit var binding: ItemRowLogoutBinding

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        binding = ItemRowLogoutBinding.bind(holder.itemView)

        binding.btnLogout.setOnClickListener {
            setupToLogout()
        }
    }

    private fun setupToLogout() {
        val act = context as MainActivity
        val user = User(
            name = "",
            token = "",
            isLogin = false
        )
        act.loginViewModel.setUserPref(user)
    }
}