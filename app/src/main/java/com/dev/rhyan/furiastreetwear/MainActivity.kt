package com.dev.rhyan.furiastreetwear

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.rhyan.furiastreetwear.data.utils.GoogleAuthClient
import com.dev.rhyan.furiastreetwear.ui.components.BottomNavigator
import com.dev.rhyan.furiastreetwear.ui.screens.home.HomeScreen
import com.dev.rhyan.furiastreetwear.ui.screens.login.LoginScreen
import com.dev.rhyan.furiastreetwear.ui.screens.try_on.TryOnScreen
import com.dev.rhyan.furiastreetwear.ui.theme.FuriaStreetwearTheme
import com.dev.rhyan.furiastreetwear.ui.viewmodels.AuthenticateViewModel
import com.dev.rhyan.furiastreetwear.ui.viewmodels.ImageProcessViewModel
import com.dev.rhyan.furiastreetwear.ui.viewmodels.ProductViewModel
import com.google.android.gms.auth.api.identity.Identity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dev.rhyan.furiastreetwear.data.models.response.UserDataProvider
import com.dev.rhyan.furiastreetwear.ui.screens.form.FormScreen
import com.dev.rhyan.furiastreetwear.ui.state.LoginUIState
import com.dev.rhyan.furiastreetwear.ui.viewmodels.FormDataViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val googleAuthClient by lazy {
            GoogleAuthClient(
                context = applicationContext,
                onTapClient = Identity.getSignInClient(applicationContext)
            )
        }

        setContent {
            FuriaStreetwearTheme {

                val navHostController: NavHostController = rememberNavController()
                val productViewModel = koinViewModel<ProductViewModel>()
                val imageViewModel = koinViewModel<ImageProcessViewModel>()
                val loginViewModel = koinViewModel<AuthenticateViewModel>()
                val formViewModel = koinViewModel<FormDataViewModel>()
                val session = remember { mutableStateOf<UserDataProvider?>(null) }

                val uiState = loginViewModel.loginState.collectAsStateWithLifecycle()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        Log.d("MainActivity", "Intent data: ${result.data}")
                        if (result.resultCode == RESULT_OK) {
                            val data = result.data
                            if (data == null) {
                                Log.e("Login", "Intent data is null")
                                return@rememberLauncherForActivityResult
                            }
                            loginViewModel.signInWithIntent(
                                googleAuthClient = googleAuthClient,
                                intent = data
                            )
                        } else {
                            Log.e("Login", "Intent result is not OK, resultCode: ${result.resultCode}")
                        }
                    }
                )

                val currentDestination by navHostController.currentBackStackEntryAsState()
                val currentScreen = currentDestination?.destination?.route

                LaunchedEffect(uiState.value) {
                    when (val state = uiState.value) {
                        is LoginUIState.isEmpty -> {
                            navHostController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        is LoginUIState.Error -> {
                            Toast.makeText(
                                applicationContext,
                                state.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is LoginUIState.Sucess -> {
                            session.value = state.data
                            if (navHostController.currentDestination?.route == "login") {
                                navHostController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                    }
                }

                Scaffold(
                    bottomBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (currentScreen != "login") {
                                BottomNavigator(onSelectedDestination = { route ->
                                    navHostController.navigate(route) {
                                        popUpTo(navHostController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                })
                            }
                        }
                    }
                ) {
                    Box() {
                        Image(
                            painter = painterResource(R.drawable.background_main),
                            contentDescription = "Background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box() {
                            NavHost(navController = navHostController, startDestination = "login") {
                                composable("login") {
                                    LoginScreen(onLogin = {
                                        lifecycleScope.launch {
                                            val signInSender = googleAuthClient.signIn()
                                            Log.d("MainActivity", "signInSender: $signInSender")
                                            if (signInSender != null) {
                                                launcher.launch(
                                                    IntentSenderRequest.Builder(signInSender).build()
                                                )
                                            } else {
                                                Log.e("MainActivity", "Failed to obtain IntentSender")
                                            }
                                        }
                                    })
                                }
                                composable("home") {
                                    HomeScreen(
                                        viewModel = productViewModel,
                                        session = session.value
                                    )
                                }
                                composable("try-on") {
                                    TryOnScreen(
                                        viewModel = imageViewModel,
                                        sharedViewModel = productViewModel,
                                        session = session.value
                                    )
                                }
                                composable("form") {
                                    FormScreen(
                                        viewModel =formViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
