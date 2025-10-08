package com.example.hydrasense.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.google.firebase.auth.FirebaseAuth
import com.example.hydrasense.AppDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(auth: FirebaseAuth, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val signupButtonColor = Color(0xFF4CAF50)

    val onSignupClick: () -> Unit = {
//        if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 6) {
//            auth.createUser WithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show()
//                    navController.navigate(AppDestinations.HOME_ROUTE) {
//                        popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
//                    }
//                } else {
//                    Toast.makeText(
//                        context,
//                        "Signup Failed: ${task.exception?.message}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        } else {
//            Toast.makeText(context, "Please enter valid email and password (min 6 chars)", Toast.LENGTH_SHORT).show()
//        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign Up") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Email Field (similar to LoginScreen)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("Enter your email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        placeholder = { Text("Enter your password (min 6 chars)") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onSignupClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = signupButtonColor)
                    ) {
                        Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Back to Login
                    Text(
                        text = "Already have an account? Login",
                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.clickable {
//                            navController.navigate(AppDestinations.LOGIN_ROUTE) {
//                                popUpTo(AppDestinations.SIGNUP_ROUTE) { inclusive = true }
//                            }
//                        }
                    )
                }
            }
        }
    }
}