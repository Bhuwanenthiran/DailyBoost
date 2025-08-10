package com.example.dailyboost

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.dailyboost.ui.theme.DailyBoostTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username") ?: "User"
        setContent {
            DailyBoostTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DashboardScreen(username)
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(username: String) {
    val context = LocalContext.current
    var selectedMood by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var savedGoal by remember { mutableStateOf("") }

    val quotes = mapOf(
        "😊" to listOf("Keep smiling!", "Your vibe is contagious!"),
        "😐" to listOf("Stay balanced.", "Take a break."),
        "😔" to listOf("You're stronger than you think.", "Tomorrow is a new day.")
    )

    // Load saved goal once on composition
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        savedGoal = prefs.getString("savedGoal", "") ?: ""
        goal = savedGoal
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome, $username!", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        Text("How are you feeling today?")

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("😊", "😐", "😔").forEach { mood ->
                Button(
                    onClick = { selectedMood = mood },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(mood)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedMood.isNotEmpty()) {
            val quote = quotes[selectedMood]?.random() ?: ""
            Text("Quote for you: $quote", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = goal,
            onValueChange = { goal = it },
            label = { Text("Set Today's Goal") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                savedGoal = goal.trim()
                // Save to SharedPreferences
                val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                prefs.edit().putString("savedGoal", savedGoal).apply()
            },
            enabled = goal.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Goal")
        }

        if (savedGoal.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Today's Goal: $savedGoal", style = MaterialTheme.typography.bodyLarge)
        }
    }
}