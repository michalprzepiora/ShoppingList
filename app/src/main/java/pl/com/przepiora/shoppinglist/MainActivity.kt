package pl.com.przepiora.shoppinglist


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.runBlocking
import pl.com.przepiora.shoppinglist.model.Entry
import pl.com.przepiora.shoppinglist.service.EntryRepositoryNetwork
import pl.com.przepiora.shoppinglist.service.configuration.RetrofitConfiguration
import pl.com.przepiora.shoppinglist.ui.theme.ShoppingListTheme
import java.util.Locale

val entryRepositoryNetwork = EntryRepositoryNetwork(RetrofitConfiguration.entryRepository)
lateinit var initialList: List<Entry>

class MainActivity : ComponentActivity() {

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            initialList = entryRepositoryNetwork.getAll()
        }
        setContent {
            ShoppingListTheme {
                val showDialog = remember { mutableStateOf(false) }
                val entryList =
                    remember { mutableStateListOf(*initialList.map { it }.toTypedArray()) }
                val sortedWith = entryList.sortedWith(
                    compareBy({ it.isDone },
                        { it.text.lowercase(Locale.ENGLISH) })
                )
                Log.d("Entry refresh", sortedWith.toString())
                AnimatedVisibility(showDialog.value) {
                    AddProductDialog(showDialog, entryList) {
                    }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar()
                    },
                    containerColor = Color.DarkGray

                ) { paddingValues ->
                    Log.d("Entry refresh", "Open Scaffold with: $paddingValues padding values.")
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (entryList.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = "The list is empty. Please add products...",
                                    modifier = Modifier.align(Alignment.Center),
                                    color = Color.White
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(top = 36.dp)
                                    .background(color = Color.DarkGray)
                            ) {
                                Log.d("Entry refresh", "Refresh ${entryList.size} Entry objects.")
                                items(items = sortedWith.toList(), key = { it.text }) {
                                    EntryCard(it, entryList, Modifier.animateItemPlacement())
                                }
                            }
                        }
                        FloatingActionButton(modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(15.dp)
                            .size(width = 350.dp, height = 50.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(50.dp)),
                            shape = RoundedCornerShape(50.dp),
                            containerColor = Color.Yellow,
                            onClick = { showDialog.value = !showDialog.value })
                        {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Create,
                                    contentDescription = "Add new entry"
                                )
                                Text(text = "  Add product")
                            }
                        }
                    }
                }

            }
        }
    }
}

@SuppressLint("UnnecessaryComposedModifier")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryCard(entry: Entry, entryList: MutableList<Entry>, modifier: Modifier) {
    val color: CardColors
    val icon: ImageVector

    Log.d("Entry data", entry.toString())
    if (entry.isDone) {
        color = CardDefaults.cardColors(Color.LightGray)
        icon = Icons.Default.CheckCircle
    } else {
        color = CardDefaults.cardColors(Color.Green)
        icon = Icons.Default.ShoppingCart
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .composed { modifier },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .size(width = 328.dp, height = 70.dp)
                .padding(5.dp),
            colors = color,
            shape = RoundedCornerShape(10.dp),
            onClick = {
                val index = entryList.indexOf(entry)
                entry.isDone = !entry.isDone
                entryList.add(index, entry)
                entryList.removeAt(index + 1)
                runBlocking {
                    entry.isDone = !entry.isDone
                    entryRepositoryNetwork.changeDone(entry)
                }
            },

            ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start

            ) {
                Icon(
                    imageVector = icon, contentDescription = "bought",
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = entry.text,
                )

            }
        }
        Card(
            modifier = Modifier
                .size(width = 70.dp, height = 70.dp)
                .padding(5.dp)
                .fillMaxSize(),
            colors = CardDefaults.cardColors(Color.DarkGray),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                entryList.remove(entry)
                runBlocking {
                    entryRepositoryNetwork.delete(entry)
                }
            },
            border = BorderStroke(3.dp, Color.LightGray)
        ) {

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete item",
                    modifier = Modifier.size(40.dp),
                    tint = Color.LightGray
                )

            }
        }
    }
}

@Composable
@Preview
fun TopBar() {
    Box(
        modifier = Modifier
            .background(color = Color.Gray)
            .fillMaxWidth()
    ) {
        Text(
            text = "Shopping list:",
            fontFamily = FontFamily.SansSerif,
            fontSize = 27.sp
        )
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = "Synch: 12.12.2023 12:00",
            textAlign = TextAlign.Right
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    showDialog: MutableState<Boolean>,
    entryList: MutableList<Entry>,
    onDismissRequest: () -> Unit
) {
    var saveButtonIsEnabled = false
    val context = LocalContext.current
    val focusRequester = FocusRequester()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(0.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White),
            border = BorderStroke(5.dp, Color.Black)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var text by remember {
                    mutableStateOf("")
                }
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .focusRequester(focusRequester),
                    value = text,
                    label = { Text(text = "Add new product to list") },
                    maxLines = 1,
                    onValueChange = {
                        text = it
                        saveButtonIsEnabled = it.isNotEmpty()
                    }
                )
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            val entry = Entry(false, text)
                            if (entryList.contains(entry)) {
                                Toast.makeText(
                                    context,
                                    "Entry is already exist.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                entryList.add(entry)
                                runBlocking {

                                    entryRepositoryNetwork.add(entry)
                                }
                            }
                            showDialog.value = false
                        },
                        enabled = saveButtonIsEnabled
                    ) {
                        Text(
                            modifier = Modifier
                                .size(width = 120.dp, height = 20.dp)
                                .align(Alignment.CenterVertically),
                            textAlign = TextAlign.Center,
                            text = "Save"
                        )
                    }
                    Button(onClick = { showDialog.value = false }) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
