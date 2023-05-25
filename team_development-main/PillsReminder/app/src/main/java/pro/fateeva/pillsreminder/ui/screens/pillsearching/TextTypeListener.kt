package pro.fateeva.pillsreminder.ui.screens.pillsearching

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.flow.MutableStateFlow

class TextTypeListener {
    fun onTypeTextListener(editText: EditText, flow: MutableStateFlow<String>) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                flow.value = s.toString()
            }
        })
    }
}