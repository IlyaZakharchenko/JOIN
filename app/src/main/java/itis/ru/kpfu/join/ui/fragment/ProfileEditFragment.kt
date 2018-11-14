package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.Toolbar
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
import itis.ru.kpfu.join.mvp.presenter.ProfileEditPresenter
import itis.ru.kpfu.join.mvp.view.ProfileEditView
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_make_photo
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_open_gallery
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_remove_photo
import kotlinx.android.synthetic.main.fragment_profile_edit.btn_save
import kotlinx.android.synthetic.main.fragment_profile_edit.et_email
import kotlinx.android.synthetic.main.fragment_profile_edit.et_first_name
import kotlinx.android.synthetic.main.fragment_profile_edit.et_last_name
import kotlinx.android.synthetic.main.fragment_profile_edit.et_phone
import kotlinx.android.synthetic.main.fragment_profile_edit.et_username
import kotlinx.android.synthetic.main.fragment_profile_edit.iv_avatar
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
        get() = null
    override val enableBackPressed: Boolean
        get() = false
    override val enableBottomNavBar: Boolean
        get() = true
    override val toolbar: Toolbar?
        get() = toolbar_profile_edit

    @InjectPresenter
    lateinit var presenter: ProfileEditPresenter

    lateinit var chooseAvatarDialog: MaterialDialog

    @ProvidePresenter
    fun providePresenter(): ProfileEditPresenter {
        return JoinApplication.appComponent.provideProfileEditPresenter()
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

        initChooseAvatarDialog()
        iv_avatar.setOnClickListener { chooseAvatarDialog.show() }

        //TODO profile info test
        val user = presenter.getUser()
        et_first_name.setText(user?.firstName)
        et_last_name.setText(user?.lastName)
        et_username.setText(user?.userName)
        et_email.setText(user?.email)
        et_phone.setText(user?.phone)
        /*Picasso
                .with(context)
                .load(File(user?.imagePath))
                .resize(iv_avatar.width,
                        iv_avatar.height)
                .into(iv_avatar)*/

        btn_save.setOnClickListener {
            user?.userName = et_username.text.toString()
            user?.email = et_email.text.toString()
            user?.phone = et_phone.text.toString()
            user?.firstName = et_first_name.text.toString()
            user?.lastName = et_last_name.text.toString()
            presenter.updateUser(user)
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

    private fun initChooseAvatarDialog() {
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
}
