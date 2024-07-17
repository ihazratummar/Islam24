package com.hazrat.islam24.core.presentation.common

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.hazrat.islam24.ui.theme.Islam24Theme

@Composable
fun HyperLinkText(modifier: Modifier = Modifier,
              fullText: String,
              linkText: List<String>,
              linkTextColor: Color = MaterialTheme.colorScheme.primary,
              linkTextFontWeight: FontWeight = FontWeight.Normal,
              linkTextDecoration: TextDecoration = TextDecoration.Underline,
              hyperlinks: List<String>,
              fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        linkText.forEachIndexed { index, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperlinks[index],
                start = startIndex,
                end = endIndex
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ),
            start = 0,
            end = fullText.length
        )
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = annotatedString,
        onClick = {
        annotatedString.getStringAnnotations("URL", it, it)
            .firstOrNull()?.let { stringAnnotation ->
                uriHandler.openUri(stringAnnotation.item)}},
        style = TextStyle(
            color = Color.White,
            textAlign = TextAlign.Center
        )
    )
}

@Preview()
@Composable
fun HyperLinkTextPreview(){
    Islam24Theme {
        HyperLinkText(fullText = "By tapping Continue you agree to our Terms and Conditions and Privacy Policy",
        linkText = listOf("Terms and Conditions", "Privacy Policy"),
        hyperlinks = listOf("https:www.google.com", "https:www.google.com")
        )
    }
}