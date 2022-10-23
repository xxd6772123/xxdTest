package com.xxd.common.basic.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.blankj.utilcode.util.Utils;
import com.xxd.common.basic.bean.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:通讯录联系人工具类
 */
public class ContactUtil {

    /**
     * 获取通讯录中所有联系人信息
     *
     * @return
     */
    @SuppressLint("SupportAnnotationUsage")
    @RequiresPermission(allOf = {Manifest.permission.READ_CONTACTS})
    public static List<ContactInfo> getAllContact() {
        Context context = Utils.getApp();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        List<ContactInfo> contactInfoList = new ArrayList<>();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, null, null, null);    //获得_id属性
        while (cursor.moveToNext()) {
            StringBuilder buf = new StringBuilder();
            int id = cursor.getInt(0);//获得id并且在data中寻找数据
            buf.append("id=" + id);
            uri = Uri.parse("content://com.android.contacts/contacts/" + id + "/data");    //如果要获得data表中某个id对应的数据，则URI为content://com.android.contacts/contacts/#/data
            final String DATA1 = ContactsContract.Data.DATA1;
            final String MIME_TYPE = ContactsContract.Data.MIMETYPE;
            Cursor cursor2 = resolver.query(uri, new String[]{DATA1, MIME_TYPE}, null, null, null);    //data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setId(id);

            while (cursor2.moveToNext()) {
                String data = cursor2.getString(cursor2.getColumnIndex(DATA1));
                String mimeTypeValue = cursor2.getString(cursor2.getColumnIndex(MIME_TYPE));
                if (ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE.equalsIgnoreCase(mimeTypeValue)) {//名字
                    buf.append(",name=" + data);
                    contactInfo.setName(data);

                } else if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equalsIgnoreCase(mimeTypeValue)) {//电话
                    buf.append(",phone=" + data);
                    contactInfo.setPhone(data);

                } else if (ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE.equalsIgnoreCase(mimeTypeValue)) {//邮件
                    buf.append(",email=" + data);
                    contactInfo.setEmail(data);

                } else if (ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE.equalsIgnoreCase(mimeTypeValue)) {//地址
                    buf.append(",address=" + data);
                    contactInfo.setAddress(data);

                } else if (ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE.equalsIgnoreCase(mimeTypeValue)) {//单位组织
                    buf.append(",organization=" + data);
                    contactInfo.setOrganization(data);

                }
            }
            String str = buf.toString();
            //Log.i("Contacts", str);
            contactInfoList.add(contactInfo);
        }
        return contactInfoList;
    }

    /**
     * 保存单个联系人信息
     *
     * @param contactInfo
     * @return
     */
    public static boolean addContact(ContactInfo contactInfo) {
        if (contactInfo == null) {
            return false;
        }

        Context context = Utils.getApp();
        String name = contactInfo.getName();
        String phone = contactInfo.getPhone();
        String email = contactInfo.getEmail();
        String address = contactInfo.getAddress();
        String organization = contactInfo.getOrganization();
        boolean isOk = false;
        try {
            //插入raw_contacts表，并获取_id属性
            Uri uri = ContactsContract.RawContacts.CONTENT_URI;
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            long contact_id = ContentUris.parseId(resolver.insert(uri, values));

            //插入data表
            uri = ContactsContract.Data.CONTENT_URI;
            final String KEY_RAW_CONTACT_ID = ContactsContract.CommonDataKinds.Identity.RAW_CONTACT_ID;
            final String KEY_DATA1 = ContactsContract.Data.DATA1;

            //add Name
            values.put(KEY_RAW_CONTACT_ID, contact_id);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            values.put(KEY_DATA1, name);
            resolver.insert(uri, values);
            values.clear();

            //add Phone
            values.put(KEY_RAW_CONTACT_ID, contact_id);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            values.put(KEY_DATA1, phone);
            resolver.insert(uri, values);
            values.clear();

            //add email
            values.put(KEY_RAW_CONTACT_ID, contact_id);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            values.put(KEY_DATA1, email);
            resolver.insert(uri, values);
            values.clear();

            //add address
            values.put(KEY_RAW_CONTACT_ID, contact_id);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
            values.put(KEY_DATA1, address);
            resolver.insert(uri, values);
            values.clear();

            //add Organization
            values.put(KEY_RAW_CONTACT_ID, contact_id);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
            values.put(KEY_DATA1, organization);
            resolver.insert(uri, values);
            values.clear();

            isOk = true;
        } catch (Exception e) {
            isOk = false;
            e.printStackTrace();
        }
        return isOk;
    }

    /**
     * 保存多个联系人
     *
     * @param contactInfoList
     */
    public static void addAllContact(List<ContactInfo> contactInfoList) {
        if (contactInfoList == null) return;
        for (ContactInfo info : contactInfoList) {
            addContact(info);
        }
    }

    /**
     * 删除某个联系人
     *
     * @param contactInfo
     * @return
     */
    public static boolean delContact(@NonNull ContactInfo contactInfo) {
        long contactId = getContactId(contactInfo);
        if (contactId == -1) {
            return false;
        }
        return delContact(contactId);
    }

    /**
     * 删除联系人
     *
     * @param contactInfoList
     */
    public static void delAllContact(List<ContactInfo> contactInfoList) {
        if (contactInfoList == null) return;
        for (ContactInfo info : contactInfoList) {
            delContact(info);
        }
    }

    /**
     * 根据contacts表中的id来删除对应的联系人的信息
     *
     * @param contactId ContactsContract.Contacts.CONTENT_URI对应的表中的id字段的值
     */
    public static boolean delContact(long contactId) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        //delete contact
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null)
                .build());
        //delete contact information such as phone number,email
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=" + contactId, null)
                .build());

        try {
            Utils.getApp().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据ContactInfo获取该联系人在contacts表中对应的id
     *
     * @param contactInfo
     * @return
     */
    public static long getContactId(@NonNull ContactInfo contactInfo) {
        long id = -1;
        if (contactInfo == null || TextUtils.isEmpty(contactInfo.getName())) {
            return id;
        }
        ContentResolver resolver = Utils.getApp().getContentResolver();
        String[] projection = new String[]{ContactsContract.Contacts._ID};
        String selection = ContactsContract.Contacts.DISPLAY_NAME + "=?";
        String[] args = new String[]{contactInfo.getName()};
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, projection, selection, args, null);
        if (cursor == null) {
            return id;
        }
        if (cursor.moveToFirst()) {
            id = cursor.getLong(0);
        }
        return id;
    }

}
