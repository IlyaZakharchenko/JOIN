package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
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
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.mvp.presenter.ProfilePresenter
import itis.ru.kpfu.join.mvp.view.ProfileView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.SpecializationsAdapter
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_make_photo
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_open_gallery
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_remove_photo
import kotlinx.android.synthetic.main.fragment_profile.app_bar_profile
import kotlinx.android.synthetic.main.fragment_profile.btn_edit
import kotlinx.android.synthetic.main.fragment_profile.collapsing_toolbar
import kotlinx.android.synthetic.main.fragment_profile.iv_profile_avatar
import kotlinx.android.synthetic.main.fragment_profile.iv_profile_avatar_shadows
import kotlinx.android.synthetic.main.fragment_profile.rv_specializations
import kotlinx.android.synthetic.main.fragment_profile.toolbar_profile
import kotlinx.android.synthetic.main.fragment_profile.tv_email
import kotlinx.android.synthetic.main.fragment_profile.tv_phone
import kotlinx.android.synthetic.main.fragment_profile.tv_username
import java.io.File

class ProfileFragment : BaseFragment(), ProfileView {

    companion object {
        const val PROFILE_FRAGMENT = "PROFILE_FRAGMENT"

        fun newInstance(userId: Long = -1L): ProfileFragment {
            val args = Bundle()
            args.putLong(PROFILE_FRAGMENT, userId)

            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_profile

    override val toolbarTitle: Int?
        get() = R.string.toolbar_title_empty

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_profile

    @InjectPresenter
    lateinit var presenter: ProfilePresenter

    private lateinit var chooseAvatarDialog: MaterialDialog

    private lateinit var user: User

    private var adapter: SpecializationsAdapter? = null

    private var userId: Long? = null

    @ProvidePresenter
    fun providePresenter(): ProfilePresenter {
        return JoinApplication.appComponent.providePresenters().provideProfilePresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getLong(PROFILE_FRAGMENT)
        if(userId!= -1L) {
            (activity as? FragmentHostActivity)?.enableBackPressed(true)
        }

        user = userId?.let { presenter.getUser(it) } ?: User()

        initRecyclerView()
        initFields(user)
        initChooseAvatarDialog()
        initListeners()
    }

    override fun initFields(user: User) {
        user.email?.let { tv_email.text = it }
        user.username?.let { tv_username.text = it }
        tv_phone.text = if (user.phoneNumber.isNullOrEmpty()) "Не указан" else "8${user.phoneNumber}"

        user.profileImage?.let { setImageProfile(it) }

        if (user.name.isNullOrEmpty() || user.lastname.isNullOrEmpty()) {
            user.username?.let { (activity as? FragmentHostActivity)?.setToolbarTitle(it) }
        } else {
            (activity as? FragmentHostActivity)?.setToolbarTitle("${user.lastname} ${user.name}")
        }

        user.specializations?.let { adapter?.setItems(it as List<Specialization>) }
    }

    private fun initRecyclerView() {
        adapter = SpecializationsAdapter()
        rv_specializations.adapter = adapter
        rv_specializations.isNestedScrollingEnabled = false
        rv_specializations.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initListeners() {
        if (userId == -1L) {
            btn_edit.visibility = View.VISIBLE
            btn_edit.setOnClickListener {
                (activity as? FragmentHostActivity)?.setFragment(ProfileEditFragment.newInstance(), true)
            }
            collapsing_toolbar.setOnClickListener { chooseAvatarDialog.show() }
        }
        toolbar_profile.setOnClickListener { app_bar_profile.setExpanded(true) }
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
            iv_profile_avatar_shadows.visibility = View.GONE
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
            presenter.changeProfileImage(image)

            chooseAvatarDialog.dismiss()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onImageSetSuccess(url: String) {
        setImageProfile(url)
    }

    override fun onConnectionError() {
        Toast.makeText(baseActivity, "Internet connection error", Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    private fun setImageProfile(url: String) {

        val vto = iv_profile_avatar.viewTreeObserver
        vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                iv_profile_avatar.viewTreeObserver.removeOnPreDrawListener(this)
                val height = iv_profile_avatar.measuredHeight
                val width = iv_profile_avatar.measuredWidth

                iv_profile_avatar_shadows.visibility = View.VISIBLE

                Picasso
                        .with(context)
                        .load(url)
                        .resize(width, height)
                        .placeholder(R.drawable.progress_animation)
                        .centerCrop()
                        .into(iv_profile_avatar)

                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if(userId == -1L) {
            inflater?.inflate(R.menu.menu_profile, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_exit -> {
                presenter.exit()
                (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}