package com.phamhungan.ballimpact.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.phamhungan.ballimpact.model.Ball;
import com.phamhungan.ballimpact.model.Config;
import com.phamhungan.ballimpact.model.Line;
import com.phamhungan.ballimpact.model.Table;
import com.phamhungan.ballimpact.util.DataUtil;

public class Database {

	Context mContext;
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDB;

	final static String DATABASE_NAME = "BallImpact";
	final String DATABASE_TABLE_Config = "Config";
	final String DATABASE_CREATE_Config = "CREATE TABLE Config (configId TEXT,ballPlayer1Id INT,ballPlayer2Id INT,tableId INT,player1Name TEXT,player2Name TEXT,lineId INT);";
	final String DATABASE_TABLE_Ball = "Ball";
	final String DATABASE_CREATE_Ball = "CREATE TABLE Ball (id INT,locked INT,p1Selected INT,p2Selected INT);";
	final String DATABASE_TABLE_Table = "mTable";
	final String DATABASE_CREATE_Table = "CREATE TABLE mTable (id INT,locked INT,selected INT);";
	final String DATABASE_TABLE_Line = "Line";
	final String DATABASE_CREATE_Line = "CREATE TABLE Line (id INT,selected INT);";
	public class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_Config);
			db.execSQL(DATABASE_CREATE_Ball);
			db.execSQL(DATABASE_CREATE_Table);
			db.execSQL(DATABASE_CREATE_Line);
		}

		
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d("GO TO UPGRADE DATABASE", "TRUE");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_Config);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_Ball);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_Table);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_Line);
			db.execSQL(DATABASE_CREATE_Config);
			db.execSQL(DATABASE_CREATE_Ball);
			db.execSQL(DATABASE_CREATE_Table);
			db.execSQL(DATABASE_CREATE_Line);
			db.setVersion(newVersion);
		}

		public int getConfigCount() {
			String countQuery = "SELECT  * FROM " + DATABASE_TABLE_Config;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			int cnt = cursor.getCount();
			cursor.close();
			return cnt;
		}

		public int getBallCount() {
			String countQuery = "SELECT  * FROM " + DATABASE_TABLE_Ball;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			int cnt = cursor.getCount();
			cursor.close();
			return cnt;
		}

		public int getTableCount() {
			String countQuery = "SELECT  * FROM " + DATABASE_TABLE_Table;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			int cnt = cursor.getCount();
			cursor.close();
			return cnt;
		}

		public int getLineCount() {
			String countQuery = "SELECT  * FROM " + DATABASE_TABLE_Line;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			int cnt = cursor.getCount();
			cursor.close();
			return cnt;
		}
	}

	public Database(Context context) {
		this.mContext = context;
	}

	public Database open() {
		try {
			mDBHelper = new DatabaseHelper(mContext, DATABASE_NAME, null,1);
			mDB = mDBHelper.getWritableDatabase();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
			close();
			mDBHelper = new DatabaseHelper(mContext, DATABASE_NAME, null,1);
			mDB = mDBHelper.getWritableDatabase();
		}

		return this;
	}

	public void close() {
		if (mDBHelper != null) {
			mDBHelper.close();
		}
	}

	public int getConfigCount() {
		return mDBHelper.getConfigCount();
	}

	public int getBallCount() {
		return mDBHelper.getBallCount();
	}

	public int getTableCount() {
		return mDBHelper.getTableCount();
	}

	public int getLineCount() {
		return mDBHelper.getLineCount();
	}

	public long insertConfig(Config config) {
		ContentValues value = new ContentValues();
		value.put("configId", config.getConfigId());
		value.put("ballPlayer1Id", config.getBallPlayer1Id());
		value.put("ballPlayer2Id", config.getBallPlayer2Id());
		value.put("tableId", config.getTableId());
		value.put("player1Name", config.getPlayer1Name());
		value.put("player2Name", config.getPlayer2Name());
		value.put("lineId", config.getLineId());
		boolean ss = false;
		while (!ss) {
			try {
				mDB.beginTransaction();
				ss = true;
			} catch (Exception e) {
				mDB = mDBHelper.getReadableDatabase();
				return -1;
			}
		}
		long idInsert = 0;
		try {
			idInsert = mDB.insertWithOnConflict(DATABASE_TABLE_Config, null,
					value, SQLiteDatabase.CONFLICT_IGNORE);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
		} finally {
			mDB.endTransaction();
		}
		return idInsert;
	}

	public Config getConfig(String id) {
		Config config = new Config();
		Cursor mCursor = null;
		try {
			mCursor = mDB.query(true, DATABASE_TABLE_Config, new String[] {
					"configId", "ballPlayer1Id", "ballPlayer2Id", "tableId","player1Name", "player2Name","lineId"
			}, "configId like ?", new String[]{id+"%"}, null, null, null, null);
			if (mCursor != null && mCursor.moveToFirst()) {
				do {
					config.setConfigId(mCursor.getString(0));
					config.setBallPlayer1Id(mCursor.getInt(1));
					config.setBallPlayer2Id(mCursor.getInt(2));
					config.setTableId(mCursor.getInt(3));
					config.setPlayer1Name(mCursor.getString(4));
					config.setPlayer2Name(mCursor.getString(5));
					config.setLineId(mCursor.getInt(6));
				} while (mCursor.moveToNext());
			}
		} finally {
			mCursor.close();
		}
		return config;
	}

	public long insertBall(Ball ball) {
		ContentValues value = new ContentValues();
		value.put("id", ball.getBallId());
		value.put("locked", ball.getLocked());
		if(ball.getIsP1Selected()==1){
			value.put("p1Selected", 1);
		}
		if (ball.getIsP2Selected()==1){
			value.put("p2Selected", 1);
		}
		if(ball.getIsP1Selected()==0&&ball.getIsP2Selected()==0) {
			value.put("p1Selected", 0);
			value.put("p2Selected", 0);
		}
		boolean ss = false;
		while (!ss) {
			try {
				mDB.beginTransaction();
				ss = true;
			} catch (Exception e) {
				mDB = mDBHelper.getReadableDatabase();
				return -1;
			}
		}
		long idInsert = 0;
		try {
			idInsert = mDB.insertWithOnConflict(DATABASE_TABLE_Ball, null,
					value, SQLiteDatabase.CONFLICT_IGNORE);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
		} finally {
			mDB.endTransaction();
		}
		return idInsert;
	}

	public long insertTable(Table table) {
		ContentValues value = new ContentValues();
		value.put("id", table.getTableId());
		value.put("locked", table.getLocked());
		if(table.getIsSelected()==1){
			value.put("selected", 1);
		}else {
			value.put("selected", 0);
		}
		boolean ss = false;
		while (!ss) {
			try {
				mDB.beginTransaction();
				ss = true;
			} catch (Exception e) {
				mDB = mDBHelper.getReadableDatabase();
				return -1;
			}
		}
		long idInsert = 0;
		try {
			idInsert = mDB.insertWithOnConflict(DATABASE_TABLE_Table, null,
					value, SQLiteDatabase.CONFLICT_IGNORE);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
		} finally {
			mDB.endTransaction();
		}
		return idInsert;
	}

	public long insertLine(Line line) {
		ContentValues value = new ContentValues();
		value.put("id", line.getLineId());
		if(line.getIsSelected()==1){
			value.put("selected", 1);
		}else {
			value.put("selected", 0);
		}
		boolean ss = false;
		while (!ss) {
			try {
				mDB.beginTransaction();
				ss = true;
			} catch (Exception e) {
				mDB = mDBHelper.getReadableDatabase();
				return -1;
			}
		}
		long idInsert = 0;
		try {
			idInsert = mDB.insertWithOnConflict(DATABASE_TABLE_Line, null,
					value, SQLiteDatabase.CONFLICT_IGNORE);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
		} finally {
			mDB.endTransaction();
		}
		return idInsert;
	}

	public Ball getBall(int id) {
		Ball result = new Ball();
		if (mDBHelper.getBallCount() == 0)
			return result;

		Cursor mCursor = null;
		String selection = "id = " + id + "";
		try {
			mCursor = mDB.query(true, DATABASE_TABLE_Ball, new String[] {"id","locked","p1Selected","p2Selected"}, selection, null, null, null, null, null);
			if (mCursor != null && mCursor.moveToFirst()) {
				do {
					Ball ball = new Ball();
					ball.setBallId(mCursor.getInt(0));
					ball.setLocked(mCursor.getInt(1));
					if(mCursor.getInt(2)==1){
						ball.setIsP1Selected(1);
					}
					if (mCursor.getInt(3)==1){
						ball.setIsP2Selected(1);
					}
					if (mCursor.getInt(2)==0&&mCursor.getInt(3)==0){
						ball.setIsP1Selected(0);
						ball.setIsP2Selected(0);
					}
					result = ball;
				} while (mCursor.moveToNext());
			}
		} finally {
			mCursor.close();
		}
		return result;
	}

	public Table getTable(int id) {
		Table result = new Table();
		if (mDBHelper.getBallCount() == 0)
			return result;

		Cursor mCursor = null;
		String selection = "id = " + id + "";
		try {
			mCursor = mDB.query(true, DATABASE_TABLE_Table, new String[] {"id","locked","selected"}, selection, null, null, null, null, null);
			if (mCursor != null && mCursor.moveToFirst()) {
				do {
					Table table = new Table();
					table.setTableId(mCursor.getInt(0));
					table.setLocked(mCursor.getInt(1));
					table.setIsSelected(mCursor.getInt(2));
					result = table;
				} while (mCursor.moveToNext());
			}
		} finally {
			mCursor.close();
		}
		return result;
	}

	public void updateSelectedTable(int tableId,int selected) {
		boolean ss = false;
		while (!ss) {
			try {
				mDB.beginTransaction();
				ss = true;
			} catch (Exception e) {
				mDB = mDBHelper.getReadableDatabase();
			}
		}
		try {
			mDB.execSQL("UPDATE " + DATABASE_TABLE_Table
					+ " SET selected = " + selected + ""
					+ " WHERE id = " + tableId + "");
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
		} finally {
			mDB.endTransaction();
		}
	}

	public void updateSelectedLine(int lineId,int selected) {
		boolean ss = false;
		while (!ss) {
			try {
				mDB.beginTransaction();
				ss = true;
			} catch (Exception e) {
				mDB = mDBHelper.getReadableDatabase();
			}
		}
		try {
			mDB.execSQL("UPDATE " + DATABASE_TABLE_Line
					+ " SET selected = " + selected + ""
					+ " WHERE id = " + lineId + "");
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
		} finally {
			mDB.endTransaction();
		}
	}

	public void updateBall(int ballId,Object value,String field) {
		boolean ss = false;
		while (!ss) {
			try {
				mDB.beginTransaction();
				ss = true;
			} catch (Exception e) {
				mDB = mDBHelper.getReadableDatabase();
			}
		}
		try {
			mDB.execSQL("UPDATE " + DATABASE_TABLE_Ball
					+ " SET "+field+" = " + value + ""
					+ " WHERE id = " + ballId + "");
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
		} finally {
			mDB.endTransaction();
		}
	}

	public void updateConfig() {
		Config config = DataUtil.config;
		boolean ss = false;
		while (!ss) {
			try {
				mDB.beginTransaction();
				ss = true;
			} catch (Exception e) {
				mDB = mDBHelper.getReadableDatabase();
			}
		}
		try {
			mDB.execSQL("UPDATE " + DATABASE_TABLE_Config
					+ " SET ballPlayer1Id = " + config.getBallPlayer1Id() + ""
					+ " , ballPlayer2Id = " + config.getBallPlayer2Id() + ""
					+ " , tableId = " + config.getTableId() + ""
					+ " , lineId = " + config.getLineId() + ""
					+ " , player1Name = \"" + config.getPlayer1Name() + "\""
					+ " , player2Name = \"" + config.getPlayer2Name() + "\""
					+ " WHERE configId = " + config.getConfigId() + "");
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			mDB = mDBHelper.getReadableDatabase();
		} finally {
			mDB.endTransaction();
		}
	}
}
