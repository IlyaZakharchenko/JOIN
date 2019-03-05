package itis.ru.kpfu.join.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import kotlinx.android.synthetic.main.dialog_confirmation.*

class ConfirmationDialog: DialogFragment() {

    companion object {
        private const val REQUEST_CODE = "request code"

        fun getInstance(requestCode: Int) = ConfirmationDialog().also {
            it.arguments = Bundle().apply {
                putInt(REQUEST_CODE, requestCode)
            }
        }
    }

    private var requestCode: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_confirmation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requestCode = arguments?.getInt(REQUEST_CODE)

        ok.setOnClickListener { activity?.finish() }
        cancel.setOnClickListener { dismiss() }
    }
}