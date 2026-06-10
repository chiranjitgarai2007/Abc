package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlowColor
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: RiddleViewModel,
    onNavigateToRiddles: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()
    
    val currentLevel by viewModel.highestUnlockedLevel.collectAsState(initial = 0)
    val gameCompleted by viewModel.storage.gameCompleted.collectAsState(initial = false)
    val soundEnabled by viewModel.soundEnabled.collectAsState(initial = true)
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsState(initial = true)
    
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    val showComingSoon = {
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Coming Soon")
        }
    }

    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = {
                Text(
                    text = "SETTINGS",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sound Effects", color = Color.LightGray)
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { coroutineScope.launch { viewModel.storage.toggleSound() } },
                            colors = SwitchDefaults.colors(checkedTrackColor = GlowColor)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Vibration", color = Color.LightGray)
                        Switch(
                            checked = vibrationEnabled,
                            onCheckedChange = { coroutineScope.launch { viewModel.storage.toggleVibration() } },
                            colors = SwitchDefaults.colors(checkedTrackColor = GlowColor)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showResetConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA52A2A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("RESET PROGRESS", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("CLOSE", color = GlowColor)
                }
            },
            containerColor = Color(0xFF1E1E22)
        )
    }

    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("Are you sure?", color = Color.White) },
            text = { Text("This will delete all completed levels and start from Level 1.", color = Color.LightGray) },
            confirmButton = {
                TextButton(
                    onClick = { 
                        viewModel.restartGame()
                        showResetConfirmDialog = false
                        showSettingsDialog = false
                        coroutineScope.launch { snackbarHostState.showSnackbar("Progress Reset") }
                    }
                ) {
                    Text("YES, RESET", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("CANCEL", color = Color.LightGray)
                }
            },
            containerColor = Color(0xFF1E1E22)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A1D),
                        Color(0xFF111113),
                        Color(0xFF070708)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Section Bar with custom status/settings controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showSettingsDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { showComingSoon() },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Achievement",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { showComingSoon() },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Leaderboard,
                        contentDescription = "Leaderboard",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.2f))

            // App Brand Section
            Text(
                text = "MATH",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 16.sp,
                    fontSize = 80.sp
                ),
                color = Color.White
            )
            
            Text(
                text = "Ver 2.15",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    letterSpacing = 2.sp
                ),
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Navigation Options
            if (currentLevel > 0 || gameCompleted) {
                MenuButton(
                    text = "CONTINUE",
                    onClick = {
                        viewModel.loadSavedGame()
                        onNavigateToRiddles()
                    },
                    modifier = Modifier.shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = Color.White,
                        spotColor = Color.White
                    )
                )
                
                Spacer(modifier = Modifier.height(18.dp))
                
                MenuButton(
                    text = "MATH RIDDLES",
                    onClick = {
                         coroutineScope.launch { snackbarHostState.showSnackbar("Restart progress in Settings to start over") }
                    }
                )
            } else {
                MenuButton(
                    text = "MATH RIDDLES",
                    onClick = onNavigateToRiddles
                )
            }
            
            Spacer(modifier = Modifier.height(18.dp))
            
            MenuButton(
                text = "CROSS PUZZLES",
                onClick = { showComingSoon() }
            )
            
            Spacer(modifier = Modifier.height(18.dp))
            
            MenuButton(
                text = "DAILY CHALLENGES",
                onClick = { showComingSoon() }
            )

            Spacer(modifier = Modifier.weight(1.5f))

            // Follow block content
            Text(
                text = "FOLLOW US",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 4.sp
                ),
                color = Color.DarkGray
            )
            
            Spacer(modifier = Modifier.height(14.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                SocialIcon(icon = Icons.Default.ThumbUp, onClick = { showComingSoon() })
                SocialIcon(icon = Icons.Default.Tag, onClick = { showComingSoon() })
                SocialIcon(icon = Icons.Default.Share, onClick = { showComingSoon() })
            }
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scaleFactor by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1.0f)

    Surface(
        color = Color(0xFF1E1E22),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isPressed) Color.White else GlowColor),
        modifier = modifier
            .fillMaxWidth(0.85f)
            .height(60.dp)
            .scale(scaleFactor)
            .shadow(
                elevation = if (isPressed) 2.dp else 12.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = GlowColor,
                spotColor = GlowColor
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp
                ),
                color = Color.White
            )
        }
    }
}

@Composable
fun SocialIcon(icon: ImageVector, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scaleFactor by animateFloatAsState(targetValue = if (isPressed) 0.90f else 1.0f)

    Surface(
        color = Color(0xFF151518),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isPressed) Color.White else GlowColor),
        modifier = Modifier
            .size(52.dp)
            .scale(scaleFactor)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
