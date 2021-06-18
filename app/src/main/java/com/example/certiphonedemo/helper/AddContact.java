package com.example.certiphonedemo.helper;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.certiphonedemo.MainActivity;

import java.util.ArrayList;

public class AddContact extends AppCompatActivity {
    MainActivity mainActivity;

    /**
     * Permet de vérifier si on a bien la permission de faire l'action précisser
     *
     * @param context    le context de l'application qui a besoin des permissions
     * @param permission la permission que l'on souhaite vérifier
     * @return true si la permission est accorder, sinon false
     */
    public boolean checkPermission(Context context, String permission) {
        int check = ContextCompat.checkSelfPermission(context, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * C'est le constructeur de la classe il permets de demander les permission qu'il à besoin
     *
     * @param mainActivity le main activity pour les requêtes des permissions
     */
    public AddContact(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        if (!checkPermission(mainActivity.getApplicationContext(), Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        }

    }

    /**
     * permets de créer un contact 2 informations, a condition que le numéro de téléphone n'est pas déjà attribuer a un contact
     *
     * @param displayName  le nom du contact à créer
     * @param mobileNumber le numéro de téléphone du contact
     * @return true si le contact à été créer et false en cas d'erreur
     */
    public boolean add(String displayName, String mobileNumber) {
        return add(displayName, mobileNumber, null, null, null, null, null);
    }

    /**
     * permets de créer un contact avec plusieurs informations, a condition que le numéro de téléphone n'est pas déjà attribuer a un contact
     *
     * @param displayName  le nom du contact à créer
     * @param mobileNumber le numéro de téléphone du contact
     * @param homeNumber   le numéro fix du contact (peut etre null si inexistant)
     * @param workNumber   le numéro de travail du contact (peut etre null si inexistant)
     * @param emailID      le mail du contact (peut etre null si inexistant)
     * @param company      l'entreprise ou travail le contact (peut etre null si inexistant)
     * @param jobTitle     l'emplois du contact, une compagny doit être transmise (peut etre null si inexistant)
     * @return true si le contact à été créer et false en cas d'erreur
     */
    public boolean add(String displayName, String mobileNumber, String homeNumber, String workNumber, String emailID, String company, String jobTitle) {
        if (!checkPermission(mainActivity.getApplicationContext(), Manifest.permission.WRITE_CONTACTS)) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.WRITE_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        }
        if (!checkPermission(mainActivity.getApplicationContext(), Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        }
        if (checkPermission(mainActivity.getApplicationContext(), Manifest.permission.READ_CONTACTS) && checkPermission(mainActivity.getApplicationContext(), Manifest.permission.WRITE_CONTACTS) && !contactExists(mainActivity.getApplicationContext(), mobileNumber)) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
            //------------------------------------------------------ nom
            if (displayName != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName).build());
            }
            //------------------------------------------------------ numéro mobile
            if (mobileNumber != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
            }
            //------------------------------------------------------ numéro de domicile
            if (homeNumber != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME).build());
            }
            //------------------------------------------------------ numéro de travail
            if (workNumber != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK).build());
            }
            //------------------------------------------------------ Email
            if (emailID != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID).withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());
            }
            //------------------------------------------------------ Entreprise
            if (company != null && jobTitle != null) {
                if (!company.equals("") && !jobTitle.equals("")) {
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company).withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK).withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle).withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK).build());
                }
            }
            //Demande au contact provider de créer un contact
            try {
                mainActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mainActivity.getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * permets de vérifier si le numéro existe dans la liste des contacts
     *
     * @param context le contexte du mainActivity
     * @param number  le numéro de téléphone a vérifier
     * @return true si le numéro de téléphone est attribuer a un contact
     */
    public boolean contactExists(Context context, String number) {
        if (number != null) {
            ContentResolver cr = context.getContentResolver();
            Cursor curContacts = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            while (curContacts.moveToNext()) {
                String contactNumber = curContacts.getString(curContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (number.equals(contactNumber)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
