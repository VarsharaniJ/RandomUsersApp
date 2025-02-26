package com.example.randomuserapp.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.randomuserapp.R
import com.example.randomuserapp.domain.model.User
import com.example.randomuserapp.presentation.viewmodels.UserListViewModel

@Composable
fun UserListScreen(navController: NavHostController, userViewModel: UserListViewModel) {
    val users = userViewModel.users.observeAsState(emptyList())
    val errorMsg by userViewModel.error.observeAsState()
    var userCount by remember { mutableStateOf("") } // Initially empty
    var errorMessage by remember { mutableStateOf("") } // For input validation errors
    var searchQuery by remember { mutableStateOf("") } // For filtering users

    val invalidNumberError = stringResource(R.string.please_enter_a_valid_number)
    val enterNoOfUSer = stringResource(R.string.please_enter_a_number_of_users)

    Column(modifier = Modifier.padding(16.dp)) {
        // User count input field
        UserCountInput(
            userCount = userCount,
            onUserCountChange = { newCount ->
                if (newCount.all { it.isDigit() }) {
                    errorMessage = "" // Clear error if input is valid
                    userCount = newCount
                } else {
                    errorMessage = invalidNumberError
                }
            }
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Submit button to fetch users
        Button(
            onClick = {
                if (userCount.isNotEmpty()) {
                    userViewModel.fetchUsers(userCount.toInt())
                } else {
                    errorMessage = enterNoOfUSer
                }
            },
            enabled = userCount.isNotEmpty() && errorMessage.isEmpty()
        ) {
            Text(stringResource(R.string.submit))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display internet connection error below submit button if necessary
        if (!errorMsg.isNullOrEmpty()) {
            Text(
                text = errorMsg!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Search bar for filtering users
        if (users.value.isNotEmpty()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                },
                label = { Text(stringResource(R.string.search_users)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Display filtered users
        val filteredUsers = if (searchQuery.isEmpty()) {
            users.value
        } else {
            users.value.filter {
                it.firstName.contains(searchQuery, ignoreCase = true) ||
                        it.lastName.contains(searchQuery, ignoreCase = true) ||
                        it.address.contains(searchQuery, ignoreCase = true)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(filteredUsers) { index, user ->
                UserCard(user = user, onClick = {
                    Log.d("filtered user", "index : $index")
                    navController.navigate(Screen.USER_DETAILS_SCREEN+"/$index")
                })
            }
        }
    }
}


@Composable
fun UserCountInput(userCount: String, onUserCountChange: (String) -> Unit) {
    OutlinedTextField(
        value = userCount,
        onValueChange = { newCount ->
            // Filter out non-digit characters and ensure the value is within the range of 1 to 5000
            val sanitizedInput = newCount.filter { it.isDigit() }

            // Convert to number and check if it's within the desired range
            val number = sanitizedInput.toIntOrNull()
            if (number != null && number in 1..5000) {
                onUserCountChange(sanitizedInput)
            }
        },
        label = { Text(stringResource(R.string.enter_number_of_users)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(user: User, onClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp), onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(model = user.profilePicture, contentDescription = stringResource(R.string.profile_picture), modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "${user.firstName} ${user.lastName}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "${user.address}, ${user.address}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
