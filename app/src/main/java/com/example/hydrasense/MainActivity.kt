package com.example.hydrasense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hydrasense.ui.AppNavigation
import com.example.hydrasense.ui.theme.HydraSenseTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            HydraSenseTheme {
                AppNavigation()
            }
        }
    }
}
