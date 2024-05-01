package com.example.LifeLink.Screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.LifeLink.Models.Medical
import com.example.LifeLink.Models.MedicalInfo
import com.example.LifeLink.Models.User
import com.example.LifeLink.R
import com.example.LifeLink.ViewModels.MedicalViewModel
import com.example.LifeLink.ViewModels.UserViewModel
import com.example.LifeLink.ui.theme.AppRed

private var showDialog by mutableStateOf(false)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun MedicalScreen(viewModel: MedicalViewModel,userviewModel: UserViewModel, rememberNavController: NavHostController) {
    val medical by viewModel.medical.collectAsState(
        initial = null
    )
    val user by userviewModel.currentUserFlow.collectAsState(
        initial = null
    )
    val list = mutableListOf(
        medical?.let { MedicalInfo(name="Name", value = it.name, image = R.drawable.bus, userImage = user?.image) },
        medical?.let { MedicalInfo(name="Blood Type", value = it.bloodType, image = R.drawable.blood) },
        medical?.let { MedicalInfo(name="Allergies", value = it.allergies, image = R.drawable.allergy) },
        medical?.let { MedicalInfo(name="Medications", value = it.medications, image = R.drawable.medication) },
        medical?.let { MedicalInfo(name="Medical Notes", value = it.medicalNotes, image = R.drawable.medicalnotes) },
        medical?.let { MedicalInfo(name="Existing Condition", value = it.existingCondition, image = R.drawable.existingcondition) }
    )
    Column(modifier = Modifier.fillMaxSize())
    {
        //toolbar
        GetMedicalToolbar(rememberNavController)
        GetMedicalAddButton()
        Scaffold(
            content = {
                Box(modifier = Modifier.background(Color.White)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(10.dp)
                        ) {
                            items(list) { model ->
                                if (model != null) {
                                    MedicalItemUI(model, user, viewModel,medical!!
                                        /*onClickAction = {
                                            Log.d("d_onclick", "inside")
                                            EditDetails(
                                                model!!.name,
                                                model.value,
                                                viewModel,
                                                medical!!
                                            )
                                        }*/)
                                }
                            }
                        }
                    }
                }
            }
        )
        }
}

@Composable
fun GetMedicalToolbar(rememberNavController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(16.dp)
    ){
        Box(modifier = Modifier
            .fillMaxHeight()
            .clickable { rememberNavController.navigateUp() }, contentAlignment = Alignment.CenterStart){
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "back" ,
                modifier = Modifier.padding(8.dp)
            )
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(
                text = "Medical Info",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun GetMedicalAddButton() {
    Box(
        modifier = Modifier
            .height(70.dp), contentAlignment = Alignment.CenterStart
    )
    {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.addmedical),
                contentDescription = "addcontact",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Edit or add Medical Info",
                fontSize = 18.sp,
                color = Color(0xFF40A6F0),
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MedicalItemUI(
    med: MedicalInfo,
    user: User?,
    viewModel: MedicalViewModel,
    medical: Medical
){
//    if(showDialog)
//        EditDetails(med.name, med.value, viewModel, medical!!)
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp)
            .background(Color.White)
            .verticalScroll(scrollState)
            .clickable { }
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            if(med.userImage != null){
                Image(
                    bitmap = BitmapFactory.decodeByteArray(user?.image, 0, user?.image!!.size).asImageBitmap(),
                    contentDescription = "display",
                    contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .clip(CircleShape)                       // clip to the circle shape
                        .border(2.dp, Color.Unspecified, CircleShape)   // add a border (optional)
                        .size(48.dp)
                )
            }else{
                Image(
                    painter = painterResource(id = med.image),
                    contentDescription = "display",
                    contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .clip(CircleShape)                       // clip to the circle shape
                        .border(2.dp, Color.Unspecified, CircleShape)   // add a border (optional)
                        .size(48.dp)
                )
            }

        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = med.name,
                fontSize = 16.sp,
                color = AppRed
            )
            Text(
                text = med.value,
                fontSize = 12.sp,
                color = Color(0xFF373738),
            )
        }
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .align(Alignment.CenterVertically),
//            contentAlignment = Alignment.CenterEnd){
//            TextButton(
//                onClick = {
//                    onClickAction
//                }
//            ) {
//                Icon(painter = painterResource(id = R.drawable.edit), contentDescription = "edit")
//            }

//        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDetails(name : String, value : String, viewModel: MedicalViewModel, medical: Medical){
    when(name){
        "name" -> medical.name = value
        "bloodType" -> medical.bloodType = value
        "allergies" -> medical.allergies = value
        "medications" -> medical.medications = value
        "medicalNotes" -> medical.medicalNotes = value
        "bloodType" -> medical.existingCondition = value
    }
    var key by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                ProfileProperty(name, value)
                TextField(
                    value = key,
                    onValueChange = { key = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
                Button(onClick = {
                    viewModel.edit(medical)
                }){
                    Text("Submit")
                }
            }
        }
    }
}