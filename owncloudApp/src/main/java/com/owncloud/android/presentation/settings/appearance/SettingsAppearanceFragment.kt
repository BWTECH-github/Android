package com.owncloud.android.presentation.settings.appearance

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.owncloud.android.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsAppearanceFragment : PreferenceFragmentCompat() {
    // ViewModel
    private val appearanceViewModel by viewModel<SettingsAppearanceViewModel>()

    private var prefShowHiddenFiles: SwitchPreferenceCompat? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_appearance, rootKey)

        prefShowHiddenFiles = findPreference(PREF_SHOW_HIDDEN_FILES)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefShowHiddenFiles?.isChecked = appearanceViewModel.isHiddenFilesShown()

        initPreferenceListeners()
    }

    private fun initPreferenceListeners() {
        prefShowHiddenFiles?.setOnPreferenceChangeListener { _: Preference?, newValue: Any ->
            appearanceViewModel.setShowHiddenFiles(newValue as Boolean)
            true
        }
    }

    companion object {
        const val PREF_SHOW_HIDDEN_FILES = "show_hidden_files"
    }
}