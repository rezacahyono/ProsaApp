package com.rchyn.prosa.components

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.util.AttributeSet
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.ItemRowChangeLanguageBinding

class ChangeLanguagePreference(context: Context, attrs: AttributeSet) :
    Preference(context, attrs), View.OnClickListener {

    private lateinit var binding: ItemRowChangeLanguageBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        binding = ItemRowChangeLanguageBinding.bind(holder.itemView)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

        binding.root.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        try {
            sharedPref.edit().putBoolean(
                context.getString(R.string.refresh_key), true).apply()
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }


}