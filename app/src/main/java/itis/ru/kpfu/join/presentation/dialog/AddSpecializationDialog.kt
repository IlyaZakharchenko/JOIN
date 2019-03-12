package itis.ru.kpfu.join.presentation.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import com.jaredrummler.materialspinner.MaterialSpinner
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.presentation.util.divideString
import itis.ru.kpfu.join.presentation.util.parseLevelFromInt
import itis.ru.kpfu.join.presentation.util.parseLevelFromString
import itis.ru.kpfu.join.presentation.util.toPx
import itis.ru.kpfu.join.presentation.util.transformToString
import kotlinx.android.synthetic.main.dialog_add_specialization.btn_add_spec
import kotlinx.android.synthetic.main.dialog_add_specialization.btn_cancel
import kotlinx.android.synthetic.main.dialog_add_specialization.btn_save
import kotlinx.android.synthetic.main.dialog_add_specialization.spinner_exp
import kotlinx.android.synthetic.main.dialog_add_specialization.spinner_lvl
import kotlinx.android.synthetic.main.dialog_add_specialization.spinner_spec
import kotlinx.android.synthetic.main.dialog_add_specialization.spinners_container

class AddSpecializationDialog : DialogFragment() {

    companion object {
        private const val REQUEST_CODE = "request code"
        private const val SPECIALIZATION = "specialization"
        private const val POSITION = "position"

        fun getInstance(requestCode: Int, position: Int, spec: Specialization?) = AddSpecializationDialog().also {
            it.arguments = Bundle().apply {
                putInt(REQUEST_CODE, requestCode)
                putInt(POSITION, position)
                putSerializable(SPECIALIZATION, spec)
            }
        }
    }

    var onSave: ((Specialization, Int, Int) -> Unit)? = null

    private lateinit var itemsSpec: MutableList<String>
    private lateinit var itemsTech: MutableList<String>
    private lateinit var itemsLvl: MutableList<String>
    private lateinit var itemsExp: MutableList<String>

    private var position: Int? = null
    private var requestCode: Int? = null

    private var spec: Specialization? = null

    private var technologies = ArrayList<MaterialSpinner>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_specialization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spec = arguments?.getSerializable(SPECIALIZATION) as Specialization?
        position = arguments?.getInt(POSITION)
        requestCode = arguments?.getInt(REQUEST_CODE)

        initItems()
        initSpinners(spec)
        initListeners()
    }

    private fun initItems() {
        itemsSpec = mutableListOf("Ничего не выбрано", "iOS Developer", "Android Developer", "Java Developer",
                "Designer", "ProjectModel Manager", "C# Developer", "RoR Developer", "Python Developer",
                "Frontend Developer", "Backend Developer", "SMM Manager", "System Administrator")
        itemsTech = mutableListOf("Ничего не выбрано", "Swift", "Objective C", "Java", "Kotlin", "Sketch", "Spring",
                "Python", "C#", "PostgreSQL", "SQL", "Zoom", "Pivotal tracker")
        itemsLvl = mutableListOf("Ничего не выбрано", "Junior", "Middle", "Senior")
        itemsExp = mutableListOf("Ничего не выбрано")

        for (i in 0..50) {
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
            spinner_spec.selectedIndex = if (itemsSpec.indexOf(item.name) > -1) itemsSpec.indexOf(item.name) else 0
            spinner_lvl.selectedIndex = if (itemsLvl.indexOf(
                            parseLevelFromInt(item.knowledgeLevel)) > -1) itemsLvl.indexOf(parseLevelFromInt(
                    item.knowledgeLevel)) else 0
            spinner_exp.selectedIndex = if (itemsExp.indexOf(item.experience.toString()) > -1) itemsExp.indexOf(
                    item.experience.toString()) else 0

            val techList = item.technologies?.let { divideString(it) }

            techList?.forEach { str ->
                if (itemsTech.indexOf(str) == -1) itemsTech.add(str)
                createSpinner(itemsTech.indexOf(str))
            }
        } else {
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
        context?.let { toPx(300, it) }?.let { spinner.setDropdownHeight(it) }

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
            if (techs.size == 0) {
                Toast.makeText(context, "Выберите хотя бы одну технологию", Toast.LENGTH_SHORT).show()
                return
            }

            val spec = Specialization(itemsSpec[spinner_spec.selectedIndex],
                    parseLevelFromString(itemsLvl[spinner_lvl.selectedIndex]),
                    itemsExp[spinner_exp.selectedIndex].toInt(), transformToString(techs))


            onSave?.invoke(spec, position ?: -1, requestCode ?: -1)
            dialog.dismiss()
        }
    }
}