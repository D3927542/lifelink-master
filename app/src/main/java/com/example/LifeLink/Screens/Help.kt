package com.example.LifeLink.Screens

import android.content.Context
import android.graphics.BitmapFactory
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.LifeLink.Models.Contact
import com.example.LifeLink.R
import com.example.LifeLink.Utils.RequestAllPermission
import com.example.LifeLink.Utils.getByteArrayFromDrawable
import com.example.LifeLink.Utils.getSmsPermission
import com.example.LifeLink.ViewModels.ContactViewModel
import com.example.LifeLink.ViewModels.LocationViewModel
import com.example.LifeLink.ViewModels.UserViewModel
import com.example.LifeLink.ui.theme.AppRed
import kotlinx.coroutines.delay

@Composable
fun HelpScreen(contactVm: ContactViewModel, locationVm: LocationViewModel, userViewModel: UserViewModel ,rememberNavController: NavHostController) {
    val context = LocalContext.current
    RequestAllPermission(context)
    getSmsPermission()
    val contacts by contactVm.contacts.collectAsState(initial = emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppRed)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    )
    {
        //GetSpacer(modifier = Modifier.height(48.dp))
        GetTimer(contacts, locationVm, context, userViewModel)
        GetEmergencyTextHelpScreen()
        //GetSpacer(modifier = Modifier.height(48.dp))
        GetAllContact(contacts, context)
        ElevatedButton(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
            border = BorderStroke(0.dp, color = Color.Unspecified),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            onClick = { rememberNavController.navigateUp() }
        )
        {
            Text("I am safe",
                color = Color.Black,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun GetSpacer(modifier: Modifier){
    Spacer(modifier = modifier)
}


@Composable
fun GetTimer(contacts: List<Contact>, locationVm: LocationViewModel, context: Context, userViewModel : UserViewModel) {
    var timeLeft by remember { mutableStateOf(10) }
    LaunchedEffect(key1 = timeLeft) {
        if(locationVm.locationText.isBlank())
            locationVm.getCurrentLocation(context)
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
            if(timeLeft == 0){
                contacts.forEach {
                    SendMessage(it.phoneNumber,
                        "Hi ${userViewModel.currentuser.name} need Help, here is my location http://maps.google.com/maps?daddr=${locationVm.currentLocation?.latitude}+${locationVm.currentLocation?.longitude}")
                }
            }
        }
        Toast.makeText(context, "Sms sent, Help arriving soon.", Toast.LENGTH_SHORT).show()
    }
    Box(
        modifier = Modifier
            .border(1.dp, Color.White, CircleShape)
            .size(200.dp)
            .padding(24.dp)
            .border(1.dp, Color.White, CircleShape)
            .padding(24.dp)
            .border(1.dp, Color.White, CircleShape)
            .background(Color.White, CircleShape)/*
            */,
        contentAlignment = Alignment.Center,
    )
    {
        Text(text = timeLeft.toString(), fontSize = 36.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GetEmergencyTextHelpScreen(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Emergency Calling...",
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        GetSpacer(modifier = Modifier.height(16.dp))
        Text(
            text = "your contacts, app user nearby and your organization can see your help request",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Composable
fun GetAllContact(contacts : List<Contact>, context: Context){
    var defaultImage = ContextCompat.getDrawable(context, R.drawable.contact)
        ?.let { getByteArrayFromDrawable(it) }
    val photos = GetImages(contacts, BitmapFactory.decodeByteArray(defaultImage, 0, defaultImage!!.size).asImageBitmap())
    Box(
        modifier = Modifier
            .width(250.dp)
            .height(230.dp)
            .paint(
                // Replace with your image id
                painterResource(id = R.drawable.borders),
                contentScale = ContentScale.FillWidth,
            )
            .padding(top = 0.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
        ,
    )
    {
        Image(
            bitmap = photos[0],
            contentDescription = "display",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .clip(CircleShape)                       // clip to the circle shape
                .border(2.dp, AppRed, CircleShape)   // add a border (optional)
                .size(48.dp)
                .align(Alignment.CenterStart)
        )
        Image(
            bitmap = photos[1],
            contentDescription = "display",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .clip(CircleShape)                       // clip to the circle shape
                .border(2.dp, AppRed, CircleShape)   // add a border (optional)
                .size(48.dp)
                .align(Alignment.TopCenter)

        )
        Image(
            bitmap = photos[2],
            contentDescription = "display",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .clip(CircleShape)                       // clip to the circle shape
                .border(2.dp, AppRed, CircleShape)   // add a border (optional)
                .size(48.dp)
                .align(Alignment.CenterEnd)

        )
        Image(
            bitmap = photos[3],
            contentDescription = "display",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .clip(CircleShape)                       // clip to the circle shape
                .border(2.dp, AppRed, CircleShape)   // add a border (optional)
                .size(48.dp)
                .align(Alignment.BottomCenter)

        )
        /*Image(
            bitmap = BitmapFactory.decodeByteArray(user.image, 0, user.image.size).asImageBitmap(),
            contentDescription = "display",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .clip(CircleShape)                       // clip to the circle shape
                .border(2.dp, AppRed, CircleShape)   // add a border (optional)
                .size(48.dp)
                .align(Alignment.Center)

        )*/
    }
}

fun GetImages(contacts: List<Contact>, defaultImage: ImageBitmap) : List<ImageBitmap>{
    var res = mutableListOf<ImageBitmap>()
    for(i in 0..3){
        val image = contacts.getOrNull(i)
        if(image == null)
            res.add(defaultImage)
        else res.add(BitmapFactory.decodeByteArray(image.image, 0, image.image.size).asImageBitmap())
    }
    return res
}

fun SendMessage(phoneNumber:String, message:String ){
    val smsManager: SmsManager = SmsManager.getDefault()
    smsManager.sendTextMessage(phoneNumber, null, message, null, null)
}