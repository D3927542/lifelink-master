package com.example.LifeLink.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.LifeLink.Constants.Permissions.Companion.allPermissions
import com.example.LifeLink.R
import com.example.LifeLink.Screens.GetUserDetail
import com.example.LifeLink.Utils.GetContactsPermission
import com.example.LifeLink.Utils.RequestAllPermission
import com.example.LifeLink.Utils.checkLocationSetting
import com.example.LifeLink.ViewModels.UserViewModel
import com.example.LifeLink.ui.theme.LifeLinkTheme
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private var userExist: Boolean = false
    val REQUEST_CODE = 100
    private val vm by viewModels<UserViewModel>()
    private lateinit var dexter : DexterBuilder
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPermissions()
        GlobalScope.launch {
            userExist = vm.userExist
            Log.d("user exist", userExist.toString())
        }
        setContent {
            //get permissions
            checkLocationSetting(
                context = this,
                onDisabled = {
                    this.requestPermissions(
                        arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        REQUEST_CODE
                    )
                },
                onEnabled = { /* This will call when setting is already enabled */ }
            )
            RequestAllPermission(applicationContext)
            GetContactsPermission()

            if (userExist)
                GoToHomeScreen()
            else
                GetUserDetail()
        }


    }
   fun getPermissions(){
       Dexter.withContext(this)
           .withPermissions(allPermissions.toList()
           ).withListener(object : MultiplePermissionsListener {
               override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                   println()
               }

               override fun onPermissionRationaleShouldBeShown(
                   permissions: List<PermissionRequest>,
                   token: PermissionToken,
               ) {
                   token?.continuePermissionRequest()
               }
           }).withErrorListener{
               Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
           }
           .check()
   }
    @Preview(showBackground = true)
    @Composable
    fun GoToHomeScreen() {
        val context = LocalContext.current
        LifeLinkTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFEE2A28),
                                Color(0xFFF69291),
                            )
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(56.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(500.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bus),
                        contentDescription = "bus",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
                ElevatedButton(
//                colors = ButtonDefaults.buttonColors( = Color.White),
                    modifier = Modifier
                        .height(50.dp)
                        .width(150.dp),
                    border = BorderStroke(0.dp, color = Color.Unspecified),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = ButtonDefaults.outlinedShape,
                    onClick = { context.startActivity(Intent(context, MainActivity::class.java)) })
                {
                    Text(
                        "Get Started",
                        color = Color(0xFFB43C3A),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}