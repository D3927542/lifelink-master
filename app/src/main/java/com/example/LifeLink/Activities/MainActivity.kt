package com.example.LifeLink.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.LifeLink.Models.Contact
import com.example.LifeLink.Utils.BottomNavItem
import com.example.LifeLink.Screens.ContactScreen
import com.example.LifeLink.Screens.HomeScreen
import com.example.LifeLink.Screens.MedicalScreen
import com.example.LifeLink.Screens.SettingsScreen
import com.example.LifeLink.Models.User
import com.example.LifeLink.Models.ViewModelsResponse
import com.example.LifeLink.Screens.HelpScreen
import com.example.LifeLink.Service.NotificationService
import com.example.LifeLink.Utils.AddItem
import com.example.LifeLink.Utils.getBottomNavList
import com.example.LifeLink.ViewModels.ContactViewModel
import com.example.LifeLink.ViewModels.LocationViewModel
import com.example.LifeLink.ViewModels.MedicalViewModel
import com.example.LifeLink.ViewModels.UserViewModel
import com.example.LifeLink.ui.theme.LifeLinkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userVm  by viewModels<UserViewModel>()
    private val contactVm  by viewModels<ContactViewModel>()
    private val locationVm  by viewModels<LocationViewModel>()
    private val medicalVm  by viewModels<MedicalViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var user by mutableStateOf(User(name = "", image = byteArrayOf()))
        var contacts = listOf<Contact>()
        GlobalScope.launch {
            user = userVm.currentuser
            locationVm.getCurrentLocation(this@MainActivity)
            contacts = contactVm.contacts.first()
            medicalVm.medical
        }
        val dataset = ViewModelsResponse(userVm, contactVm, locationVm, medicalVm)
        setContent {
            ShowBottomNav(user, dataset)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                val notificationService = NotificationService(this@MainActivity)
                notificationService.showBasicNotification()
                mainHandler.postDelayed(this, 30 * 60 * 10* 1000)
            }
        })
    }
}
@Composable
fun ShowBottomNav(user: User, dataset: ViewModelsResponse){
    LifeLinkTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomBar(navController = navController, screens = getBottomNavList()) }
        ) {
            Modifier.padding(it)
            BottomNavGraph(
                navController = navController,
                user,
                dataset
            )
        }
    }
}
@Composable
fun BottomBar(
    navController: NavHostController, screens: List<BottomNavItem>
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .background(Color.White)
            .zIndex(1f)
            .height(70.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
fun BottomNavGraph(navController: NavHostController, user: User, dataset: ViewModelsResponse) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(bottom = 70.dp)
    ) {
        composable("home") {
            HomeScreen(user = user, rememberNavController = navController)
        }
        composable("contact") {
            ContactScreen(contactVm = dataset.contactVm,rememberNavController = navController)
        }
        composable("medical") {
            MedicalScreen(dataset.medicalVm, dataset.userVm,rememberNavController = navController)
        }
        composable("settings") {
            SettingsScreen(dataset.userVm)
        }
        composable("help") {
            HelpScreen(contactVm = dataset.contactVm, locationVm = dataset.locationVm, userViewModel = dataset.userVm, rememberNavController = navController)
        }
    }
}
