package com.example.randomuserapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.randomuserapp.R
import com.example.randomuserapp.presentation.viewmodels.UserListViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    index: Int,
    userViewModel: UserListViewModel,
    navController: NavController
) {
    val user = userViewModel.users.observeAsState(emptyList()).value.getOrNull(index)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_details)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (user != null) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(text = user.address, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    AsyncImage(
                        model = user.profilePicture,
                        contentDescription = stringResource(R.string.profile_picture),
                        modifier = Modifier.size(150.dp)
                    )
                }
            } else {
                Text(
                    text = "User not found",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red
                )
            }
        }
    }
}
