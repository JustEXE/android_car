/*************************************************************************************************
 * 版权所有 (C)2015,  成都市商联汇通技术有限公司
 * 
 * 文件名称：MyContentProvider.java
 * 内容摘要：
 * 当前版本：
 * 作         者： 翟彬
 * 完成日期：2016年4月15日下午4:04:09
 * 修改记录：
 * 修改日期：
 * 版   本  号：
 * 修   改  人：
 * 修改内容：
 ************************************************************************************************/
package com.zb.mytest.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * @ClassName: MyContentProvider
 * @Description: TODO
 * @author 翟彬
 * @date 2016年4月15日 下午4:04:09
 */
public class MyContentProvider extends ContentProvider {
	private static final String TAG = "MyContentProvider";
	private MyData mydata = null;
	private SQLiteDatabase dbBase;

	@Override
	public boolean onCreate() {
		mydata = new MyData(getContext());
		if (mydata == null) {
			return false;
		} else {
			dbBase = mydata.getWritableDatabase();
			return true;
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// dbBase.query(TABLE_NAME, new String[]{"user"}, selection,
		// selectionArgs, null, null, sortOrder);
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(TABLE_NAME);
		Cursor c = builder.query(dbBase, null, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = dbBase.insert(TABLE_NAME, null, values);
		if (id > 0) {
			Uri rowUri = ContentUris.appendId(Uri.parse("content://com.zb.test.contentprovider").buildUpon(), id).build();
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLiteException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static final String DB_NAME = "contentprovider.db";
	private static final String TABLE_NAME = "user";
	private static final int VERSION = 1;

	private class MyData extends SQLiteOpenHelper {
		private SQLiteDatabase db = null;

		public MyData(Context context) {
			super(context, DB_NAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			this.db = db;
			db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,user TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP　TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}
}
