package itis.ru.kpfu.join.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import kotlinx.android.synthetic.main.dialog_error.*

class ErrorDialog : DialogFragment() {

    companion object {
        private const val ERROR_MESSAGE = "error message"

        fun getInstance(errorMessage: String) = ErrorDialog().also {
            it.arguments = Bundle().apply {
                putString(ERROR_MESSAGE, errorMessage)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_error, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val errorMessage = arguments?.get(ERROR_MESSAGE) as String

        error_message.text = errorMessage
        btn_ok.setOnClickListener { dismiss() }
    }
}