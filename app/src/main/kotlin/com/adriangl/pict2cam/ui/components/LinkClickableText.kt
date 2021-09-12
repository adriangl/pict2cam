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

package com.adriangl.pict2cam.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit

const val LINK_CLICKABLE_TEXT_STRING_ANNOTATION = "url"

/**
 * Composable function that enables URLs in text strings with "url" annotations.
 *
 * Use LINK_CLICKABLE_TEXT_STRING_ANNOTATION to annotate your AnnotatedStrings.
 */
@Composable
fun LinkClickableText(text: AnnotatedString,
                      modifier: Modifier = Modifier,
                      color: Color = Color.Unspecified,
                      fontSize: TextUnit = TextUnit.Unspecified,
                      fontStyle: FontStyle? = null,
                      fontWeight: FontWeight? = null,
                      fontFamily: FontFamily? = null,
                      letterSpacing: TextUnit = TextUnit.Unspecified,
                      textDecoration: TextDecoration? = null,
                      textAlign: TextAlign? = null,
                      lineHeight: TextUnit = TextUnit.Unspecified,
                      style: TextStyle = LocalTextStyle.current) {
    val uriHandler = LocalUriHandler.current

    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
        }
    }

    val mergedStyle = style.merge(
        TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        )
    )

    ClickableText(
        modifier = modifier,
        text = text,
        style = mergedStyle,
        onClick = {
            text
                .getStringAnnotations(LINK_CLICKABLE_TEXT_STRING_ANNOTATION, it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}
