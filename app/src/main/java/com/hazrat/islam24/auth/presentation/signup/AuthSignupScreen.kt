package com.hazrat.islam24.auth.presentation.signup

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.hazrat.islam24.auth.navigation.Login
import com.hazrat.islam24.auth.navigation.SignUp
import com.hazrat.islam24.auth.presentation.component.CustomTextField
import com.hazrat.islam24.main.navigation.ProfileScreen
import com.hazrat.islam24.main.navigation.nvgraph.Route

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun AuthSignupScreen(
    navController: NavController,
    signUpState: SingupState,
    onEvent: (SingupEvent) -> Unit,
    authState: AuthState
) {
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when(authState){
            is AuthState.Authenticated -> {
                navController.navigate(ProfileScreen) {
                    popUpTo(SignUp) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, authState.message, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
        onEvent(SingupEvent.Refresh)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "Sign Up", fontSize = 40.sp, fontFamily = FontFamily(Font(R.font.nunitobold)))
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "First create your account",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.nunitobold)),
            color = Color(0xFFA8A6A7)
        )
        Spacer(modifier = Modifier.height(100.dp))
        CustomTextField(
            label = { Text(text = "Full name") },
            placeholder = { Text(text = "Enter your full name") },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.editcontained),
                    contentDescription = null
                )
            },
            value = signUpState.name,
            onValueChange = {
                onEvent(SingupEvent.SetName(it))
            },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomTextField(
            label = { Text(text = "Email") },
            placeholder = { Text(text = "Enter Your Email") },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.email),
                    contentDescription = null
                )
            },
            value = signUpState.email,
            onValueChange = {
                onEvent(SingupEvent.SetEmail(it))
            },
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
            value = signUpState.password,
            onValueChange = {
                onEvent(SingupEvent.SetPassword(it))
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            trailingIcon = {
                val image = if (signUpState.passwordVisible) painterResource(id = R.drawable.eyeopen)
                else painterResource(id = R.drawable.eyeclose)
                IconButton(onClick = {
                    onEvent(SingupEvent.OnPasswordVisibilityChanged)
                }) {
                    Icon(
                        painter = image,
                        contentDescription = if (signUpState.passwordVisible) "Hide Password" else "Show Password",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            visualTransformation = if (signUpState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomTextField(
            label = { Text(text = "Confirm password") },
            placeholder = { Text(text = "Enter Your Password") },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.password),
                    contentDescription = null
                )
            },
            value = signUpState.confirmPassword,
            onValueChange = {
                onEvent(SingupEvent.SetConfirmPassword(it))
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            trailingIcon = {
                val image =
                    if (signUpState.confirmPasswordVisible) painterResource(id = R.drawable.eyeopen)
                    else painterResource(id = R.drawable.eyeclose)
                IconButton(onClick = {
                    onEvent(SingupEvent.OnConfirmPasswordVisibilityChanged)
                }) {
                    Icon(
                        painter = image,
                        contentDescription = if (signUpState.confirmPasswordVisible) "Hide Password" else "Show Password",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            visualTransformation = if (signUpState.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            if (signUpState.confirmPassword.isNotBlank() && !signUpState.isPasswordValid) {
                Icon(painter = painterResource(id = R.drawable.alert), contentDescription ="Alert", tint = MaterialTheme.colorScheme.error )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Password do not match", fontFamily = FontFamily(Font(R.font.nunitoregular)),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(modifier = Modifier.height(35.dp))
        Button(
            onClick = {
                onEvent(SingupEvent.Signup(signUpState.name, signUpState.email, signUpState.password, signUpState.confirmPassword))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color(0xFF222222)
            ),
            enabled = signUpState.isFormValid && signUpState.isPasswordValid && authState != AuthState.Loading
        ) {
            Text(
                text = "SIGN UP",
                fontFamily = FontFamily(Font(R.font.nunitoregular)),
                fontSize = 22.sp
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row {
            Text(
                text = "Already have an account?",
                fontFamily = FontFamily(Font(R.font.nunitoregular)),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Sign In",
                fontFamily = FontFamily(Font(R.font.nunitoregular)),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate(Login)
                }
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}