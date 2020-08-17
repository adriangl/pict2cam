/*
 * Copyright 2020 Adrián García
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adriangl.pict2cam.welcome

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adriangl.pict2cam.R
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

/**
 * Main activity class that the user will see when opening the launcher icon.
 *
 * The activity explains how to use the app and adds a credits screen with references to used
 * open-source libraries and resources.
 */
class WelcomeActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make sure you don't call setContentView!

        setTransformer(AppIntroPageTransformerType.Fade)
        isSkipButtonEnabled = false
        setImmersiveMode()

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.onboarding_step_1_title),
            description = getString(R.string.onboarding_step_1_description),
            backgroundColor = ContextCompat.getColor(this, R.color.welcome_step_1_background_color),
            titleTypefaceFontRes = R.font.opensans_semibold,
            descriptionTypefaceFontRes = R.font.opensans,
            imageDrawable = R.drawable.ic_onboarding_app_logo
        ))

        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.onboarding_step_2_title),
            description = getString(R.string.onboarding_step_2_description),
            backgroundColor = ContextCompat.getColor(this, R.color.welcome_step_2_background_color),
            titleTypefaceFontRes = R.font.opensans_semibold,
            descriptionTypefaceFontRes = R.font.opensans,
            imageDrawable = R.drawable.ic_onboarding_select_image
        ))

        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.onboarding_step_3_title),
            description = getString(R.string.onboarding_step_3_description),
            backgroundColor = ContextCompat.getColor(this, R.color.welcome_step_3_background_color),
            titleTypefaceFontRes = R.font.opensans_semibold,
            descriptionTypefaceFontRes = R.font.opensans,
            imageDrawable = R.drawable.ic_onboarding_crop_image
        ))

        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.onboarding_step_4_title),
            description = getString(R.string.onboarding_step_4_description),
            backgroundColor = ContextCompat.getColor(this, R.color.welcome_step_4_background_color),
            titleTypefaceFontRes = R.font.opensans_semibold,
            descriptionTypefaceFontRes = R.font.opensans,
            imageDrawable = R.drawable.ic_onboarding_messaging_apps
        ))

        addSlide(WelcomeCreditsFragment.newInstance())
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }
}
