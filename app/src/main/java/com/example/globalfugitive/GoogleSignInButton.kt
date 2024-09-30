package com.example.globalfugitive

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.globalfugitive.BuildConfig.WEB_CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInButton(
    onGetCredentialResponse: (Credential) -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    Box(
        modifier = Modifier
            .clickable {

                // Handle click event when the image is clicked
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(WEB_CLIENT_ID)
                    .setAutoSelectEnabled(false)
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                coroutineScope.launch {
                    try {
                        val result = credentialManager.getCredential(
                            request = request,
                            context = context
                        )

                        onGetCredentialResponse(result.credential)
                    } catch (e: GetCredentialException) {
                        println("Get credentials failed")
                        println(e)
                    }
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_button), // Replace with your image resource
            contentDescription = "Sign in with Google",
            modifier = Modifier.width(200.dp) // You can adjust the size as per your requirement
        )
    }
}