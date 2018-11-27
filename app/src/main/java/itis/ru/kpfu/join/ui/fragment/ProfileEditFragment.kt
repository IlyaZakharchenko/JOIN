package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode.NONE
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.R.string
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.mvp.presenter.ProfileEditPresenter
import itis.ru.kpfu.join.mvp.view.ProfileEditView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.fragment.dialog.AddSpecializationDialog
import itis.ru.kpfu.join.ui.recyclerView.adapter.SpecializationsEditAdapter
import itis.ru.kpfu.join.utils.Constants
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_make_photo
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_open_gallery
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_remove_photo
import kotlinx.android.synthetic.main.fragment_profile_edit.btn_add_spec
import kotlinx.android.synthetic.main.fragment_profile_edit.et_email
import kotlinx.android.synthetic.main.fragment_profile_edit.et_first_name
import kotlinx.android.synthetic.main.fragment_profile_edit.et_last_name
import kotlinx.android.synthetic.main.fragment_profile_edit.et_phone
import kotlinx.android.synthetic.main.fragment_profile_edit.et_username
import kotlinx.android.synthetic.main.fragment_profile_edit.iv_avatar
import kotlinx.android.synthetic.main.fragment_profile_edit.rv_specializations_edit
import kotlinx.android.synthetic.main.fragment_profile_edit.ti_email
import kotlinx.android.synthetic.main.fragment_profile_edit.ti_first_name
import kotlinx.android.synthetic.main.fragment_profile_edit.ti_last_name
import kotlinx.android.synthetic.main.fragment_profile_edit.ti_phone
import kotlinx.android.synthetic.main.fragment_profile_edit.ti_username
import kotlinx.android.synthetic.main.fragment_profile_edit.toolbar_profile_edit
import java.io.File

class ProfileEditFragment : BaseFragment(), ProfileEditView {

    companion object {

        fun newInstance(): ProfileEditFragment {
            val args = Bundle()
            val fragment = ProfileEditFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_profile_edit

    override val toolbarTitle: Int?
        get() = R.string.toolbar_profile_edit

    override val menu: Int?
        get() = R.menu.menu_edit_profile

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = toolbar_profile_edit

    @InjectPresenter
    lateinit var presenter: ProfileEditPresenter

    lateinit var chooseAvatarDialog: MaterialDialog

    private var adapter: SpecializationsEditAdapter? = null

    @ProvidePresenter
    fun providePresenter(): ProfileEditPresenter {
        return JoinApplication.appComponent.providePresenters().provideProfileEditPresenter()
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onConnectionError() {
        Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialogs()
        initRecyclerView()
        initFields(presenter.getUser())
        initListeners()
    }

    private fun initListeners() {
        iv_avatar.setOnClickListener { chooseAvatarDialog.show() }
        btn_add_spec.setOnClickListener {
            val addSpecializationDialog = AddSpecializationDialog.newInstance()
            addSpecializationDialog.show(baseActivity.fragmentManager, Constants.DIALOG_ADD_SPECIALIZATION)
        }
    }

    private fun initFields(user: User?) {
        et_first_name.setText(user?.name)
        et_last_name.setText(user?.lastname)
        et_username.setText(user?.username)
        et_email.setText(user?.email)
        et_phone.setText(user?.phoneNumber)
        user?.imagePath?.let {
            Picasso
                    .with(context)
                    .load(File(it))
                    .resize(iv_avatar.width,
                            iv_avatar.height)
                    .into(iv_avatar)
        }
    }

    private fun openGallery() {
        ImagePicker.create(this)
                .returnMode(NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true)
                .toolbarFolderTitle("Выберите папку")
                .toolbarImageTitle("Выберите фотографию")
                .toolbarArrowColor(Color.WHITE)
                .showCamera(false)
                .multi()
                .limit(1)
                .theme(R.style.AppTheme)
                .enableLog(false)
                .start()
    }

    private fun initRecyclerView() {
        adapter = SpecializationsEditAdapter(initSpecializations(), { pos, sp -> onItemRemove(pos, sp) }
        ) { pos, sp -> onItemEdit(pos, sp) }
        rv_specializations_edit.adapter = adapter
        rv_specializations_edit.isNestedScrollingEnabled = false
        rv_specializations_edit.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun onItemRemove(position: Int, item: Specialization) {
        adapter?.removeItem(position);
    }

    private fun onItemEdit(position: Int, item: Specialization) {
        val addSpecializationDialog = AddSpecializationDialog.newInstance()
        addSpecializationDialog.show(baseActivity.fragmentManager, Constants.DIALOG_ADD_SPECIALIZATION);
    }

    private fun initSpecializations(): MutableList<Specialization> {
        return mutableListOf(
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior"),
                Specialization(specializationName = "Android developer", experience = (Math.random() * 100).toInt(),
                        knowledgeLevel = "Junior")
        )
    }

    private fun initDialogs() {
        chooseAvatarDialog = MaterialDialog.Builder(baseActivity)
                .customView(R.layout.dialog_choose_avatar, false)
                .build()

        chooseAvatarDialog.view.tv_dialog_open_gallery.setOnClickListener { openGallery() }
        chooseAvatarDialog.view.tv_dialog_remove_photo.setOnClickListener {
            iv_avatar.setImageResource(0)
            chooseAvatarDialog.dismiss()
        }

        chooseAvatarDialog.view.tv_dialog_make_photo.setOnClickListener {
            ImagePicker.cameraOnly().start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getImages(data)[0]
            Picasso
                    .with(context)
                    .load(File(image.path))
                    .resize(iv_avatar.width,
                            iv_avatar.height)
                    .into(iv_avatar)
            presenter.changeAvatar(image.path)
            chooseAvatarDialog.dismiss()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onEditSuccess() {
        (activity as? FragmentHostActivity)?.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profile_edit_save -> {
                val updatedUser = User(username = et_username.text.toString(), email = et_email.text.toString(),
                        name =
                        et_first_name.text.toString(), lastname = et_last_name.text.toString(),
                        phoneNumber = et_phone.rawText.toString
                        ())
                refreshErrors()
                presenter.updateUser(updatedUser)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onEmptyName() {
        ti_first_name.error = getString(string.fill_field)
    }

    override fun onEmptySurname() {
        ti_last_name.error = getString(string.fill_field)
    }

    override fun onEmptyEmail() {
        ti_email.error = getString(string.fill_field)
    }

    override fun onEmptyUsername() {
        ti_phone.error = getString(string.fill_field)
    }

    override fun onEmptyPhoneNumber() {
        ti_phone.error = getString(string.fill_field)
    }

    override fun onInvalidUsername() {
        ti_username.error = getString(string.error_username)
    }

    override fun onInvalidEmail() {
        ti_email.error = getString(string.error_email)
    }

    override fun onInvalidPhoneNumber() {
        ti_phone.error = getString(string.error_phone_number)
    }

    fun refreshErrors() {
        ti_first_name.error = null
        ti_last_name.error = null
        ti_username.error = null
        ti_email.error = null
        ti_phone.error = null
    }
}
