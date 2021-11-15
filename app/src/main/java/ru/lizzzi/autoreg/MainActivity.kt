package ru.lizzzi.autoreg

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

class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModelMain by viewModels()

    private var editText: EditText? = null
    private var textThis: TextView? = null
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

        initButtonSowRegion()
        textThis = findViewById(R.id.textThis)
        textThis?.visibility = View.GONE

        textRegion = findViewById(R.id.textRegion)
        textRegion?.visibility = View.GONE

        textOtherCodeForRegion = findViewById(R.id.textOtherCodeForRegion)
        textOtherCodeForRegion?.visibility = View.GONE

        textCode = findViewById(R.id.textCode)
        textCode?.visibility = View.GONE
    }

    private fun initButtonSowRegion() {
        val buttonShowRegion = findViewById<Button>(R.id.buttonShowRegion)
        buttonShowRegion.setOnClickListener(View.OnClickListener {
            editText?.let {
                val enteredCode = it.text.toString()
                if (enteredCode.isEmpty()) {
                    showToast(resources.getString(R.string.toastEnterCodeOfRegion))
                    return@OnClickListener
                }
                if (enteredCode.toInt() > 0) {
                    getRegion(enteredCode)
                } else {
                    showToast(resources.getString(R.string.toastErrorCode))
                }
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        viewModel.checkStorage()
        if (viewModel.defaultCodeOfRegion.isNotEmpty()) {
            textThis?.visibility = View.VISIBLE
            textRegion?.visibility = View.VISIBLE
            if (textOtherCodeForRegion?.length()!! > 1) {
                textOtherCodeForRegion?.visibility = View.VISIBLE
                textCode?.visibility = View.VISIBLE
            }
        } else {
            editText?.requestFocus()
            inputMethodManager.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    private fun getRegion(codeEntered: String) {
        val region = viewModel.getRegion(codeEntered)
        if (region.isNotEmpty()) {
            textThis?.visibility = View.VISIBLE
            textRegion?.visibility = View.VISIBLE
            textRegion?.text = region
            getOtherCodeOfRegion(region)
            inputMethodManager.hideSoftInputFromWindow(editText?.windowToken, 0)
        } else {
            showToast(resources.getString(R.string.toastNoSuchRegion))
        }
    }

    private fun getOtherCodeOfRegion(region: String) {
        val otherCodeOfRegion = viewModel.getOtherCodeOfRegion(region)
        if (otherCodeOfRegion.length > 1) {
            textCode?.visibility = View.VISIBLE
            textOtherCodeForRegion?.visibility = View.VISIBLE
            textOtherCodeForRegion?.text = otherCodeOfRegion
        }
    }

    private fun showToast(toastText: String) {
        textRegion?.text = ""
        textOtherCodeForRegion?.text = ""
        textThis?.visibility = View.GONE
        textRegion?.visibility = View.GONE
        textOtherCodeForRegion?.visibility = View.GONE
        textCode?.visibility = View.GONE
        val toast = Toast.makeText(this, toastText, Toast.LENGTH_LONG)
        with(toast) {
            this.setGravity(Gravity.CENTER, 0, 0)
            this.show()
        }
    }
}