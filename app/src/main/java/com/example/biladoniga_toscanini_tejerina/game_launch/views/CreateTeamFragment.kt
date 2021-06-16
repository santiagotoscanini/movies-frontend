package com.example.biladoniga_toscanini_tejerina.game_launch.views

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.data.Response
import com.data.models.Team
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.commons.*
import com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel.LaunchViewModel
import com.example.biladoniga_toscanini_tejerina.utils.invisible
import com.example.biladoniga_toscanini_tejerina.utils.visible
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CreateTeamFragment : Fragment() {

    private var firstTeammateNameInput: TextInputEditText? = null
    private var secondTeammateNameInput: TextInputEditText? = null
    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var takeImageButton: ConstraintLayout? = null
    private var teammatesImage: ShapeableImageView? = null
    private var cameraIcon: View? = null
    private var takeImageBorder: View? = null

    private lateinit var currentPhotoPath: String
    private var currentPhotoUri: String? = null
    private lateinit var currentTeamId: String

    private val launchViewModel by sharedViewModel<LaunchViewModel>()

    companion object {
        private const val cameraRequestCode = 102
        private const val galleryRequestCode = 103
        private const val takePhoto = 0
        private const val chooseFromGallery = 1
        private const val cancel = 2
        private const val firstTeammatePosition = 0
        private const val secondTeammatePosition = 1

        const val teamId = "teamId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            (it as GameLaunchActivity).showLoader()
        }
        return inflater.inflate(R.layout.fragment_create_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentTeamId = arguments?.getString(teamId) ?: Team.firstTeam

        setToolbarTitle()
        initViews(view)
        setTeamData()
    }

    private fun initViews(view: View) {
        firstTeammateNameInput = view.findViewById(R.id.first_teammate_name_input)
        secondTeammateNameInput = view.findViewById(R.id.second_teammate_name_input)
        teammatesImage = view.findViewById(R.id.teammates_image)
        cameraIcon = view.findViewById(R.id.camera_icon)
        takeImageBorder = view.findViewById(R.id.camera_background)

        saveButton = view.findViewById(R.id.create_team_save_button)
        saveButton?.setOnClickListener {
            saveTeam()
        }

        cancelButton = view.findViewById(R.id.create_team_cancel_button)
        cancelButton?.setOnClickListener {
            activity?.onBackPressed()
        }

        takeImageButton = view.findViewById(R.id.take_image_button)
        takeImageButton?.setOnClickListener { getImage() }

    }

    private fun saveTeam() {
        launchViewModel.saveTeam(
            Team(
                currentTeamId,
                arrayListOf(
                    firstTeammateNameInput?.text.toString(),
                    secondTeammateNameInput?.text.toString()
                ),
                currentPhotoUri
            )
        )
        activity?.onBackPressed()
    }

    private fun hideTakeImageButton() {
        cameraIcon?.invisible()
        takeImageBorder?.invisible()
        teammatesImage?.visible()
    }

    private fun getImage() {
        showOptionPicker()
    }

    private fun showOptionPicker() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.add_team_photo))
            .setItems(
                arrayOf(
                    getString(R.string.take_photo),
                    //it.getString(R.string.choose_from_gallery), TODO(sofi)
                    getString(R.string.cancel)
                )
            ) { dialog, which ->
                when (which) {
                    takePhoto -> takePhoto()
                    //chooseFromGallery -> pickPhoto() TODO(sofi)
                    cancel -> dialog.dismiss()
                }
            }
        builder.show()
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun pickPhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForStoragePermission()
        } else {
            getImageFromGallery()
        }
    }

    private fun askForCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.packageManager?.let {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    showError()
                    null
                }

                context?.let { requiredContext ->
                    photoFile?.also { file ->
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requiredContext,
                            AUTHORITY,
                            file
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, cameraRequestCode)
                    }
                }
            }
        }
    }

    private fun askForStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchTakePictureIntent()
        }
        if (requestCode == STORAGE_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getImageFromGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                cameraRequestCode -> {
                    setImageFromCamera()
                }
                galleryRequestCode -> {
                    data?.let { intentData ->
                        val uri = intentData.data
                        viewLifecycleOwner.lifecycleScope.launch {
                            val takeFlags: Int = intentData.flags and
                                    (Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                            )
                            uri?.let {
                                activity?.contentResolver?.takePersistableUriPermission(
                                    it,
                                    takeFlags
                                )
                            }
                        }
                        setTeamImage(uri)
                    }
                }
            }
        }
    }

    // All this methods are from https://developer.android.com/training/camera/photobasics#kotlin
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat(DATE_FORMAT_PATTERN).format(Date())
        context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
            val storageDir = it
            return File.createTempFile(
                "${JPEG_PREFIX}_${timeStamp}",
                JPG,
                storageDir
            ).apply {
                currentPhotoPath = absolutePath
            }
        }
        return null
    }

    private fun getImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(galleryIntent, galleryRequestCode)
    }

    private fun setImageFromCamera() {
        val uri = Uri.fromFile(File(currentPhotoPath))
        setTeamImage(uri)
    }

    private fun showError() {
        activity?.let {
            (it as GameLaunchActivity).showError(getString(R.string.get_image_error))
        }
    }

    private fun setTeamData() {
        launchViewModel.getTeam(currentTeamId)
        launchViewModel.team.observe(
            viewLifecycleOwner,
            Observer { teamResponse ->
                if (teamResponse is Response.Success<Team> && teamResponse.data.id == currentTeamId) {
                    setTeamImage(teamResponse.data.image?.toUri())
                    setTeammatesNames(teamResponse.data.teammatesNames)
                }
            }
        )
    }

    private fun setTeamImage(uri: Uri?) {
        uri?.let {
            teammatesImage?.let { image ->
                image.setImageURI(uri)
                currentPhotoUri = uri.toString()
                hideTakeImageButton()
            }
        }
    }

    private fun setTeammatesNames(names: List<String>) {
        for (index in names.indices) {
            when (index) {
                firstTeammatePosition -> firstTeammateNameInput?.setText(names[index])
                secondTeammatePosition -> secondTeammateNameInput?.setText(names[index])
            }
        }
    }

    private fun setToolbarTitle() {
        activity?.let {
            (it as GameLaunchActivity).apply {
                when (currentTeamId) {
                    Team.firstTeam -> {
                        this.setToolbarTitle(getString(R.string.first_team))
                    }
                    Team.secondTeam -> {
                        this.setToolbarTitle(getString(R.string.second_team))
                    }
                    Team.thirdTeam -> {
                        this.setToolbarTitle(getString(R.string.third_team))
                    }
                }
            }
        }
    }
}
