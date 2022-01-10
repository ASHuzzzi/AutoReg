package ru.lizzzi.autoreg.presentation.ui

import android.os.Bundle
import android.widget.Toast
import android.view.Gravity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.lizzzi.autoreg.R

class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModelMain by viewModels()

    private val screenCallbacks = object : MainScreenCallbacks {
        override fun getNewRegion(newRegionCode: String) {
            viewModel.getRegion(newRegionCode)
        }

        override fun showMessage(toastTextResource: Int) {
            showToast(toastTextResource)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                viewModelMain =  viewModel,
                screenCallbacks = screenCallbacks
            )
        }

        viewModel.viewState.observe(this) {
            if (it.regionName.isEmpty()) {
                showToast(R.string.toastNoSuchRegion)
            }
        }
    }

    private fun showToast(toastTextResource: Int) {
        val toast = Toast.makeText(this, resources.getString(toastTextResource), Toast.LENGTH_LONG)
        with(toast) {
            this.setGravity(Gravity.CENTER, 0, 0)
            this.show()
        }
    }

}