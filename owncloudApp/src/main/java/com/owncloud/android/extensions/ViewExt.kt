/**
 * ownCloud Android client application
 *
 * @author Aitor Ballesteros Pavón
 *
 * Copyright (C) 2024 ownCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.extensions

import android.view.View
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

fun View.setAccessibilityRole(className: Class<*>? = null, roleDescription: String? = null) {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(v: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(v, info)
            className?.let { info.className = it.name }
            roleDescription?.let { info.roleDescription = it }
        }
    })
}

/**
 * Pads this view by the system bars and display cutout (e.g. punch hole camera) insets on the requested sides, on
 * top of its own padding, so its content is not drawn below them now that the app is always laid out edge-to-edge.
 * The insets are not consumed, so sibling views still receive them.
 */
fun View.applySystemBarsAndCutoutPadding(top: Boolean = false, bottom: Boolean = false, horizontal: Boolean = true) {
    val initialLeft = paddingLeft
    val initialTop = paddingTop
    val initialRight = paddingRight
    val initialBottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
        view.setPadding(
            initialLeft + if (horizontal) insets.left else 0,
            initialTop + if (top) insets.top else 0,
            initialRight + if (horizontal) insets.right else 0,
            initialBottom + if (bottom) insets.bottom else 0,
        )
        windowInsets
    }
}
