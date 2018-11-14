package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode.NONE
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.MenuPresenter
import itis.ru.kpfu.join.mvp.view.MenuView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_make_photo
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_open_gallery
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_remove_photo
import kotlinx.android.synthetic.main.fragment_menu.app_bar_profile
import kotlinx.android.synthetic.main.fragment_menu.btn_edit
import kotlinx.android.synthetic.main.fragment_menu.collapsing_toolbar
import kotlinx.android.synthetic.main.fragment_menu.iv_profile_avatar
import kotlinx.android.synthetic.main.fragment_menu.toolbar_profile
import kotlinx.android.synthetic.main.fragment_menu.tv_email
import kotlinx.android.synthetic.main.fragment_menu.tv_phone
import kotlinx.android.synthetic.main.fragment_menu.tv_username
import java.io.File

class MenuFragment : BaseFragment(), MenuView {

    companion object {
        fun newInstance(): MenuFragment {
            val args = Bundle()
            val fragment = MenuFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_menu

    override val toolbarTitle: Int?
        get() = R.string.profile

    override val menu: Int?
        get() = R.menu.menu_profile

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_profile

    @InjectPresenter
    lateinit var presenter: MenuPresenter

    lateinit var chooseAvatarDialog: MaterialDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChooseAvatarDialog()

        toolbar_profile.setOnClickListener {
            app_bar_profile.setExpanded(true)
        }
        // iv_profile_avatar.setOnClickListener { chooseAvatarDialog.show() }
        collapsing_toolbar.setOnClickListener { chooseAvatarDialog.show() }

        //TODO profile info test
        presenter.createUser()
        val user = presenter.getUser()
        tv_username.text = user?.userName
        tv_email.text = user?.email
        tv_phone.text = user?.phone
        btn_edit.setOnClickListener {
            (activity as? FragmentHostActivity)?.setFragment(ProfileEditFragment.newInstance(), false)
        }
    }

    @ProvidePresenter
    fun providePresenter(): MenuPresenter {
        return JoinApplication.appComponent.provideMenuPresenter()
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
            iv_profile_avatar.setImageResource(0)
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
                    .resize(iv_profile_avatar.width,
                            iv_profile_avatar.height)
                    .into(iv_profile_avatar)
            presenter.changeAvatar(image.path)

            chooseAvatarDialog.dismiss()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}