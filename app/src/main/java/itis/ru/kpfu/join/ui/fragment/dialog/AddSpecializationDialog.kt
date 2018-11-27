package itis.ru.kpfu.join.ui.fragment.dialog

import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import com.jaredrummler.materialspinner.MaterialSpinner
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.utils.toPx
import kotlinx.android.synthetic.main.dialog_add_specialization.btn_add_spec
import kotlinx.android.synthetic.main.dialog_add_specialization.btn_cancel
import kotlinx.android.synthetic.main.dialog_add_specialization.spinner_spec
import kotlinx.android.synthetic.main.dialog_add_specialization.spinner_tech
import kotlinx.android.synthetic.main.dialog_add_specialization.spinners_container

class AddSpecializationDialog: DialogFragment() {

    companion object {
        fun newInstance(): AddSpecializationDialog {
            val args = Bundle()
            val fragment = AddSpecializationDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_add_specialization, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        val itemsSpec = mutableListOf("Ничего не выбрано","Android developer", "Ios developer", "Backend developer", "Database Developer")
        val itemsTech = mutableListOf("Ничего не выбрано","Android", "Kotlin", "Ios", "Ruby", "RX", "Java", "Python", "C#", "C++", "Oracle")

        spinner_spec.setItems(itemsSpec)
        spinner_spec.selectedIndex = 0

        spinner_tech.setItems(itemsTech)
        spinner_tech.selectedIndex = 0

        btn_add_spec.setOnClickListener {
            val spinner = MaterialSpinner(activity)

            val layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
           // layoutParams.setMargins(toPx(8, activity), 0, 0, toPx(8, activity))

            spinner.layoutParams = layoutParams
            spinner.setItems(itemsTech)
            spinner.selectedIndex = 0

            spinners_container.addView(spinner)

        }

        btn_cancel.setOnClickListener { dialog.dismiss() }
    }
}