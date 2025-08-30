import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecastapp.model.Favorite
import com.example.weatherforecastapp.navigation.WeatherScreens
import com.example.weatherforecastapp.screens.favorites.FavoriteViewModel


@Composable
fun WeatherAppBar(
    title: String,
    icon: ImageVector? = null,
    isMainScreen: Boolean = true,
    elevation: Dp = 0.dp,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    onAddActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
) {
    val showDialog = remember { mutableStateOf(false) }
    val showIt = remember { mutableStateOf(false) }

    if (showDialog.value) {
        ShowSettingDropdownMenu(
            showDialog = showDialog,
            navController = navController
        )
    }

    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.onSecondary
            )
        },
        actions = {
            if (isMainScreen) {
                IconButton(onClick = { onAddActionClicked.invoke() }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { showDialog.value = true }) {
                    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "Settings")
                }
            } else {
                Box {}
            }
        },
        backgroundColor = Color.Transparent,
        navigationIcon = {
            Box(
                modifier = Modifier
                    .clickable { onButtonClicked.invoke() }
                    .padding(8.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Icon",
                        tint = MaterialTheme.colors.onSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                if (isMainScreen) {
                    val isAlreadyFavList = favoriteViewModel.favList.collectAsState().value.filter {
                        (it.city == title.split(",")[0])
                    }
                    if (isAlreadyFavList.isNullOrEmpty()) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite icon",
                            modifier = Modifier
                                .scale(0.9f)
                                .size(24.dp)
                                .clickable {
                                    val dataList = title.split(",")
                                    favoriteViewModel.insertFavorite(
                                        Favorite(
                                            city = dataList[0],
                                            country = dataList[1]
                                        )
                                    ).run {
                                        showIt.value = true
                                    }
                                },
                            tint = Color.Red.copy(alpha = 0.8f)
                        )
                    } else {
                        showIt.value = false
                        Box {}
                    }
                }
                ShowToast(context = context, showIt)
            }
        },
        elevation = elevation,
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0),
                        Color(0xFF6C757D)
                    )
                )
            )
    )
}

@Composable
fun ShowToast(context: Context, showIt: MutableState<Boolean>) {
    if(showIt.value){
        Toast.makeText(context,"Added to Favorites",Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ShowSettingDropdownMenu(showDialog: MutableState<Boolean>, navController: NavController) {
    var expanded by remember {
        mutableStateOf(true)
    }
    val items = listOf("About", "Favorites", "Settings")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .absolutePadding(top = 45.dp, right = 20.dp)
            .wrapContentSize(
                Alignment.TopEnd
            )
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(140.dp)
                .background(Color.White)
        ) {
            items.forEachIndexed { index, text ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    showDialog.value = false
                }) {
                    Icon(
                        modifier = Modifier.padding(3.dp),
                        imageVector = when (text) {
                            "About" -> Icons.Default.Info
                            "Favorites" -> Icons.Default.FavoriteBorder
                            else -> Icons.Default.Settings
                        }, contentDescription = null, tint = Color.LightGray
                    )
                    Text(
                        text = text,
                        modifier = Modifier
                            .clickable {
                                navController.navigate(
                                    when (text) {
                                        "About" -> WeatherScreens.AboutScreen.name
                                        "Favorites" -> WeatherScreens.FavoriteScreen.name
                                        else -> WeatherScreens.SettingsScreen.name
                                    }
                                )

                            }
                            .padding(3.dp),
                        fontWeight = FontWeight.Light
                    )

                }
            }

        }

    }

}