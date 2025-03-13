package com.accountmanager.smartledger.presentation.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.accountmanager.smartledger.presentation.model.Account
import com.accountmanager.smartledger.presentation.viewmodel.AccountViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountScreen(context: Context, viewModel: AccountViewModel) {
    val accountList by viewModel.accounts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isDataFetched by viewModel.isDataFetched.collectAsState()

    LaunchedEffect(Unit) {
        if (isDataFetched.not()) {
            viewModel.checkInternetAndFetchAccounts(context)
        }
    }

    Box {
        // Show Progress Bar if API is loading
        if (isLoading && isDataFetched.not()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        // Show Accounts List
        LazyColumn {
            items(accountList, key = { it.actid.toString() }) { account ->
                val dismissState = rememberDismissState(
                    confirmStateChange = { dismissValue ->
                        when (dismissValue) {
                            DismissValue.DismissedToStart -> {
                                viewModel.deleteAccount(account)  // Delete on swipe left
                                true
                            }

                            else -> false
                        }
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(
                        DismissDirection.StartToEnd,
                        DismissDirection.EndToStart
                    ),
                    background = {
                        val color = when (dismissState.dismissDirection) {
                            DismissDirection.EndToStart -> Color.Red    // Delete action
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(16.dp)
                        ) {
                            if (dismissState.dismissDirection == DismissDirection.StartToEnd) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            } else if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    },
                    dismissContent = {
                        EditableAccountItem(account, viewModel)
                    }
                )
            }
        }
    }

}


@Composable
fun EditableAccountItem(account: Account, viewModel: AccountViewModel) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(account.ActName) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: ${account.actid}", style = MaterialTheme.typography.bodyLarge)

            if (isEditing) {
                TextField(
                    value = name.toString(),
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Row {
                    Button(onClick = {
                        viewModel.updateAccountName(account.actid ?: 0, name.toString())
                        isEditing = false
                    }) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                }
            } else {
                Text(text = "Name: $name", style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = { isEditing = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Name")
                }
            }
        }
    }
}
