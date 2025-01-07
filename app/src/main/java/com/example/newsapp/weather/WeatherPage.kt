import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsapp.weather.RetrofitInstance
import com.example.newsapp.weather.WeatherResponse
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

@Composable
fun WeatherPage(navController: NavHostController, onNavigate: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isFetchingLocation by remember { mutableStateOf(true) }
    var shouldRefreshWeather by remember { mutableStateOf(true) }

    // Your API Key
    val apiKey = "7117347fe5070ff05c3d04792c6e5476"

    fun fetchWeather(latitude: Double, longitude: Double) {
        coroutineScope.launch {
            try {
                isFetchingLocation = true
                errorMessage = null
                weather = RetrofitInstance.api.getWeather(latitude, longitude, apiKey)
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isFetchingLocation = false
            }
        }
    }

    // Request location and fetch weather
    if (shouldRefreshWeather) {
        shouldRefreshWeather = false
        isFetchingLocation = true
        GetLocation { latitude, longitude ->
            fetchWeather(latitude, longitude)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isFetchingLocation -> CircularProgressIndicator()
            weather != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    WeatherInfo(weather = weather!!)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            shouldRefreshWeather = true
                        }
                    ) {
                        Text("Refresh")
                    }
                }
            }
            errorMessage != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            shouldRefreshWeather = true
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}


@SuppressLint("MissingPermission")
@Composable
fun GetLocation(onLocationRetrieved: (latitude: Double, longitude: Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    onLocationRetrieved(location.latitude, location.longitude)
                } else {
                    // Handle case where location is null
                    onLocationRetrieved(0.0, 0.0) // Placeholder for testing
                }
            }.addOnFailureListener {
                // Handle failure in getting location
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}

@Composable
fun WeatherInfo(weather: WeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Weather in ${weather.name}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Temperature: ${weather.main.temp}°C",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Humidity: ${weather.main.humidity}%",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Feels Like: ${weather.main.feels_like}°C",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Condition: ${weather.weather[0].description}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
