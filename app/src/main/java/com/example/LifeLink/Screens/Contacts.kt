package com.example.LifeLink.Screens

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.LifeLink.Models.Contact
import com.example.LifeLink.R
import com.example.LifeLink.Utils.GetContactsPermission
import com.example.LifeLink.Utils.getByteArrayFromDrawable
import com.example.LifeLink.ViewModels.ContactViewModel
import com.example.LifeLink.ui.theme.AppRed
import java.io.ByteArrayOutputStream
import java.io.InputStream

private lateinit var contactLauncher :  ManagedActivityResultLauncher<Void?, Uri?>
private lateinit var context: Context
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContactScreen(contactVm: ContactViewModel, rememberNavController: NavHostController) {
    context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    GetContactsPermission()
    contactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->  if(uri != null) getContactDetails(contentResolver, uri!!, contactVm) }
    val contactList by contactVm.contacts.collectAsState(
        initial = emptyList()
    )

    Column(modifier = Modifier.fillMaxSize())
    {
        //toolbar
        GetToolbar(rememberNavController)
        GetAddButton()
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
                            items(contactList) { model ->
                                ContactItemUI(model, contactVm)
                            }
                        }
                    }
                }
            }
        )
        //contactList.forEach{ContactItemUI(it)}
    }
}



@Composable
fun ContactItemUI(contact: Contact, vm : ContactViewModel){
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp)
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                bitmap = BitmapFactory.decodeByteArray(contact.image, 0, contact.image.size).asImageBitmap(),
                contentDescription = "display",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(2.dp, Color.Unspecified, CircleShape)   // add a border (optional)
                    .size(48.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = contact.name,
                fontSize = 16.sp,
                color = AppRed
            )
            Text(
                text = contact.phoneNumber,
                fontSize = 12.sp,
                color = Color(0xFF373738),
            )
        }

        Box(
            modifier = Modifier.fillMaxSize().size(48.dp).align(Alignment.CenterVertically),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_delete_sweep_24),
                contentDescription = "edit",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(1.dp, AppRed, CircleShape)   // add a border (optional)
                    .padding(6.dp)
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { vm.delete(contact) }
            )
        }
    }
}


private fun getContactDetails(contentResolver: ContentResolver, contactUri: Uri, viewModel: ContactViewModel) {
    val contactDetails = ContactDetails("", "", byteArrayOf())

    // Query for contact name
    contentResolver.query(contactUri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val name = cursor.getString(nameIndex)
            contactDetails.name = name
        }
    }

    // Query for contact phone number
    val phoneCursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
        arrayOf(contactUri.lastPathSegment!!),
        null
    )
    phoneCursor?.use { cursor ->
        if (cursor.moveToFirst()) {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val number = cursor.getString(numberIndex)
            contactDetails.number = number
        }
    }

    // Query for contact photo
    var photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
    contactDetails.photoUri = getPhotoBytes(contentResolver, photoUri)!!
    viewModel.add(Contact(name = contactDetails.name!!,
        phoneNumber = contactDetails.number!!, image = contactDetails.photoUri))
}

// Data class to hold contact details
data class ContactDetails(
    var name: String,
    var number: String,
    var photoUri: ByteArray
)
// Function to convert the contact photo URI to a byte array


private fun getPhotoBytes(contentResolver: ContentResolver, photoUri: Uri): ByteArray? {
    var inputStream: InputStream? = null
    try {
        inputStream = contentResolver.openInputStream(photoUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
    }
    return ContextCompat.getDrawable(context, R.drawable.contact)
        ?.let { getByteArrayFromDrawable(it) }
}
@Composable
fun GetToolbar(rememberNavController: NavHostController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(16.dp)
    ){
        Box(modifier = Modifier
            .fillMaxHeight()
            .clickable { rememberNavController.navigateUp() },
            contentAlignment = Alignment.CenterStart){
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "back" ,
                modifier = Modifier.padding(8.dp)
            )
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(
                text = "Contacts",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun GetAddButton() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp),
//        .align(Alignment.CenterHorizontally),
        contentAlignment = Alignment.CenterStart)
    {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { contactLauncher.launch() })
        {
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.addcontact),
                contentDescription = "addcontact" ,
                modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Add new",
                fontSize = 20.sp,
                color = AppRed,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}