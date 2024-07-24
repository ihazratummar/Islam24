package com.hazrat.islam24.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.presentation.component.CustomTextField
import com.hazrat.islam24.main.navigation.nvgraph.Route
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun AuthLoginScreen(
    navController: NavController,
    state: LoginState,
    loginEvent: (LoginEvent) -> Unit,
    authState: AuthState
) {
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when(authState){
            is AuthState.Authenticated -> {
                navController.navigate(Route.ProfileScreen.route) {
                    popUpTo(Route.LoginScreen.route) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, authState.message, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
        loginEvent(LoginEvent.Refresh)
    }

    val onLoginClick = remember(state.email, state.password) {
        {
            loginEvent(LoginEvent.Login(email = state.email, password = state.password))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
            .padding(horizontal = MaterialTheme.dimens.size20),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size100))
        Text(text = "Sign In", fontSize = 40.sp, fontFamily = FontFamily(Font(R.font.nunitobold)))
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size50))
        Text(
            text = "Enter your email and password",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.nunitobold)),
            color = Color(0xFFA8A6A7)
        )
        Spacer(modifier = Modifier.height(100.dp))
        CustomTextField(
            label = { Text(text = "Email") },
            placeholder = { Text(text = "Enter Your Email") },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.email),
                    contentDescription = null
                )
            },
            value = state.email,
            onValueChange = { loginEvent(LoginEvent.SetEmail(it)) },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomTextField(
            label = { Text(text = "Password") },
            placeholder = { Text(text = "Enter Your Password") },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.password),
                    contentDescription = null
                )
            },
            value = state.password,
            onValueChange = { loginEvent(LoginEvent.SetPassword(it)) },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            trailingIcon = {
                val image = if (state.passwordVisible) painterResource(id = R.drawable.eyeopen)
                else painterResource(id = R.drawable.eyeclose)
                IconButton(onClick = { loginEvent(LoginEvent.OnPasswordVisibilityChanged)}
                ) {
                    Icon(
                        painter = image,
                        contentDescription = if (state.passwordVisible) "Hide Password" else "Show Password",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(35.dp))
        Button(
            onClick = onLoginClick

            ,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSurface,

                ),
            enabled = state.isFormValid && authState != AuthState.Loading
        ) {
            Text(
                text = "LOGIN",
                fontFamily = FontFamily(Font(R.font.nunitoregular)),
                fontSize = 22.sp
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row {
            Text(
                text = "Don't have an account?",
                fontFamily = FontFamily(Font(R.font.nunitoregular)),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Sign Up",
                fontFamily = FontFamily(Font(R.font.nunitoregular)),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate(Route.SignupScreen.route)

                }
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}