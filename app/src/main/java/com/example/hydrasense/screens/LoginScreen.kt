package com.example.hydrasense.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hydrasense.AppDestinations
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(auth: FirebaseAuth, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val loginButtonColor = Color(0xFF03A9F4)

    val onLoginClick: () -> Unit = {
//        if (email.isNotEmpty() && password.isNotEmpty()) {
//            isLoading = true
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    isLoading = false
//                    if (task.isSuccessful) {
//                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
//                        navController.navigate(AppDestinations.HOME_ROUTE) {
//                            popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
//                        }
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "Login Failed: ${task.exception?.message}",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//        } else {
//            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
//        }
        navController.navigate(AppDestinations.HOME_ROUTE) {
                            popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
                       }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Name Title
        Text(
            text = "HydraSense",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = loginButtonColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Subtitle
        Text(
            text = "Track water quality in real-time",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp),
                    color = Color.Black
                )

                // Email Label
                Text(
                    text = "Email",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                // TextField for Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Enter your email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Label
                Text(
                    text = "Password",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                // TextField for Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Enter your password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                Button(
                    onClick = onLoginClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = loginButtonColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Login", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign Up row
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Don't have an account? ")
                    Text(
                        text = "Sign up",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate(AppDestinations.SIGNUP_ROUTE)
                        }
                    )
                }
            }
        }
    }
}