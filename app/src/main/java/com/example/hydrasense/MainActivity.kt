package com.example.hydrasense



import android.os.Bundle

import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.navigation.NavController

import com.example.hydrasense.ui.AppNavigation

import com.example.hydrasense.ui.theme.HydraSenseTheme



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            HydraSenseTheme {

                AppNavigation()

            }

        }

    }

}



@Composable

fun NewReportScreen(navController: NavController) { Text("New Report Entry Screen") }

@Composable

fun MyReportsScreen() { Text("My Reports Screen") }

@Composable

fun NotificationsScreen() { Text("Notifications Screen") }

@Composable

fun ProfileScreen() { Text("Profile Screen") }

@Composable

fun SettingsScreen() { Text("Settings Screen") }