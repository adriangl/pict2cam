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
@file:Suppress("StringLiteralDuplication", "MagicNumber")

package com.adriangl.pict2cam.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adriangl.pict2cam.R
import com.adriangl.pict2cam.ui.extensions.toAnnotatedString
import com.adriangl.pict2cam.ui.theme.Pict2CamTheme
import com.adriangl.pict2cam.ui.theme.Pict2CamTypography
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/**
 * Data class that defines a page in the onboarding screen.
 */
data class OnboardingPageInfo(val title: AnnotatedString,
                              val description: AnnotatedString? = null,
                              val image: Painter? = null,
                              val content: @Composable ((Modifier) -> Unit)? = null,
                              val backgroundColor: Color) {
    constructor(title: String,
                description: String? = null,
                image: Painter? = null,
                content: @Composable ((Modifier) -> Unit)? = null,
                backgroundColor: Color) : this(
                    title = title.toAnnotatedString(),
                    description = description?.toAnnotatedString(),
                    image = image,
                    content = content,
                    backgroundColor = backgroundColor
                )
}

@Composable
@Suppress("UndocumentedPublicFunction")
fun OnboardingPage(onboardingPageInfo: OnboardingPageInfo) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = onboardingPageInfo.backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = onboardingPageInfo.title,
                style = Pict2CamTypography.h5.copy(fontWeight = FontWeight.SemiBold, color = Color.White),
                textAlign = TextAlign.Center
            )
        }

        if (onboardingPageInfo.image != null) {
            Image(
                painter = onboardingPageInfo.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(5f)
                    .padding(16.dp)
            )
        } else if (onboardingPageInfo.content != null) {
            onboardingPageInfo.content?.invoke(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(5f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinkClickableText(
                text = onboardingPageInfo.description ?: AnnotatedString(""),
                style = Pict2CamTypography.subtitle1.copy(color = Color.White),
                textAlign = TextAlign.Center
            )
        }

        // Add empty space so content flows properly and leaves space for the navigation elements
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
@OptIn(ExperimentalPagerApi::class)
@Suppress("UndocumentedPublicFunction")
fun OnboardingPager(modifier: Modifier = Modifier,
                    onboardingPages: List<OnboardingPageInfo>,
                    onExitClick: () -> Unit = {}) {
    val pagerState = rememberPagerState(0)
    val coroutineScope = rememberCoroutineScope()

    fun isLastPage() = pagerState.currentPage == onboardingPages.size - 1

    Box(modifier = modifier) {
        HorizontalPager(state = pagerState, count = onboardingPages.size) { page ->
            OnboardingPage(onboardingPages[page])
        }

        if (onboardingPages.size > 1) {
            HorizontalPagerIndicator(
                activeColor = Color.White,
                inactiveColor = Color.White.copy(alpha = 0.5f),
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(bottom = 16.dp, end = 16.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.3f))
                .align(Alignment.BottomEnd)
                .clickable {
                    if (isLastPage()) {
                        onExitClick()
                    } else {
                        coroutineScope.launch {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.padding(12.dp),
                tint = Color.White,
                imageVector = if (isLastPage()) {
                    Icons.Default.Check
                } else {
                    Icons.Default.ArrowForward
                },
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
@Suppress("UndocumentedPublicFunction")
fun OnboardingScreenPreview() {
    Pict2CamTheme {
        OnboardingPage(
            OnboardingPageInfo(
                title = "This is my screen!",
                image = painterResource(id = R.drawable.ic_onboarding_app_logo),
                backgroundColor = Color.Red,
                description = "This is the description!"
            )
        )
    }
}

@Preview
@Composable
@Suppress("UndocumentedPublicFunction")
fun OnboardingPagerPreview() {
    Pict2CamTheme {
        OnboardingPager(onboardingPages = listOf(
            OnboardingPageInfo(
                title = "This is my screen!",
                image = painterResource(id = R.drawable.ic_onboarding_app_logo),
                backgroundColor = Color.Red,
                description = "This is the description!"
            )
        ))
    }
}

@Preview
@Composable
@Suppress("UndocumentedPublicFunction")
fun OnboardingPagerMultiItemPreview() {
    Pict2CamTheme {
        OnboardingPager(onboardingPages = listOf(
            OnboardingPageInfo(
                title = "This is my screen!",
                image = painterResource(id = R.drawable.ic_onboarding_app_logo),
                backgroundColor = Color.Red,
                description = "This is the description!"
            ),
            OnboardingPageInfo(
                title = "This is my screen!",
                image = painterResource(id = R.drawable.ic_onboarding_app_logo),
                backgroundColor = Color.Red,
                description = "This is the description!"
            )
        ))
    }
}
