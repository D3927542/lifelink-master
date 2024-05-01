package com.example.LifeLink.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.LifeLink.Activities.MainActivity
import com.example.LifeLink.Models.Medical
import com.example.LifeLink.Models.User
import com.example.LifeLink.R
import com.example.LifeLink.Utils.getByteArrayFromDrawable
import com.example.LifeLink.ViewModels.MedicalViewModel
import com.example.LifeLink.ViewModels.UserViewModel
import com.example.LifeLink.ui.theme.AppRed
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GetUserDetail(viewModel: UserViewModel = hiltViewModel(), medicalviewModel: MedicalViewModel = hiltViewModel()){
    val context = LocalContext.current
    val bytes = getByteArrayFromDrawable(ContextCompat.getDrawable(context, R.drawable.contact)!!)
//    var defaultImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
    var userName by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var allergy by remember { mutableStateOf("") }
    var medication by remember { mutableStateOf("") }
    var medicalNotes by remember { mutableStateOf("") }
    var existingCondition by remember { mutableStateOf("") }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null)}
    val getContent = rememberLauncherForActivityResult(contract = GetContent()) { uri ->
        val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
        if (inputStream != null) {
            imageByteArray = inputStream.readBytes()
            bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray!!.size)?.asImageBitmap()
        }
    }

    val scrollState = rememberScrollState()
    Column (modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x3F9AE8D1),
                    Color(0x3FFFFFFF),
                )
            )
        )){
        GetToolbarHere()
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Row (
               modifier = Modifier
                   .fillMaxWidth()
                   .height(200.dp)
                   .align(Alignment.CenterHorizontally),
               horizontalArrangement = Arrangement.Absolute.SpaceAround,
               verticalAlignment = Alignment.CenterVertically
           ){
               Button(
                   onClick = { getContent.launch("image/*") },
                   modifier = Modifier.padding(vertical = 8.dp)
               ) {
                   Text("Select Image")
               }
               bitmap?.let {
                   Image(bitmap = it, contentDescription = "Selected Image",
                       contentScale = ContentScale.Crop, modifier = Modifier
                           .clip(
                               CircleShape
                           )
                           .size(200.dp)
                           .background(AppRed))
               }
           }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Enter your name:")
                TextField(
                    value = userName,
                    onValueChange = { userName = it },
                    modifier = Modifier.width(200.dp),
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Enter your blood type:", modifier = Modifier.width(100.dp))
                TextField(
                    value = bloodType,
                    onValueChange = { bloodType = it },
                    modifier = Modifier.width(200.dp),
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Enter your allergy:", modifier = Modifier.width(100.dp))
                TextField(
                    value = allergy,
                    onValueChange = { allergy = it },
                    modifier = Modifier.width(200.dp),
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Enter your medication:", modifier = Modifier.width(100.dp))
                TextField(
                    value = medication,
                    onValueChange = { medication = it },
                    modifier = Modifier.width(200.dp),
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Enter your medical notes:", modifier = Modifier.width(100.dp))
                TextField(
                    value = medicalNotes,
                    onValueChange = { medicalNotes = it },
                    modifier = Modifier.width(200.dp),
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Enter your existing condition:", modifier = Modifier.width(100.dp))
                TextField(
                    value = existingCondition,
                    onValueChange = { existingCondition = it },
                    modifier = Modifier.width(200.dp),
                )
            }

            Button(
                onClick = {
                    if(userName.isBlank() || imageByteArray == null)
                        Toast.makeText(context, "Please fill all required details", Toast.LENGTH_SHORT).show()
                    else{
                        viewModel.add(User(name = userName, image = imageByteArray!!))
                        medicalviewModel.add(Medical(name = userName, medications = medication, allergies = allergy, bloodType = bloodType, existingCondition = existingCondition, medicalNotes = medicalNotes))
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }},
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun GetToolbarHere() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0x1A6A5252))
    ){

        Box(modifier = Modifier
            .fillMaxSize()
            .border(1.dp, Color.Gray, RoundedCornerShape(2.dp)), contentAlignment = Alignment.Center){
            Text(
                text = "User Info",
                fontSize = 28.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal
            )
        }
    }
}

fun ContentResolver.loadBitmap(uri: Uri): ImageBitmap? {
    return try {
        val inputStream: InputStream? = openInputStream(uri)
        val image = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        image?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}