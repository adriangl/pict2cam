/*
 * Copyright 2021 Adrián García
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

package com.adriangl.pict2cam.onboarding

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adriangl.pict2cam.R
import com.adriangl.pict2cam.extensions.openWebsite
import com.adriangl.pict2cam.ui.components.OnboardingPageInfo
import com.adriangl.pict2cam.ui.components.OnboardingPager
import com.adriangl.pict2cam.ui.extensions.toAnnotatedString
import com.adriangl.pict2cam.ui.theme.Pict2CamTheme
import com.adriangl.pict2cam.ui.theme.Pict2CamTypography
import com.adriangl.pict2cam.ui.utils.linkifyResources

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun OnboardingCreditsCard(title: String,
                          description: String,
                          url: String? = null) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
       .padding(vertical = 8.dp)
       .clickable {
                url?.let {
                    context.openWebsite(it)
                }
            },
        elevation = 8.dp,
        backgroundColor = colorResource(id = R.color.onboarding_credits_card_background_color)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, style = Pict2CamTypography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = Pict2CamTypography.body2)
        }
    }
}

@Composable
fun OnboardingScreen(onExitClick: () -> Unit = {}) {
    Pict2CamTheme {
        OnboardingPager(
            onboardingPages =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // We'll display an incompatibility screen for Android 11+
                listOf(OnboardingPageInfo(
                    title = stringResource(id = R.string.onboarding_android_11_title).toAnnotatedString(),
                    description = linkifyResources(
                        mainTextId = R.string.onboarding_android_11_description,
                        linkUrlId = R.string.onboarding_android_11_link_url,
                        linkTextId = R.string.onboarding_android_11_link_text
                    ),
                    backgroundColor = colorResource(id = R.color.onboarding_android_11_background_color),
                    image = painterResource(id = R.drawable.ic_onboarding_android_11)
                ))
            } else {
                listOf(
                    OnboardingPageInfo(
                        title = stringResource(id = R.string.onboarding_welcome_title),
                        description = stringResource(id = R.string.onboarding_welcome_description),
                        backgroundColor = colorResource(id = R.color.onboarding_welcome_background_color),
                        image = painterResource(id = R.drawable.ic_onboarding_app_logo),
                    ),
                    OnboardingPageInfo(
                        title = stringResource(id = R.string.onboarding_pick_intent_title),
                        description = stringResource(id = R.string.onboarding_pick_intent_description),
                        backgroundColor = colorResource(id = R.color.onboarding_pick_intent_background_color),
                        image = painterResource(id = R.drawable.ic_onboarding_select_image),
                    ),
                    OnboardingPageInfo(
                        title = stringResource(id = R.string.onboarding_crop_image_title),
                        description = stringResource(id = R.string.onboarding_crop_image_description),
                        backgroundColor = colorResource(id = R.color.onboarding_crop_image_background_color),
                        image = painterResource(id = R.drawable.ic_onboarding_crop_image),
                    ),
                    OnboardingPageInfo(
                        title = stringResource(id = R.string.onboarding_instant_messaging_apps_title),
                        description = stringResource(id = R.string.onboarding_instant_messaging_apps_description),
                        backgroundColor =
                            colorResource(id = R.color.onboarding_instant_messaging_apps_background_color),
                        image = painterResource(id = R.drawable.ic_onboarding_messaging_apps)
                    ),
                    OnboardingPageInfo(
                        title = stringResource(id = R.string.onboarding_credits_title),
                        backgroundColor = colorResource(id = R.color.onboarding_credits_background_color),
                        content = {
                            Column(modifier = it) {
                                OnboardingCreditsCard(
                                    title = stringResource(id = R.string.android_image_cropper_title),
                                    description = stringResource(id = R.string.android_image_cropper_description),
                                    url = stringResource(id = R.string.android_image_cropper_link_url)
                                )
                                OnboardingCreditsCard(
                                    title = stringResource(id = R.string.freevector_title),
                                    description = stringResource(id = R.string.freevector_description),
                                    url = stringResource(id = R.string.freevector_link_url)
                                )
                            }
                        },
                        description = stringResource(R.string.onboarding_credits_description)
                    )
                )
            },
            onExitClick = onExitClick
        )
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
