package com.dev.rhyan.furiastreetwear.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.rhyan.furiastreetwear.R

@Composable
fun BottomNavigator(
    onSelectedDestination : (String) -> Unit = {}
) {

    var iconSelected by remember { mutableIntStateOf(R.drawable.catalog) }

    val icons = listOf<ScreenRoute>(
        ScreenRoute(
            route = "try-on",
            selected = R.drawable.tryon,
            unselected = R.drawable.tryon
        ),
        ScreenRoute(
            route = "home",
            selected = R.drawable.catalog,
            unselected = R.drawable.catalog
        ),
        ScreenRoute(
            route = "form",
            selected = R.drawable.world,
            unselected = R.drawable.world
        )
    )

    Card(
        modifier = Modifier
            .height(90.dp)
            .width(300.dp)
            .border(shape = RoundedCornerShape(200.dp), color = Color.White, width = 1.dp)
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray.copy(0.2f)
        ),
        shape = RoundedCornerShape(200.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                icons
            ) { icon ->
                Icon(
                    painter = if (iconSelected == icon.selected) painterResource(iconSelected) else painterResource(icon.unselected),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = if (iconSelected == icon.selected) Color.White else Color.Transparent,
                            shape = CircleShape,
                        ).padding(12.dp)
                        .clickable {
                            iconSelected = icon.selected
                            onSelectedDestination.invoke(icon.route)
                        },
                    tint = if (iconSelected == icon.selected) Color.Black else Color.White
                )
            }
        }
    }

}

@Preview()
@Composable
fun ShowRegisterBottomNav() {
    BottomNavigator()
}

data class ScreenRoute(
    val route : String,
    val selected: Int,
    val unselected: Int
)