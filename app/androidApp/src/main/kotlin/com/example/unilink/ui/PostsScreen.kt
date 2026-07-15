package com.example.unilink.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unilink.models.Post
import com.example.unilink.viewmodel.PostViewModel

@Composable
fun PostsScreen(
    viewModel: PostViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Publicaciones",
                style = MaterialTheme.typography.headlineSmall
            )
            TextButton(onClick = onBack) {
                Text("Volver")
            }
        }

        OutlinedTextField(
            value = state.title,
            onValueChange = viewModel::updateTitle,
            label = { Text("Titulo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.content,
            onValueChange = viewModel::updateContent,
            label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.author,
            onValueChange = viewModel::updateAuthor,
            label = { Text("Autor") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = viewModel::savePost,
                enabled = !state.isLoading
            ) {
                Text(if (state.editingPostId == null) "Publicar" else "Actualizar")
            }
            TextButton(
                onClick = viewModel::clearForm,
                enabled = !state.isLoading
            ) {
                Text("Limpiar")
            }
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(state.posts, key = { it.id }) { post ->
                PostRow(
                    post = post,
                    onEdit = { viewModel.selectPost(post) },
                    onDelete = { viewModel.deletePost(post.id) }
                )
            }
        }
    }
}

@Composable
private fun PostRow(
    post: Post,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = post.content,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Por ${post.author}",
            style = MaterialTheme.typography.bodySmall
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onEdit) {
                Text("Editar")
            }
            TextButton(onClick = onDelete) {
                Text("Eliminar")
            }
        }
        HorizontalDivider()
    }
}
