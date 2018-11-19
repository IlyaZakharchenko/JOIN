package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode.NONE
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.mvp.presenter.ProfilePresenter
import itis.ru.kpfu.join.mvp.view.ProfileView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.SpecializationsAdapter
import itis.ru.kpfu.join.utils.toPx
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_make_photo
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_open_gallery
import kotlinx.android.synthetic.main.dialog_choose_avatar.view.tv_dialog_remove_photo
import kotlinx.android.synthetic.main.fragment_profile.app_bar_profile
import kotlinx.android.synthetic.main.fragment_profile.btn_edit
import kotlinx.android.synthetic.main.fragment_profile.collapsing_toolbar
import kotlinx.android.synthetic.main.fragment_profile.iv_profile_avatar
import kotlinx.android.synthetic.main.fragment_profile.rv_specializations
import kotlinx.android.synthetic.main.fragment_profile.specializations_container
import kotlinx.android.synthetic.main.fragment_profile.toolbar_profile
import kotlinx.android.synthetic.main.fragment_profile.tv_email
import kotlinx.android.synthetic.main.fragment_profile.tv_phone
import kotlinx.android.synthetic.main.fragment_profile.tv_username
import kotlinx.android.synthetic.main.item_specialisation.view.item_grid
import kotlinx.android.synthetic.main.item_specialisation.view.tv_spec_name
import java.io.File
import java.util.Random

class ProfileFragment : BaseFragment(), ProfileView {

    companion object {
        fun newInstance(): ProfileFragment {
            val args = Bundle()
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
        get() = R.menu.menu_profile

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_profile

    @InjectPresenter
    lateinit var mPresenter: ProfilePresenter

    lateinit var chooseAvatarDialog: MaterialDialog

    private var adapter: SpecializationsAdapter? = null

    @ProvidePresenter
    fun providePresenter(): ProfilePresenter {
        return JoinApplication.appComponent.providePresenters().provideProfilePresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChooseAvatarDialog()
        initListeners()
        initSpecializations()
        initFields()
        initRecyclerView()
    }

    private fun initFields() {
        val user = userRepository.getUser()

        tv_username.text = user?.username
        tv_email.text = user?.email
        tv_phone.text = "89046666200"

        if (user?.name.isNullOrEmpty() || user?.lastname.isNullOrEmpty()) {
            user?.username?.let { (activity as? FragmentHostActivity)?.setToolbarTitle(it) }
        } else {
            (activity as? FragmentHostActivity)?.setToolbarTitle("${user?.lastname} ${user?.name}")
        }
    }

    private fun initRecyclerView() {
        adapter = SpecializationsAdapter(initSpecializations())
        rv_specializations.adapter = adapter
        rv_specializations.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initSpecializations(): List<Specialization> {
        return arrayListOf(
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
        ).shuffled()
    }

    private fun initListeners() {
        btn_edit.setOnClickListener {
            (activity as? FragmentHostActivity)?.setFragment(ProfileEditFragment.newInstance(), true)
        }
        collapsing_toolbar.setOnClickListener { chooseAvatarDialog.show() }
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
                    .resize(iv_profile_avatar.width, iv_profile_avatar.height)
                    .centerCrop()
                    .into(iv_profile_avatar)



            chooseAvatarDialog.dismiss()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}