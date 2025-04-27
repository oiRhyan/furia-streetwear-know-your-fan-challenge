package com.dev.rhyan.furiastreetwear.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dev.rhyan.furiastreetwear.R
import com.dev.rhyan.furiastreetwear.data.models.response.ProductsRequest


@Composable
fun ProductCardItem(
    onTryClothing : (String) -> Unit = {},
    onLinkClicked : (String) -> Unit = {},
    product : ProductsRequest
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 9.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B1A2A).copy(0.5f)
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            ShowItemImage(product.image)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                FilledIconButton(
                    onClick = {
                        onLinkClicked(product.link)
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(Color.White, Color.Black),
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.chrome_fill),
                        contentDescription = "Go to Link",
                        tint = Color.Black,
                        modifier = Modifier.padding(6.dp).size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                FilledIconButton(
                    onClick = {
                        onTryClothing(product.image)
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(Color.White, Color.Black),
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.tryon),
                        contentDescription = "Try-On",
                        tint = Color.Black,
                        modifier = Modifier.padding(6.dp).size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ShowItemImage(image : String) {
    GlideImage(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(20.dp))
            .size(190.dp, 120.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxSize(),
        model = image,
        contentDescription = "Imagem do Produto",
        contentScale = ContentScale.FillWidth
    )
}