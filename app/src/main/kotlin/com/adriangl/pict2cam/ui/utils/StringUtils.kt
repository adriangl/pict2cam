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

package com.adriangl.pict2cam.ui.utils

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.adriangl.pict2cam.ui.components.LINK_CLICKABLE_TEXT_STRING_ANNOTATION

/**
 * Returns an AnnotatedString with links set-up to use with LinkClickableText.
 *
 * Extracted from: https://stackoverflow.com/a/65656351/9288365
 */
@Composable
@ReadOnlyComposable
fun linkifyResources(@StringRes mainTextId: Int,
                     @StringRes linkUrlId: Int,
                     @StringRes linkTextId: Int) = buildAnnotatedString {
    val mainText = stringResource(id = mainTextId)
    val linkUrl = stringResource(id = linkUrlId)
    val linkText = stringResource(id = linkTextId)

    val startIndex = mainText.indexOf(linkText)
    val endIndex = startIndex + linkText.length

    append(mainText)

    addStyle(
        style = SpanStyle(
            color = MaterialTheme.colors.secondary,
            textDecoration = TextDecoration.Underline
        ),
        start = startIndex,
        end = endIndex
    )

    // Attach a string annotation that stores a URL to the text "link"
    addStringAnnotation(
        tag = LINK_CLICKABLE_TEXT_STRING_ANNOTATION,
        annotation = linkUrl,
        start = startIndex,
        end = endIndex
    )
}
