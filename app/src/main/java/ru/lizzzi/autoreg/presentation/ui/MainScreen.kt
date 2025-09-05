package ru.lizzzi.autoreg.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.lizzzi.autoreg.R


@Composable
fun MainScreen(
    mainViewModel: MainViewModel = viewModel(),
    onNewRegionClick: (String) -> Unit,
) {
    val viewState by mainViewModel.viewState.collectAsState()

    val textState = rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { newValue -> textState.value = newValue },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = 25.sp),
            modifier = Modifier.padding(top = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = colorResource(R.color.colorPrimaryDark)
            ),
            label =  {
                if (viewState.codeRegion.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.region),
                        color = Color.Black
                    )
                } },
            keyboardActions = KeyboardActions(
                onDone = {
                    if (textState.value.isNotEmpty() && textState.value.toInt() > 0) {
                        focusManager.clearFocus()
                    }
                    onNewRegionClick(textState.value)
                }
            ),
        )
        
        Button(
            onClick = {
                if (textState.value.isNotEmpty() && textState.value.toInt() > 0) {
                    focusManager.clearFocus()
                }
                onNewRegionClick(textState.value)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.colorPrimaryDark)
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .width(150.dp)
                .height(50.dp)
        ) {
            Text(text = stringResource(id = R.string.buttonSendRequest))
        }

        if (viewState.regionName.isNotEmpty()) {
            Text(
                text = viewState.regionName,
                modifier = Modifier.padding(top = 20.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        if (viewState.allCodeRegion.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.otherCode),
                modifier = Modifier.padding(top = 15.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)
            Text(
                text = viewState.allCodeRegion,
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}


@Preview("Main screen", showBackground = true)
@Composable
private fun ShowMainScreen() {
    MainScreen {}
}

