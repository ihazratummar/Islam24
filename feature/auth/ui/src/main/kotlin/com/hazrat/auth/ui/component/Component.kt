package com.hazrat.auth.ui.component

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import coil.compose.AsyncImage
import coil.imageLoader
import com.hazrat.model.Languages
import com.hazrat.ui.R
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.common.SpringToggle
import com.hazrat.ui.common.customClick
import com.hazrat.ui.theme.MutedTextColor
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */


data class ToggleSettingData(
    val label: String,
    val statusText: String,
    val icon: Int,
    val isEnable: Boolean,
    val onClick: () -> Unit
)

data class AppMetaDataSettings(
    val icon: Int,
    val settingName: String,
    val label: String? = null,
    val trailingIcon: Int,
    val onClick: () -> Unit
)


@Composable
fun ToggleSettings(
    modifier: Modifier = Modifier,
    icon: Int,
    label: String,
    statusText: String,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                vertical = dimens.space8,
                horizontal = dimens.space12
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.space8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconWithBackground(
            icon = icon,
            iconColor = customColors.iconColor,
            containerColor = customColors.iconColor.copy(0.05f)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimens.space4),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MutedTextColor
                )
            )
        }
        SpringToggle(
            checked = isEnabled,
            onCheckedChange = { onClick() }
        )
    }
}


@Composable
fun SettingItemCard(
    modifier: Modifier = Modifier,
    leadingIcon: Int,
    settingText: String,
    onClick: () -> Unit = {},
    label: String? = null,
    trailingIcon: Int
) {
    Row(
        modifier = modifier
            .padding(
                vertical = dimens.space8,
                horizontal = dimens.space12
            )
            .fillMaxWidth().customClick(onClick),
        horizontalArrangement = Arrangement.spacedBy(dimens.space8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconWithBackground(
            icon = leadingIcon,
            iconColor = customColors.iconColor,
            containerColor = customColors.iconColor.copy(0.05f)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimens.space4),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = settingText,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            label?.let { text ->
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedTextColor
                    )
                )
            }
        }

        IconWithBackground(
            icon = trailingIcon,
            iconColor = customColors.progressbarMute,
            containerColor = Color.Transparent,
            onClick = onClick
        )
    }
}

@Composable
fun CustomTextField(
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    OutlinedTextField(
        label = label,
        value = value,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions.Default,
        singleLine = true,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

@Composable
fun ZoomedProfileImage(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    imageUri: String?,
    context: Context
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible) {

        }
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Card(
                modifier = modifier.padding(dimens.space20)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = imageUri,
                    contentDescription = null,
                    imageLoader = context.imageLoader
                )
            }
        }
    }
}