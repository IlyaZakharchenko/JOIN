package itis.ru.kpfu.join.presentation.dialog

import android.app.usage.UsageEvents.Event.NONE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import itis.ru.kpfu.join.R
import kotlinx.android.synthetic.main.dialog_choose_image.*

class ChooseImageDialog : DialogFragment() {

    companion object {
        private const val REQUEST_CODE = "REQUEST_CODE"
        private const val IMAGES_LIMIT = "IMAGES_LIMIT"

        fun getInstance(requestCode: Int, imagesLimit: Int) = ChooseImageDialog().also {
            it.arguments = Bundle().apply {
                putInt(REQUEST_CODE, requestCode)
                putInt(IMAGES_LIMIT, imagesLimit)
            }
        }
    }

    var photoListener: ((List<String>, Int) -> Unit)? = null

    private var imagePicker: ImagePicker? = null

    private var requestCode: Int? = null
    private var imagesLimit: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_choose_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requestCode = arguments?.getInt(REQUEST_CODE)
        imagesLimit = arguments?.getInt(IMAGES_LIMIT)

        initImagePicker()
        initClickListeners()
    }

    private fun initImagePicker() {
        imagePicker = imagesLimit?.let {
            ImagePicker.create(this)
                    .returnMode(ReturnMode.NONE)
                    .folderMode(true)
                    .toolbarFolderTitle("Выберите папку")
                    .toolbarImageTitle("Выберите фотографию")
                    .toolbarArrowColor(Color.WHITE)
                    .showCamera(false)
                    .multi()
                    .limit(it)
                    .theme(R.style.AppTheme)
                    .enableLog(false)
        }
    }

    private fun initClickListeners() {
        tv_make_photo.setOnClickListener {
            ImagePicker.cameraOnly().start(this)
        }
        tv_open_gallery.setOnClickListener {
            imagePicker?.start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            this.requestCode?.let { photoListener?.invoke(ImagePicker.getImages(data).map(Image::getPath), it) }
        }
        dismiss()
    }
}