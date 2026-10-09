package com.owncloud.android.presentation.settings.appearance

import androidx.lifecycle.ViewModel
import com.owncloud.android.data.providers.SharedPreferencesProvider
import com.owncloud.android.presentation.settings.advanced.SettingsAdvancedFragment.Companion.PREF_SHOW_HIDDEN_FILES

public class SettingsAppearanceViewModel(
    private val preferencesProvider: SharedPreferencesProvider,
): ViewModel() {
    fun isHiddenFilesShown(): Boolean =
        preferencesProvider.getBoolean(PREF_SHOW_HIDDEN_FILES, false)

    fun setShowHiddenFiles(hide: Boolean) {
        preferencesProvider.putBoolean(PREF_SHOW_HIDDEN_FILES, hide)
    }
}
