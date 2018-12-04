package itis.ru.kpfu.join.ui.fragment.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable.ConstantState
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.jakewharton.rxbinding2.view.selected
import com.jaredrummler.materialspinner.MaterialSpinner
import io.realm.RealmList
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.utils.Constants
import itis.ru.kpfu.join.utils.divideString
import itis.ru.kpfu.join.utils.toPx
import itis.ru.kpfu.join.utils.transformToString
import kotlinx.android.synthetic.main.dialog_add_specialization.btn_add_spec
import kotlinx.android.synthetic.main.dialog_add_specialization.btn_cancel
import kotlinx.android.synthetic.main.dialog_add_specialization.btn_save
import kotlinx.android.synthetic.main.dialog_add_specialization.spinner_exp
import kotlinx.android.synthetic.main.dialog_add_specialization.spinner_lvl
import kotlinx.android.synthetic.main.dialog_add_specialization.spinner_spec
import kotlinx.android.synthetic.main.dialog_add_specialization.spinners_container

class AddSpecializationDialog : DialogFragment() {

    companion object {
        const val SPECIALIZATION = "dialog_add_specialization"
        const val REQUEST_CODE = 1123
        const val RESULT_SPEC = "result"
        const val RESULT_POS = "position"

        fun newInstance(item: Specialization?, position: Int): AddSpecializationDialog {
            val args = Bundle()
            args.putSerializable(RESULT_SPEC, item)
            args.putInt(RESULT_POS, position)

            val fragment = AddSpecializationDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var itemsSpec: MutableList<String>
    private lateinit var itemsTech: MutableList<String>
    private lateinit var itemsLvl: MutableList<String>
    private lateinit var itemsExp: MutableList<String>

    private var addNew: Boolean = false
    private var position: Int? = null
    private var technologies = ArrayList<MaterialSpinner>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_specialization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getSerializable(RESULT_SPEC) as Specialization?
        position = arguments?.getInt(RESULT_POS)

        initItems()
        initSpinners(item)
        initListeners()
    }

    private fun initItems() {
        itemsSpec = mutableListOf("Ничего не выбрано", "Android developer", "Ios developer", "Backend developer",
                "Database Developer", "Smth", "DB master")
        itemsTech = mutableListOf("Ничего не выбрано", "Android", "Kotlin", "Ios", "Ruby", "Rx", "Java", "Python",
                "C#", "C++", "Oracle", "C", "Postgress", "MySQL", "MVP", "MVVM", "Dependency Injection", "API",
                "Technology1", "Technology2", "Technology3", "Technology4")
        itemsLvl = mutableListOf("Ничего не выбрано", "Junior", "Middle", "Senior")
        itemsExp = mutableListOf("Ничего не выбрано")

        for (i in 0..100) {
            itemsExp.add("$i")
        }
    }

    private fun initSpinners(item: Specialization?) {
        spinner_spec.setItems(itemsSpec)
        spinner_spec.selectedIndex = 0

        spinner_lvl.setItems(itemsLvl)
        spinner_lvl.selectedIndex = 0

        spinner_exp.setItems(itemsExp)
        spinner_exp.selectedIndex = 0

        if (item != null) {
            spinner_spec.selectedIndex = itemsSpec.indexOf(item.name)
            spinner_lvl.selectedIndex = itemsLvl.indexOf(item.knowledgeLevel)
            spinner_exp.selectedIndex = itemsExp.indexOf(item.experience.toString())

            val techList = item.technologies?.let { divideString(it) }

            techList?.forEach { str ->
                createSpinner(itemsTech.indexOf(str))
            }
        } else {
            addNew = true
            createSpinner(0)
        }
    }

    private fun initListeners() {
        btn_add_spec.setOnClickListener { createSpinner(0) }
        btn_cancel.setOnClickListener { dialog.dismiss() }
        btn_save.setOnClickListener { sendResult() }
    }

    private fun createSpinner(selectedIndex: Int) {
        val spinner = MaterialSpinner(activity)

        val layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        spinner.layoutParams = layoutParams
        spinner.setItems(itemsTech)
        spinner.selectedIndex = selectedIndex

        technologies.add(spinner)
        spinners_container.addView(spinner)
    }

    private fun sendResult() {
        if (spinner_spec.selectedIndex == 0) {
            Toast.makeText(context, "Выберите название специализации", Toast.LENGTH_SHORT).show()
        } else if (spinner_exp.selectedIndex == 0) {
            Toast.makeText(context, "Выберите опыт", Toast.LENGTH_SHORT).show()
        } else if (spinner_lvl.selectedIndex == 0) {
            Toast.makeText(context, "Выберите уровень", Toast.LENGTH_SHORT).show()
        } else {
            val techs = HashSet<String>()
            technologies.forEach { spinner ->
                if (spinner.selectedIndex != 0) {
                    techs.add(itemsTech[spinner.selectedIndex])
                }
            }
            if(techs.size == 0 ){
                Toast.makeText(context, "Выберите хотя бы одну технологию", Toast.LENGTH_SHORT).show()
                return
            }

            val spec = Specialization(itemsSpec[spinner_spec.selectedIndex], itemsLvl[spinner_lvl.selectedIndex],
                    itemsExp[spinner_exp.selectedIndex].toInt(), transformToString(techs))

            val intent = Intent()
            intent.putExtra(RESULT_SPEC, spec)
            intent.putExtra(RESULT_POS, position)

            targetFragment?.onActivityResult(targetRequestCode, REQUEST_CODE,
                    intent)
            dialog.dismiss()
        }
    }
}