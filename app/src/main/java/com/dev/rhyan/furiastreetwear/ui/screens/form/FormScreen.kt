package com.dev.rhyan.furiastreetwear.ui.screens.form

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dev.rhyan.furiastreetwear.R
import com.dev.rhyan.furiastreetwear.ui.viewmodels.FormDataViewModel

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel : FormDataViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var expandedSocialMidias by remember { mutableStateOf(false) }
    var expandedOutfits by remember { mutableStateOf(false) }
    var expandedTypes by remember { mutableStateOf(false) }

    val redes = listOf("Instagram", "Twitter/X", "TikTok", "Twitch", "YouTube")
    val opcoesCompra = listOf("Sim", "Não")
    val tipos = listOf("Casual", "Esportivo", "Street", "Techwear", "Minimalista", "Oversized")

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
                modifier = Modifier.size(60.dp),
                border = BorderStroke(2.dp, Color.White),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    shape = CircleShape
                ) {
                    GlideImage(
                        model = R.drawable.banner,
                        contentDescription = "ProfileImage",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Fala Furioso! Curtiu o app? Conta pra gente sua experiência e desbloqueie novidades exclusivas!",
            fontSize = 11.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.nome,
                onValueChange = { uiState.onNameChanged(it) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.White, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    disabledContainerColor = Color.Gray.copy(0.2f),
                    focusedContainerColor = Color.Gray.copy(0.2f),
                    unfocusedContainerColor = Color.Gray.copy(0.2f),
                ),
                placeholder = { Text("Nome Completo", color = Color.White) }
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { uiState.onEmailChanged(it) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.White, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    disabledContainerColor = Color.Gray.copy(0.2f),
                    focusedContainerColor = Color.Gray.copy(0.2f),
                    unfocusedContainerColor = Color.Gray.copy(0.2f),
                ),
                placeholder = { Text("E-mail", color = Color.White) }
            )

            ExposedDropdownMenuBox(
                expanded = expandedSocialMidias,
                onExpandedChange = { expandedSocialMidias = !expandedSocialMidias }
            ) {
                OutlinedTextField(
                    value = uiState.redeFavorita,
                    onValueChange = {  },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .border(2.dp, Color.White, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        disabledContainerColor = Color.Gray.copy(0.2f),
                        focusedContainerColor = Color.Gray.copy(0.2f),
                        unfocusedContainerColor = Color.Gray.copy(0.2f),
                    ),
                    placeholder = {
                        Text("Rede favorita para acompanhar E-Sports", color = Color.White)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedSocialMidias) }
                )

                ExposedDropdownMenu(
                    expanded = expandedSocialMidias,
                    onDismissRequest = { expandedSocialMidias = false }
                ) {
                    redes.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                uiState.onRedeFavoritaChanged(item)
                                expandedSocialMidias = false
                            }
                        )
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = expandedTypes,
                onExpandedChange = { expandedTypes = !expandedTypes }
            ) {
                OutlinedTextField(
                    value = uiState.estiloFavorito,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .border(2.dp, Color.White, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        disabledContainerColor = Color.Gray.copy(0.2f),
                        focusedContainerColor = Color.Gray.copy(0.2f),
                        unfocusedContainerColor = Color.Gray.copy(0.2f),
                    ),
                    placeholder = {
                        Text("Qual seu estilo de roupas favorito?", color = Color.White)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTypes) }
                )

                ExposedDropdownMenu(
                    expanded = expandedTypes,
                    onDismissRequest = { expandedTypes = false }
                ) {
                    tipos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                uiState.onEstiloFavoritoChanged(item)
                                expandedTypes = false
                            }
                        )
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = expandedOutfits,
                onExpandedChange = { expandedOutfits = !expandedOutfits }
            ) {
                OutlinedTextField(
                    value = uiState.comprouProduto,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .border(2.dp, Color.White, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        disabledContainerColor = Color.Gray.copy(0.2f),
                        focusedContainerColor = Color.Gray.copy(0.2f),
                        unfocusedContainerColor = Color.Gray.copy(0.2f),
                    ),
                    placeholder = {
                        Text("Você já comprou produtos da Fúria?", color = Color.White)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedOutfits) }
                )

                ExposedDropdownMenu(
                    expanded = expandedOutfits,
                    onDismissRequest = { expandedOutfits = false }
                ) {
                    opcoesCompra.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                uiState.onProdutoChanged(item)
                                expandedOutfits = false
                            }
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Checkbox(
                    checked = uiState.compartilhaNoticias,
                    onCheckedChange = { uiState.onSharedNoticesChanged(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color.White
                    )
                )
                Text(
                    text = "Permitir que a Fúria compartilhe notícias",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 60.dp, end = 60.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                   viewModel.salvarFormularioFirestore(onResult = {
                       if(it == true) {
                           Toast.makeText(context, "Formulário cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                       } else {
                           Toast.makeText(context, "Ops, ocorreu um erro ao cadastrar seu formulário, tente novamente", Toast.LENGTH_LONG).show()
                       }
                   })
                },
                colors = ButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Enviar",
                    color = Color.Black
                )
            }
        }
    }
}
