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
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    val isSuccess by viewModel.isSuccess.collectAsState()
    val showError by viewModel.showError.collectAsState()
    val showHint by viewModel.showHint.collectAsState()

    var showOverlay by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Loading Animation Effect
    LaunchedEffect(Unit) {
        delay(750)
        isLoading = false
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            showOverlay = true
        }
    }

    LaunchedEffect(showError) {
        if (showError) {
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
                    Text(
                        text = "Level 1",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                    IconButton(
                        onClick = { /* Grid view or Coming Soon comment */ },
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

                // Riddle Content Container
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "4, 8, 16, ?",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 2.sp
                            ),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Find the missing number in the sequence",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                // Custom Keyboard
                CustomKeyboard(
                    currentAnswer = currentAnswer,
                    onDigit = { viewModel.inputDigit(it) },
                    onDelete = { viewModel.deleteDigit() },
                    onHint = { viewModel.toggleHint() },
                    onEnter = { viewModel.submitAnswer() }
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
                text = { Text("Pattern: Multiply by 2\n\n4 x 2 = 8\n8 x 2 = 16\n16 x 2 = ?", color = Color.LightGray) },
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
                            text = "The sequence doubles each step.\n4 -> 8 -> 16 -> 32",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Text(
                            text = "Next Level Coming Soon",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = CorrectColor),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
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
        border = BorderStroke(1.dp, if (isPressed) Color.White else GlowColor),
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
