package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode.NONE
import com.squareup.picasso.Picasso
import io.realm.RealmList
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
        initFields()
        initListeners()
    }

    private fun initListeners() {
        iv_avatar.setOnClickListener { chooseAvatarDialog.show() }
        btn_add_spec.setOnClickListener {
            val addSpecializationDialog = AddSpecializationDialog.newInstance(null, -1)
            addSpecializationDialog.setTargetFragment(this, AddSpecializationDialog.REQUEST_CODE)
            addSpecializationDialog.show(baseActivity.supportFragmentManager, AddSpecializationDialog.SPECIALIZATION)
        }
    }

    private fun initFields() {
        val user = presenter.getUser()

        user?.name?.let { et_first_name.setText(it) }
        user?.lastname?.let { et_last_name.setText(it) }
        user?.username?.let { et_username.setText(it) }
        user?.email?.let { et_email.setText(it) }
        user?.phoneNumber?.let { et_phone.setText(it) }
        user?.profileImage?.let { setImageProfile(it) }
    }

    private fun openGallery() {
        ImagePicker.create(this)
                .returnMode(NONE)
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
        val items = presenter.getUser()?.getParsedSpecializations() ?: ArrayList()

        adapter = SpecializationsEditAdapter(items, { pos, sp -> onItemRemove(pos, sp) }
        ) { pos, sp -> onItemEdit(pos, sp) }
        rv_specializations_edit.adapter = adapter
        rv_specializations_edit.isNestedScrollingEnabled = false
        rv_specializations_edit.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun onItemRemove(position: Int, item: Specialization) {
        adapter?.removeItem(position)
    }

    private fun onItemEdit(position: Int, item: Specialization) {
        val addSpecializationDialog = AddSpecializationDialog.newInstance(item, position)
        addSpecializationDialog.setTargetFragment(this, AddSpecializationDialog.REQUEST_CODE)
        addSpecializationDialog.show(baseActivity.supportFragmentManager, AddSpecializationDialog.SPECIALIZATION);
    }

    private fun initDialogs() {
        chooseAvatarDialog = MaterialDialog.Builder(baseActivity)
                .customView(R.layout.dialog_choose_avatar, false)
                .build()

        chooseAvatarDialog.view.tv_dialog_open_gallery.setOnClickListener { openGallery() }
        chooseAvatarDialog.view.tv_dialog_remove_photo.setOnClickListener {
            presenter.deleteImage()
            chooseAvatarDialog.dismiss()
        }

        chooseAvatarDialog.view.tv_dialog_make_photo.setOnClickListener {
            ImagePicker.cameraOnly().start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getImages(data)[0]
            presenter.changeProfileImage(image)

            chooseAvatarDialog.dismiss()
        } else if (requestCode == AddSpecializationDialog.REQUEST_CODE) {
            val result = data?.getSerializableExtra(AddSpecializationDialog.RESULT_SPEC) as Specialization
            val position = data.getIntExtra(AddSpecializationDialog.RESULT_POS, -1)

            if (position == -1) adapter?.addItem(result) else adapter?.updateItem(position, result)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onEditSuccess() {
        Toast.makeText(baseActivity, "Профиль испешно изменен", Toast.LENGTH_SHORT).show()
        (activity as? FragmentHostActivity)?.onBackPressed()
    }

    override fun onImageDeleteSuccess() {
        Toast.makeText(baseActivity, "Фотография успешно удалена", Toast.LENGTH_SHORT).show()
        iv_avatar.setImageResource(R.drawable.ic_no_avatar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profile_edit_save -> {
                refreshErrors()
                val items: RealmList<Specialization> = RealmList()
                adapter?.getItems()?.let { items.addAll(it) }

                val updatedUser = User(
                        username = et_username.text?.trim().toString(),
                        email = et_email.text?.trim().toString(),
                        name = et_first_name.text?.trim().toString(),
                        lastname = et_last_name.text?.trim().toString(),
                        phoneNumber = et_phone.rawText.trim(), specializations = items)
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

    override fun onEmptySpecializations() {
        Toast.makeText(baseActivity, "Требуется добавить хотя бы одну специализацию", Toast.LENGTH_SHORT).show()
    }

    private fun refreshErrors() {
        ti_first_name.error = null
        ti_last_name.error = null
        ti_username.error = null
        ti_email.error = null
        ti_phone.error = null
    }

    private fun setImageProfile(url: String) {

        val vto = iv_avatar.viewTreeObserver
        vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                iv_avatar.viewTreeObserver.removeOnPreDrawListener(this)
                val height = iv_avatar.measuredHeight
                val width = iv_avatar.measuredWidth

                iv_avatar.visibility = View.VISIBLE

                Picasso
                        .with(context)
                        .load(url)
                        .resize(width, height)
                        .placeholder(R.drawable.progress_animation)
                        .centerCrop()
                        .into(iv_avatar)

                return true
            }
        })
    }

    override fun onImageSetSuccess(url: String) {
        setImageProfile(url)
        Toast.makeText(context, "Фотография успешно изменена.", Toast.LENGTH_SHORT).show()
    }
}
