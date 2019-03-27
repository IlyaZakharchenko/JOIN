package itis.ru.kpfu.join.presentation.ui.main.profile.edit

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.squareup.picasso.Picasso
import io.realm.RealmList
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.R.string
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.presentation.adapter.SpecializationsEditAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.dialog.AddSpecializationDialog
import itis.ru.kpfu.join.presentation.dialog.ChooseImageDialog
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
import javax.inject.Inject
import javax.inject.Provider

class ProfileEditFragment : BaseFragment(), ProfileEditView {

    companion object {
        private const val ADD_SPEC_DIALOG_TAG = "add spec dialog tag"
        private const val CHOOSE_IMAGE_DIALOG_TAG = "choose image dialog tag"

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
    @Inject
    lateinit var presenterProvider: Provider<ProfileEditPresenter>
    @Inject
    lateinit var adapter: SpecializationsEditAdapter

    @ProvidePresenter
    fun providePresenter(): ProfileEditPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initListeners()
    }

    private fun initListeners() {
        iv_avatar.setOnClickListener { presenter.onChoosePhoto() }
        btn_add_spec.setOnClickListener { presenter.onAddSpec() }
    }

    override fun initFields(user: User?) {
        user?.name?.let { et_first_name.setText(it) }
        user?.lastname?.let { et_last_name.setText(it) }
        user?.username?.let { et_username.setText(it) }
        user?.email?.let { et_email.setText(it) }
        user?.phoneNumber?.let { et_phone.setText(it) }
        user?.profileImage?.let { setImageProfile(it) }
        user?.getParsedSpecializations()?.let { adapter.items = it }
    }

    private fun initRecyclerView() {
        adapter.onItemEdit = { position, spec -> presenter.onEditSpec(position, spec) }
        adapter.onItemRemove = { position -> presenter.onRemoveSpec(position) }

        rv_specializations_edit.adapter = adapter
        rv_specializations_edit.isNestedScrollingEnabled = false
        rv_specializations_edit.layoutManager = LinearLayoutManager(baseActivity)
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        if (childFragment is AddSpecializationDialog) {
            childFragment.onSave = { spec, position, requestCode ->
                presenter.onAddSpecResult(spec, position, requestCode)
            }
        } else if (childFragment is ChooseImageDialog) {
            childFragment.photoListener = { imagePaths, requestCode ->
                presenter.onChoosePhotoResult(imagePaths[0], requestCode) }
        }
    }

    override fun onError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
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
                adapter.items.let { items.addAll(it) }
                adapter.items = items

                val updatedUser = User(
                        username = et_username.text?.trim().toString(),
                        email = et_email.text?.trim().toString(),
                        name = et_first_name.text?.trim().toString(),
                        lastname = et_last_name.text?.trim().toString(),
                        phoneNumber = et_phone.rawText.trim(), specializations = items)
                presenter.onUpdateUser(updatedUser)
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

    override fun onError() {
        Toast.makeText(baseActivity, "Неопознанная ошибка", Toast.LENGTH_SHORT).show()
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

    override fun updateSpec(position: Int, spec: Specialization) {
        adapter.updateItem(position, spec)
    }

    override fun addSpec(spec: Specialization) {
        adapter.addItem(spec)
    }

    override fun removeSpec(position: Int) {
        adapter.removeItem(position)
    }

    override fun showAddSpecDialog(requestCode: Int, position: Int, spec: Specialization?) {
        AddSpecializationDialog
                .getInstance(requestCode, position, spec)
                .show(childFragmentManager, ADD_SPEC_DIALOG_TAG)
    }

    override fun showChooseImageDialog(requestCode: Int, imagesLimit: Int) {
        ChooseImageDialog
                .getInstance(requestCode, imagesLimit)
                .show(childFragmentManager, CHOOSE_IMAGE_DIALOG_TAG)
    }

}
