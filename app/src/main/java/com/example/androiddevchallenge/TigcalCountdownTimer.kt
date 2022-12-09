package com.example.androiddevchallenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.typography

@ExperimentalAnimationApi
@Composable
fun TigcalCountDownTimer(viewModel: TimerViewModel = viewModel()) {
    val inputMode by viewModel.isInputMode.observeAsState(true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        AnimatedVisibility(visible = inputMode, enter = slideInHorizontally()) {
            TimerInput(viewModel)
        }

        AnimatedVisibility(visible = !inputMode, enter = slideInVertically()) {
            TimerOutput(viewModel)
        }
    }
}

@Composable
fun TimerInput(viewModel: TimerViewModel) {
    var hour by rememberSaveable { mutableStateOf(0f) }
    var minute by rememberSaveable { mutableStateOf(0f) }
    var second by rememberSaveable {
        mutableStateOf(0f)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.timer_hour),
            style = typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        TimerSlider(
            hour, onValueChange = { hour = it }
        )
        Text(
            text = stringResource(id = R.string.timer_minute),
            style = typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        TimerSlider(
            minute,
            onValueChange = { minute = it }
        )
        Text(
            text = stringResource(id = R.string.timer_second),
            style = typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        TimerSlider(
            second,
            onValueChange = { second = it }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    startTimer(viewModel, hour.toLong(), minute.toLong(), second.toLong())
                },
            ) {
                Text(stringResource(id = R.string.action_start), style = typography.h6)
            }
        }
    }
}

@Composable
fun TimerSlider(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Text(text = value.toLong().toString())
    Slider(
        value = value, valueRange = 0f..59f, steps = 0,
        onValueChange = { onValueChange(it) }
    )
}

fun startTimer(viewModel: TimerViewModel, hours: Long, minutes: Long, seconds: Long) {
    viewModel.startTimer(hours, minutes, seconds)
}

@ExperimentalAnimationApi
@Composable
fun TimerOutput(viewModel: TimerViewModel) {
    val timeRemaining: String by viewModel.timeRemaining.observeAsState("")
    val timesUp: Boolean by viewModel.isTimesUp.observeAsState(false)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(
            text = timeRemaining,
            style = typography.h1,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colors.primary
        )
        Button(
            onClick = { viewModel.stopTimer() },
        ) {
            Text(stringResource(id = R.string.action_stop), style = typography.h6)
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(128.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(visible = timesUp, enter = slideInVertically()) {
                Card(
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    backgroundColor = MaterialTheme.colors.secondary
                ) {
                    Text(
                        text = stringResource(id = R.string.message_times_up),
                        color = MaterialTheme.colors.onSecondary,
                        style = typography.h3,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview("Light Timer", widthDp = 360, heightDp = 640)
@Composable
fun LightTimer() {
    MyTheme {
        TimerOutput(viewModel())
    }
}

@ExperimentalAnimationApi
@Preview("Dark Timer", widthDp = 360, heightDp = 640)
@Composable
fun DarkTimer() {
    MyTheme(darkTheme = true) {
        TigcalCountDownTimer(viewModel())
    }
}
