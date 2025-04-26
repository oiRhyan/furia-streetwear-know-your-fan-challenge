package com.dev.rhyan.furiastreetwear.ui.screens.try_on


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dev.rhyan.furiastreetwear.R
import com.dev.rhyan.furiastreetwear.data.models.response.UserDataProvider
import com.dev.rhyan.furiastreetwear.ui.state.ImageIAState
import com.dev.rhyan.furiastreetwear.ui.viewmodels.ImageProcessViewModel
import com.dev.rhyan.furiastreetwear.ui.viewmodels.ProductViewModel
import java.io.File
import java.io.FileOutputStream

@SuppressLint("ResourceType")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TryOnScreen(
    viewModel: ImageProcessViewModel,
    sharedViewModel: ProductViewModel,
    session : UserDataProvider?
) {

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val iaState by viewModel.iaState.collectAsStateWithLifecycle()
    val clothing by sharedViewModel.selectedProduct.collectAsStateWithLifecycle()

    val personPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            uiState.onPersonImageChanged(uri)
        }
    }

    val permissionGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED


    if (!permissionGranted) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.app_slogan),
                contentDescription = "App Logo"
            )
            OutlinedCard(
                shape = CircleShape,
                modifier = Modifier
                    .size(60.dp),
                border = BorderStroke(2.dp, color = Color.White),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    shape = CircleShape
                ) {
                    GlideImage(
                        model = session?.photoUrl,
                        contentDescription = "ProfileImage",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Selecione uma roupa do nosso catálogo de opções!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box() {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(start = 20.dp, end = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray.copy(alpha = 0.5f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { personPicker.launch("image/*") }
                ) {
                    if (uiState.personImage != null) {
                        when (val state = iaState) {
                            is ImageIAState.Empty -> {
                                AsyncImage(
                                    model = uiState.personImage,
                                    contentDescription = "Person Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            is ImageIAState.Loading -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 10.dp
                                    )
                                }
                            }

                            is ImageIAState.Sucess -> {
                                Image(
                                    bitmap = state.image.asImageBitmap(),
                                    contentDescription = "Person Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            is ImageIAState.Error -> {

                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.image_upload),
                                contentDescription = "Image Choose",
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "ANEXE SUA IMAGEM",
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 5.dp, end = 5.dp)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Compartilhar",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 16.dp)
                        .clickable {
                            if (iaState is ImageIAState.Sucess) {
                                val bitmap = (iaState as ImageIAState.Sucess).image

                                val file = File(context.cacheDir, "shared_image.png")
                                val outputStream = FileOutputStream(file)

                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                outputStream.flush()
                                outputStream.close()

                                viewModel.shareImage(context, file)
                            }
                        }
                )

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .padding(top = 280.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = {
                                viewModel.renderImage(
                                    clothImage = clothing.toString(),
                                    context = context
                                )
                            },
                            colors = ButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                disabledContainerColor = Color.White,
                                disabledContentColor = Color.Black
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.star_ia),
                                contentDescription = "Star IA"
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Torne-se fúria!")
                        }

                        Card(
                            modifier = Modifier
                                .height(120.dp)
                                .width(120.dp)
                                .clickable { },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF0A1117).copy(alpha = 0.5f)
                            )
                        ) {
                            if (clothing != null) {
                                GlideImage(
                                    model = clothing,
                                    contentDescription = "Clothing Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}