package itis.ru.kpfu.join.ui.fragment

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.kpfu.join.R

class ProgressDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): ProgressDialogFragment {
            val args = Bundle()
            val fragment = ProgressDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_progress, container, false)
    }
}