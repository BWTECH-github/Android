package com.owncloud.android.presentation.settings.appearance

import androidx.lifecycle.ViewModel
import com.owncloud.android.MainApp
import com.owncloud.android.data.providers.SharedPreferencesProvider
import com.owncloud.android.presentation.settings.advanced.SettingsAdvancedFragment.Companion.PREF_SHOW_HIDDEN_FILES
import com.owncloud.android.presentation.settings.appearance.SettingsAppearanceFragment.Companion.PREF_ENABLE_DARKMODE

public class SettingsAppearanceViewModel(
    private val preferencesProvider: SharedPreferencesProvider,
): ViewModel() {
    fun isHiddenFilesShown(): Boolean =
        preferencesProvider.getBoolean(PREF_SHOW_HIDDEN_FILES, false)

    fun setShowHiddenFiles(hide: Boolean) {
        preferencesProvider.putBoolean(PREF_SHOW_HIDDEN_FILES, hide)
    }

    fun isDarkmodeEnabled(): Boolean =
        preferencesProvider.getBoolean(PREF_ENABLE_DARKMODE, false)

    fun setDarkmodeEnabled(enabled: Boolean) {
        preferencesProvider.putBoolean(PREF_ENABLE_DARKMODE, enabled)
        MainApp.applyNightMode(enabled)
    }
}
