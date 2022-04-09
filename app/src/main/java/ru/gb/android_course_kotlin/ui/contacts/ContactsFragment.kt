package ru.gb.android_course_kotlin.ui.contacts

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.provider.Telephony.Mms.Addr.CONTACT_ID
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.gb.android_course_kotlin.R
import ru.gb.android_course_kotlin.databinding.FragmentContactsBinding

const val REQUEST_CODE = 42

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
    }

    private fun checkPermissions() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                            getContacts()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Contacts access")
                        .setMessage("Explication")
                        .setPositiveButton("Allow") { _, _->
                            requestPermissions()
                        }
                        .setNegativeButton("Deni") { dialog, _->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }

                else -> {
                    requestPermissions()
                }
            }
        }
    }

    private fun requestPermissions() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Contacts access")
                            .setMessage("This screen will be empty")
                            .setNegativeButton("Dismiss") { dialog, _-> dialog.dismiss()}
                            .setPositiveButton("Allow") { _, _-> openSettings()}
                            .create()
                            .show()
                    }
                }
                return
            }
        }
    }

    private fun openSettings() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
            intent.data = Uri.fromParts("package", "ru.gb.android_course_kotlin", null)
        })
    }

    private fun getContacts() {
        context?.let { ctx ->
            val contentResolver: ContentResolver = ctx.contentResolver
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val namePosition = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val name = if (namePosition >= 0)
                            cursor.getString(namePosition)
                        else "Column not found"
                        addView(ctx, name)

                        var phoneNum = "Phone not found."
                        val hasPhoneColumnPosition = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                            if (hasPhoneColumnPosition >=0) {
                                val idPosition =
                                    cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID)
                                phoneNum = getPhoneNumber(cursor.getString(idPosition))
                            }
                        addView(ctx, phoneNum)
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }

    private fun getPhoneNumber(id: String): String {
        var number = "";
        context?.let { ctx ->
            val contentResolver: ContentResolver = ctx.contentResolver
            val cursorWithPhoneNumber: Cursor? = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                "$CONTACT_ID=$id",
                null,
                null
            )

            cursorWithPhoneNumber?.let { cursor ->
                if (cursor.moveToPosition(0)) {
                    val phonePosition = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    number = if (phonePosition >= 0)
                        cursor.getString(phonePosition)
                    else "Phone number not found"
                }
            }
            cursorWithPhoneNumber?.close()
        }
        return number;
    }

    private fun addView(context: Context, textToShow: String) {
        binding.fragmentContactsContainer.addView(AppCompatTextView(context).apply {
            text = textToShow
            textSize = resources.getDimension(R.dimen.contacts_fragment_container_text_size)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() =
            ContactsFragment()
    }
}