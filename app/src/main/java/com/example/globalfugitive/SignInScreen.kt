package com.example.globalfugitive


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.globalfugitive.BuildConfig.WEB_CLIENT_ID
import com.example.globalfugitive.ui.theme.backgroundDark
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import kotlinx.coroutines.launch


@SuppressLint("ResourceType")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun SignInScreen(
    userViewModel: UserViewModel,
    navController: NavController,
) {

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val currentUser = userViewModel.currentUser

    val errorMessage by userViewModel.errorMessage.observeAsState()

    // Observe the errorMessage and stop loading if there's an error
    if (errorMessage != null) {
        isLoading = false
    }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            isLoading = false
            // Navigate to MainMenu when the user is successfully logged in
            navController.navigate("DrawerMenu")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Image(
                painter = painterResource(id = R.drawable.global_fugitive_text_transp),
                contentDescription = "Global Fugitive text",
                modifier = Modifier
                    .height(100.dp)
                    .offset(y = 25.dp)
            )
        }
    }

    // UI Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = {
                userViewModel.clearErrorMessage()
                email = it
            },
            label = { Text("Username") },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                userViewModel.clearErrorMessage()
                password = it

            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                val activity = context as Activity
                userViewModel.signInWithEmailAndPassword(
                    activity,
                    email,
                    password,
                ) { navController.navigate("DrawerMenu") }
            },
            modifier = Modifier.width(200.dp),
//            enabled = !isLoading
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                isLoading = true
                val activity = context as Activity
                userViewModel.createUserWithEmailAndPassword(
                    activity,
                    email,
                    password,
                ) { navController.navigate("DrawerMenu") }
            },
            modifier = Modifier.width(200.dp),
//            enabled = !isLoading
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        GoogleSignInButton { credential ->
            userViewModel.signInWithGoogle(credential) { navController.navigate("DrawerMenu") }
        }


        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        // Error message display
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

    }

}






