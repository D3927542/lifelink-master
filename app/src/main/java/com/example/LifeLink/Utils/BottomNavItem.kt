@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.example.LifeLink.Utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.LifeLink.R
import com.example.LifeLink.ui.theme.AppRed
import java.io.ByteArrayOutputStream


data class BottomNavItem(
    val name: String,
    val route: String,
    val icon_nonfocused: Int,
    val icon_focused: Int
)

@Composable
fun getBottomNavList() : List<BottomNavItem> = listOf(
        BottomNavItem(
            name = "Home",
            route = "home",
            icon_nonfocused = R.drawable.home_selected,
            icon_focused = R.drawable.home
        ),
        BottomNavItem(
            name = "Contact",
            route = "contact",
            icon_nonfocused = R.drawable.contact,
            icon_focused = R.drawable.contact_selected
        ),
        BottomNavItem(
            name = "Medical Info",
            route = "medical",
            icon_nonfocused = R.drawable.medical,
            icon_focused = R.drawable.medical_selected
        ),
        BottomNavItem(
            name = "Settings",
            route = "settings",
            icon_nonfocused = R.drawable.menu,
            icon_focused = R.drawable.menu_selected
        ),
    )

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background =
        if (selected) AppRed else Color.Transparent

    val contentColor =
        if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .padding(1.dp)
            .background(background)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = if (selected) screen.icon_focused else screen.icon_nonfocused),
                contentDescription = "icon",
                tint = contentColor
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.name,
                    color = contentColor
                )
            }
        }
    }
}
fun getByteArrayFromDrawable(drawable: Drawable): ByteArray {
    val bitmap = drawableToBitmap(drawable)
    return bitmapToByteArray(bitmap)
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
@Composable
fun GetContactsPermission(){
//    var check  = false
//    rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->  check = isGranted}
//    return check
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission is granted, you can access contacts here
            // For example, you can use a ContactList composable to display contacts
         } else {
            // Permission denied, handle this case
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    DisposableEffect(Unit) {
        launcher.launch(android.Manifest.permission.READ_CONTACTS)
        onDispose { }
    }
}