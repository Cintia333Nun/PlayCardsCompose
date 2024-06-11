package com.example.playcardscompose

import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.playcardscompose.ui.theme.PlayCardsComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun GamePlayScreen() {
    val context = LocalContext.current
    val mediaPlayerSpin = remember {
        MediaPlayer.create(context, R.raw.spin)
    }
    val mediaPlayerWin = remember {
        MediaPlayer.create(context, R.raw.win)
    }
    val mediaPlayerHighScore = remember {
        MediaPlayer.create(context, R.raw.highscore)
    }

    var playerCard by remember { mutableIntStateOf(14) }
    var cpuCard by remember { mutableIntStateOf(14) }

    val playerImageResource = getImageResource(card = playerCard)
    val cpuImageResource = getImageResource(card = cpuCard)

    var playerScore by remember { mutableIntStateOf(0) }
    var cpuScore by remember { mutableIntStateOf(0) }

    val onClickNewCards = {
        cpuCard = getCardsPlay()
        val cpuRandom = cpuCard

        playerCard = getCardsPlay()
        val playerRandom = playerCard

        if (playerRandom > cpuRandom) {
            playerScore += 1
        } else if (cpuRandom > playerRandom) {
            cpuScore += 1
        }

        val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
        vibrator?.vibrate(
            VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
        )

        CoroutineScope(Dispatchers.Default).launch {
            mediaPlayerSpin.start()
        }
    }
    val onClickRestart = {
        playerScore = 0
        cpuScore = 0
        playerCard = 14
        cpuCard = 14
        CoroutineScope(Dispatchers.Default).launch {
            mediaPlayerSpin.start()
        }
    }

    BackgroundBox()
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Logo()
        }
        CardsPlay(
            playerImageResource = playerImageResource,
            cpuImageResource = cpuImageResource
        )
        Spacer(modifier = Modifier.height(64.dp))
        ButtonsContainer(onClickNewCards, onClickRestart)
        Spacer(modifier = Modifier.height(64.dp))
        ScoresContainer(playerScore = playerScore, cpuScore = cpuScore)
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayerSpin.release()
        }
    }
}

@Composable
fun BackgroundBox() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun Logo() {
    Image(
        modifier = Modifier.size(200.dp),
        painter = painterResource(id = R.drawable.logo),
        contentDescription = null,
    )
}

@Composable
fun CardsPlay(playerImageResource: Int, cpuImageResource: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = playerImageResource),
            contentDescription = null
        )
        Image(
            painter = painterResource(id = cpuImageResource),
            contentDescription = null
        )
    }
}

@Composable
fun ButtonsContainer(
    onclickPlay: () -> Job,
    onclickRestart: () -> Job
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(text = "Jugar", onclick = onclickPlay)
        Button(text = "Restaurar", onclick = onclickRestart)
    }
}

@Composable
fun Button(text: String, onclick: () -> Job) {
    androidx.compose.material3.Button(
        onClick = { onclick.invoke() },
        modifier = Modifier
            .height(60.dp)
            .width(150.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ScoresContainer(playerScore: Int, cpuScore: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ScoreText(text = "Jugar", score = playerScore)
        ScoreText(text = "Dispositivo", score = cpuScore)
    }
}

@Composable
fun ScoreText(text: String, score: Int) {
    Text(
        text = "$text: $score",
        color = Color.White,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun getImageResource(card: Int): Int {
    return when (card) {
        1 -> R.drawable.card2
        2 -> R.drawable.card2
        3 -> R.drawable.card3
        4 -> R.drawable.card4
        5 -> R.drawable.card5
        6 -> R.drawable.card6
        7 -> R.drawable.card7
        8 -> R.drawable.card8
        9 -> R.drawable.card9
        10 -> R.drawable.card10
        11 -> R.drawable.card11
        12 -> R.drawable.card12
        13 -> R.drawable.card13
        else -> R.drawable.card14
    }
}

fun getCardsPlay()= (2..14).random()

@Preview(showBackground = true)
@Composable
fun GamePlayScreenPreview() {
    PlayCardsComposeTheme {
        GamePlayScreen()
    }
}