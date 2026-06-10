package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CorrectColor
import com.example.ui.theme.GlowColor
import kotlinx.coroutines.delay

@Composable
fun MathRiddlesScreen(
    viewModel: RiddleViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val currentAnswer by viewModel.currentAnswer.collectAsState()
    val isLevelSuccess by viewModel.isLevelSuccess.collectAsState()
    val isGameCompleted by viewModel.isGameCompleted.collectAsState()
    val showError by viewModel.showError.collectAsState()
    val showHint by viewModel.showHint.collectAsState()
    val currentLevelIndex by viewModel.currentLevelIndex.collectAsState()

    val currentRiddle = viewModel.riddles[currentLevelIndex]

    var showOverlay by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val haptic = LocalHapticFeedback.current

    // Loading Animation Effect
    LaunchedEffect(currentLevelIndex) {
        isLoading = true
        delay(750)
        isLoading = false
    }

    LaunchedEffect(isLevelSuccess) {
        if (isLevelSuccess) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            showOverlay = true
            delay(2500) // Show success dialog for 2.5 seconds
            showOverlay = false
            viewModel.nextLevel()
        }
    }

    LaunchedEffect(showError) {
        if (showError) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            snackbarHostState.showSnackbar("Wrong Answer")
            viewModel.dismissError()
        }
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
        if (isLoading) {
            // Elegant loading spinner
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "LOADING RIDDLE...",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 2.sp
                    )
                )
            }
        } else if (isGameCompleted) {
            // Premium Completion Screen
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "CONGRATULATIONS",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    ),
                    color = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "More Levels Coming Soon",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = { viewModel.restartGame() },
                    colors = ButtonDefaults.buttonColors(containerColor = GlowColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(
                        "RESTART PUZZLES",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(
                        "BACK TO HOME",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        color = Color.White
                    )
                }
            }
        } else {
            // Screen Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .statusBarsPadding()
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Level ${currentRiddle.levelNumber}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "LEVEL ${currentRiddle.levelNumber} / 5",
                            color = GlowColor,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        )
                    }
                    IconButton(
                        onClick = { /* Grid view */ },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                    ) {
                        Icon(
                            Icons.Default.GridView,
                            contentDescription = "Grid",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                // Progress Bar at the top
                LinearProgressIndicator(
                    progress = { currentRiddle.levelNumber / 5f },
                    modifier = Modifier.fillMaxWidth().height(4.dp).padding(vertical = 8.dp),
                    color = GlowColor,
                    trackColor = Color.DarkGray
                )

                // Riddle Content Container with Animation
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = currentRiddle.question,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                        },
                        label = "riddle_animation"
                    ) { targetQuestion ->
                        // Glassmorphism card effect design
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E22).copy(alpha = 0.6f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = targetQuestion,
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 2.sp
                                    ),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Find the missing number",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        letterSpacing = 2.sp
                                    ),
                                    color = GlowColor
                                )
                            }
                        }
                    }
                }

                // Custom Keyboard
                CustomKeyboard(
                    currentAnswer = currentAnswer,
                    onDigit = { 
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.inputDigit(it) 
                    },
                    onDelete = { 
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.deleteDigit() 
                    },
                    onHint = { 
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.toggleHint() 
                    },
                    onEnter = { 
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.submitAnswer() 
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (showHint) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissHint() },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissHint() }) {
                        Text("OK", color = Color.White)
                    }
                },
                title = { Text("Information Hint", color = Color.White) },
                text = { Text(currentRiddle.hint, color = Color.LightGray) },
                containerColor = Color(0xFF1E1E22)
            )
        }

        // Success dialog overlay
        if (showOverlay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E22)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, CorrectColor),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Correct",
                            tint = CorrectColor,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "CORRECT ANSWER!",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            ),
                            color = CorrectColor
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = currentRiddle.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Text(
                            text = "Loading Next Level...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = GlowColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(
                            color = CorrectColor,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomKeyboard(
    currentAnswer: String,
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    onHint: () -> Unit,
    onEnter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // Top Row: Answer, Clear, Hint, Enter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Answer display field
            Surface(
                color = Color(0xFF1E1E22),
                border = BorderStroke(1.dp, GlowColor),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1.5f)
                    .height(56.dp)
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = currentAnswer.ifEmpty { "ENTER ANSWER" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            letterSpacing = 1.5.sp
                        ),
                        color = if (currentAnswer.isEmpty()) Color.DarkGray else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            KeyButton(icon = Icons.Default.Clear, onClick = onDelete)
            KeyButton(icon = Icons.Default.Lightbulb, onClick = onHint)
            KeyButton(text = "ENTER", onClick = onEnter, isPrimary = true, weight = 1.2f)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Numbers Top Row: 1..5
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 1..5) {
                KeyButton(text = i.toString(), onClick = { onDigit(i.toString()) }, weight = 1f)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Numbers Bottom Row: 6..9, 0
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 6..9) {
                KeyButton(text = i.toString(), onClick = { onDigit(i.toString()) }, weight = 1f)
            }
            KeyButton(text = "0", onClick = { onDigit("0") }, weight = 1f)
        }
    }
}

@Composable
fun RowScope.KeyButton(
    text: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    weight: Float? = null,
    isPrimary: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scaleFactor by animateFloatAsState(targetValue = if (isPressed) 0.88f else 1.0f)

    val modifier = if (weight != null) Modifier.weight(weight) else Modifier.width(60.dp)
    
    Surface(
        color = if (isPrimary) CorrectColor else Color(0xFF1E1E22),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (isPressed) Color.White else (if (isPrimary) CorrectColor else GlowColor)),
        modifier = modifier
            .height(56.dp)
            .scale(scaleFactor)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else if (text != null) {
                Text(
                    text = text,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
