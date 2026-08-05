package com.hazrat.auth.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.hazrat.ui.R
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Login Screen matching exact user mockup using TopAppBar & LazyColumn.
 * @author Hazrat Ummar Shaikh
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onGoogleSignInClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Box(modifier = Modifier.padding(start = dimens.space12)) {
                        IconWithBackground(
                            icon = R.drawable.arrow_left,
                            onClick = onBackClick,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.space24)
                        .padding(vertical = dimens.space32),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Rounded Card with App Logo matching Dark/Light Mode
                    Box(
                        modifier = Modifier
                            .size(dimens.layoutXs)
                            .clip(RoundedCornerShape(dimens.cornerXl))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = dimens.divider,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(dimens.cornerXl)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.splash_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(dimens.layoutXs - dimens.space24)
                                .padding(dimens.space8)
                        )
                    }

                    Spacer(modifier = Modifier.height(dimens.space32))

                    // Welcome Back Title
                    Text(
                        text = stringResource(R.string.login_welcome_back),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(dimens.space12))

                    // Subtitle
                    Text(
                        text = stringResource(R.string.login_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = customColors.secondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = dimens.space8)
                    )

                    Spacer(modifier = Modifier.height(dimens.space24))

                    // Subtle Indicator (Line - Dot - Line)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.width(dimens.layoutSm)
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = dimens.space8)
                                .size(dimens.space8)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                        )
                    }

                    Spacer(modifier = Modifier.height(dimens.space32))

                    // Continue with Google Button
                    Button(
                        onClick = onGoogleSignInClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimens.compInput)
                            .border(
                                width = dimens.divider,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(dimens.cornerFull)
                            ),
                        shape = RoundedCornerShape(dimens.cornerFull),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = dimens.space2
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_google),
                                contentDescription = null,
                                modifier = Modifier.size(dimens.iconMd),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(dimens.space12))
                            Text(
                                text = stringResource(R.string.login_continue_with_google),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimens.space24))

                    // Footer Terms & Privacy Clickable Text
                    val prefixText = stringResource(R.string.login_agree_terms_prefix)
                    val termsText = stringResource(R.string.login_terms_of_service)
                    val andText = stringResource(R.string.login_and)
                    val privacyText = stringResource(R.string.login_privacy_policy)

                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = customColors.secondaryText)) {
                            append(prefixText)
                        }
                        pushStringAnnotation(tag = "TERMS", annotation = "terms")
                        withStyle(
                            style = SpanStyle(
                                color = customColors.secondaryText,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append(termsText)
                        }
                        pop()
                        withStyle(style = SpanStyle(color = customColors.secondaryText)) {
                            append(andText)
                        }
                        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                        withStyle(
                            style = SpanStyle(
                                color = customColors.secondaryText,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append(privacyText)
                        }
                        pop()
                    }

                    ClickableText(
                        text = annotatedString,
                        style = MaterialTheme.typography.bodySmall.copy(
                            textAlign = TextAlign.Center
                        ),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                                .firstOrNull()?.let {
                                    onTermsClick()
                                }
                            annotatedString.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                                .firstOrNull()?.let {
                                    onPrivacyClick()
                                }
                        }
                    )
                }
            }
        }
    }
}
