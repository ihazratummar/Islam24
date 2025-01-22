package com.hazrat.islam24.auth.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.hazrat.islam24.auth.presentation.profileScreen.component.profileCardShimmerEffect
import com.hazrat.islam24.ui.theme.dimens
import com.skydoves.cloudy.Cloudy

/**
 * @author Hazrat Ummar Shaikh
 */


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
            unfocusedBorderColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun ZoomedProfileImage(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    imageUri: String?
) {

    val imageBuilder = ImageRequest.Builder(LocalContext.current)
        .data(imageUri)
        .crossfade(true)
        .placeholderMemoryCacheKey(MemoryCache.Key(imageUri?:""))
        .build()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible) {
            Cloudy(
                modifier = Modifier.fillMaxSize(),
                radius = 25
            ) { }
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Card(
                modifier = modifier.padding(dimens.size20)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = imageBuilder,
                    contentDescription = null
                )
            }
        }
    }
}



@Composable
fun ButtonLoading(modifier: Modifier = Modifier) {
    Button(
        modifier = modifier
            .profileCardShimmerEffect(),
        onClick = {},
        shape = RoundedCornerShape(dimens.size10),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text("Submit")
    }
}


@Composable
fun OtpInputField(
    otp: String,
    onOtpChanged: (String) -> Unit
) {
    BasicTextField(
        value = otp,
        onValueChange = {
            if (it.length <= 4) {
                onOtpChanged(it)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(dimens.size10)
        ) {
            repeat(4) { index ->
                val number = when {
                    index >= otp.length -> ""
                    else -> otp[index]
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimens.size6),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = number.toString(), style = MaterialTheme.typography.titleLarge)
                    Box(
                        modifier = Modifier
                            .width(dimens.size40)
                            .height(dimens.size2)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}