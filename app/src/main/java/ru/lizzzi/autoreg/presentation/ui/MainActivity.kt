package ru.lizzzi.autoreg.presentation.ui

import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import android.widget.Toast
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.lizzzi.autoreg.R

class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModelMain by viewModels()

    private var editText: EditText? = null
    private var textRegion: TextView? = null
    private var textOtherCodeForRegion: TextView? = null
    private var textCode: TextView? = null

    private val inputMethodManager by lazy {
        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)

        initButtonShowRegion()

        textRegion = findViewById(R.id.textRegion)
        textOtherCodeForRegion = findViewById(R.id.textOtherCodeForRegion)
        textCode = findViewById(R.id.textCode)

        setObserveForRegion()
    }

    private fun initButtonShowRegion() {
        val buttonShowRegion = findViewById<Button>(R.id.buttonShowRegion)
        buttonShowRegion.setOnClickListener(View.OnClickListener {
            editText?.let {
                val enteredCode = it.text.toString()
                if (enteredCode.isEmpty()) {
                    hideRegionInfo()
                    showToast(R.string.toastEnterCodeOfRegion)
                    return@OnClickListener
                }
                if (enteredCode.toInt() > 0) {
                    getRegion(enteredCode)
                } else {
                    hideRegionInfo()
                    showToast(R.string.toastErrorCode)
                }
            }
        })
    }

    private fun setObserveForRegion() {
        viewModel.liveData.observe(this) { region ->
            if (region.regionName.isNotEmpty()) {
                inputMethodManager.hideSoftInputFromWindow(editText?.windowToken, 0)
                textRegion?.visibility = View.VISIBLE
                textRegion?.text = region.regionName
                if (region.allCodeRegion.isNotEmpty()) {
                    textCode?.visibility = View.VISIBLE
                    textOtherCodeForRegion?.visibility = View.VISIBLE
                    textOtherCodeForRegion?.text = region.allCodeRegion
                }
            } else {
                hideRegionInfo()
                showToast(R.string.toastNoSuchRegion)
            }
        }
    }

    private fun getRegion(codeEntered: String) {
        viewModel.getRegion(codeEntered)
    }

    private fun showToast(toastTextResource: Int) {

        val toast = Toast.makeText(this, resources.getString(toastTextResource), Toast.LENGTH_LONG)
        with(toast) {
            this.setGravity(Gravity.CENTER, 0, 0)
            this.show()
        }
    }

    private fun hideRegionInfo() {
        textRegion?.let {
            it.text = ""
            it.visibility = View.GONE
        }
        textOtherCodeForRegion?.let{
            it.text = ""
            it.visibility = View.GONE
        }

        textCode?.visibility = View.GONE
    }
}