package itis.ru.kpfu.join.presentation.ui.main.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.presentation.adapter.SpecializationsAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.dialog.ChooseImageDialog
import itis.ru.kpfu.join.presentation.model.CreatedDialogModel
import itis.ru.kpfu.join.presentation.ui.main.profile.edit.ProfileEditFragment
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import itis.ru.kpfu.join.presentation.ui.main.dialogs.selected.ChatFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.layout_progress_error.*
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class ProfileFragment : BaseFragment(), ProfileView {

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val TAG_CHOOSE_IMAGE_DIALOG = "TAG_CHOOSE_IMAGE_DIALOG"

        fun newInstance(userId: Long): ProfileFragment {
            val args = Bundle()
            args.putLong(KEY_USER_ID, userId)

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
        get() = true

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_profile

    @InjectPresenter
    lateinit var presenter: ProfilePresenter

    @Inject
    lateinit var presenterProvider: Provider<ProfilePresenter>

    private var adapter: SpecializationsAdapter? = null

    private var logoutItem: MenuItem? = null
    private var settingsItem: MenuItem? = null

    fun getUserId(): Long = arguments?.getLong(KEY_USER_ID)
            ?: throw IllegalArgumentException("User ID is null")

    @ProvidePresenter
    fun providePresenter(): ProfilePresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_profile.setOnClickListener { app_bar_profile.setExpanded(true) }
        initRecyclerView()
    }

    override fun setUser(user: User, isOwner: Boolean) {
        if (isOwner) {
            logoutItem?.isVisible = true
            settingsItem?.isVisible = true

            btn_edit.apply {
                visibility = View.VISIBLE
                text = "Редактировать"
                setOnClickListener { presenter.onEditProfile() }
            }

            collapsing_toolbar.setOnClickListener { presenter.onChoosePhoto() }
        } else {
            btn_edit.apply {
                visibility = View.VISIBLE
                text = "Написать сообщение"
                setOnClickListener { presenter.onOpenChat() }
            }
        }

        tv_email.text = user.email.orEmpty()
        tv_username.text = user.username.orEmpty()
        tv_phone.text = user.phoneNumber.orEmpty()

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

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        if (childFragment is ChooseImageDialog) {
            childFragment.photoListener = { imagePaths, requestCode ->
                presenter.onChoosePhotoResult(imagePaths[0], requestCode)
            }
        }
    }

    override fun setChangedPhotoProfile(url: String) {
        setImageProfile(url)
        Toast.makeText(context, "Фотография успешно изменена.", Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showRetry(errorText: String) {
        retry.visibility = View.VISIBLE
        retry_title.text = errorText
        btn_retry.setOnClickListener { presenter.onRetry() }
    }

    override fun hideRetry() {
        retry.visibility = View.GONE
    }

    override fun hideCollapsingToolbar() {
        iv_profile_avatar.visibility = View.GONE
    }

    override fun showCollapsingToolbar() {
        iv_profile_avatar.visibility = View.VISIBLE
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
        inflater?.inflate(R.menu.menu_profile, menu)

        logoutItem = menu?.findItem(R.id.menu_logout)
        settingsItem = menu?.findItem(R.id.menu_settings)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_logout -> {
                presenter.onLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showChooseImageDialog(requestCode: Int, limit: Int) {
        ChooseImageDialog.getInstance(requestCode, limit).show(childFragmentManager, TAG_CHOOSE_IMAGE_DIALOG)
    }

    override fun setBackArrowEnabled(enabled: Boolean) {
        (activity as? FragmentHostActivity)?.enableBackPressed(enabled)
    }

    override fun setSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false)
    }

    override fun setChangeProfileFragment() {
        (activity as? FragmentHostActivity)?.setFragment(ProfileEditFragment.newInstance(), true)
    }

    override fun setDialogFragment(dialog: CreatedDialogModel) {
        (activity as? FragmentHostActivity)?.setFragment(
                ChatFragment.newInstance(dialog),
                true
        )
    }
}