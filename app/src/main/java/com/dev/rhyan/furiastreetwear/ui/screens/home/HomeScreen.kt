package com.dev.rhyan.furiastreetwear.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.dev.rhyan.furiastreetwear.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dev.rhyan.furiastreetwear.data.models.response.UserDataProvider
import com.dev.rhyan.furiastreetwear.ui.components.ProductCardItem
import com.dev.rhyan.furiastreetwear.ui.state.HomeUiState
import com.dev.rhyan.furiastreetwear.ui.viewmodels.ProductViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(
    viewModel: ProductViewModel,
    session: UserDataProvider?
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp)
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
        Column(
            modifier = Modifier
                .height(650.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, end = 20.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.banner_furia_slogan),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Vista fúria",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            when (val catalog = uiState) {
                is HomeUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 10.dp
                        )
                    }
                }

                is HomeUiState.Sucess -> {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(start = 30.dp, end = 30.dp)
                            .height(440.dp),
                        columns = GridCells.Fixed(2)
                    ) {
                        items(catalog.products) { product ->
                            ProductCardItem(product = product, onLinkClicked = { link ->
                                viewModel.goToProduct(context, link)
                            }, onTryClothing = { selected ->
                                viewModel.selectProduct(selected)
                            })
                        }
                    }
                }

                is HomeUiState.Error -> {

                }
            }
        }
    }
}
