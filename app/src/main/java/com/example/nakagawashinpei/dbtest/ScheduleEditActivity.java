package com.example.nakagawashinpei.dbtest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleEditActivity extends AppCompatActivity {

    private Realm mRealm;
    EditText mDataEdit;
    EditText mTitleEdit;
    EditText mDetailEdit;
    VideoView mVideoView;
    Button mDelete;
    private String mUri;
    private static final int RESULT_PICK_IMAGEFILE = 1001;
    ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);

        mRealm = Realm.getDefaultInstance();
        mDataEdit = (EditText)findViewById(R.id.dateEdit);
        mTitleEdit = (EditText)findViewById(R.id.gim);
        mDetailEdit = (EditText)findViewById(R.id.detailEdit);
        mDelete = (Button) findViewById(R.id.delete);
        mVideoView = (VideoView)findViewById(R.id.video);
        //mImageView = (ImageView)findViewById(R.id.imageView);


        //サムネイル取得
        Bitmap bitmap = null;
        // ThumbnailUtilsインスタンス作成
        ThumbnailUtils tu = new ThumbnailUtils();

        long scheduleId = getIntent().getLongExtra("schedule_id",-1);
        if (scheduleId != -1){
            RealmResults<Schedule> results = mRealm.where(Schedule.class).equalTo("id",scheduleId).findAll();
            Schedule schedule = results.first();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(schedule.getData());
            mDataEdit.setText(date);
            mTitleEdit.setText(schedule.getTitle());
            mDetailEdit.setText(schedule.getDetail());
            mUri = schedule.getMovie();
            mVideoView.setVideoURI(Uri.parse(mUri));
            mVideoView.setMediaController(new MediaController(this));
            //Toast.makeText(this, mUri, Toast.LENGTH_LONG).show();

            //bitmap = getCustomThumbnail(,2);
            //mImageView.setImageBitmap(bitmap);

            mDelete.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.INVISIBLE);
        }
    }

    public void onSaveTapped(View view){
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd");
        Date dateParse = new Date();
        try{
            dateParse = sfd.parse(mDataEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date date = dateParse;
        long scheduleId = getIntent().getLongExtra("schedule_id",-1);
        if (scheduleId != -1) {
            final RealmResults<Schedule> results = mRealm.where(Schedule.class).equalTo("id",scheduleId).findAll();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Schedule schedule = results.first();
                    schedule.setDate(date);
                    schedule.setTitle(mTitleEdit.getText().toString());
                    schedule.setDetail(mDetailEdit.getText().toString());
                    schedule.setMovie(mUri.toString());
                }
            });
            Snackbar.make(findViewById(android.R.id.content),"アップデートしました",Snackbar.LENGTH_LONG)
                    .setAction("戻る",new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            finish();;
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();
        } else {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Number maxId = realm.where(Schedule.class).max("id");
                    long nextId = 0;
                    if (maxId != null) {
                        nextId = maxId.longValue() + 1;
                    }
                    Schedule schedule = realm.createObject(Schedule.class, new Long(nextId));
                    schedule.setDate(date);
                    schedule.setTitle(mTitleEdit.getText().toString());
                    schedule.setDetail(mDetailEdit.getText().toString());
                    schedule.setMovie(mUri.toString());
                }
            });
            Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onDeleteTapped(View view) {
        final long scheduleId = getIntent().getLongExtra("schedule_id",-1);
        if (scheduleId != -1) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Schedule schedule = realm.where(Schedule.class)
                            .equalTo("id",scheduleId).findFirst();
                    schedule.deleteFromRealm();
                }
            });
            finish();;
        }
    }

    public void OnMovieTapped(View view){
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if(resultData.getData() != null){

                //ParcelFileDescriptor pfDescriptor = null;
                //try{
                Uri uri = resultData.getData();
                mUri = uri.toString();

                Log.d("naka", "Video Uri :" + uri.getPath());
                //File file = new File(uri.getPath());


                /*
                // これはパスを取るためだけのコードです。
                // ContentResolver経由でファイルパスを取得
                ContentResolver cr = getContentResolver();
                String[] columns = {
                        MediaStore.Images.Media.DATA
                };
                Cursor c = cr.query(resultData.getData(), columns, null, null, null);
                int column_index = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                c.moveToFirst();
                String path = c.getString(column_index);
                Log.v("test", "path=" + path);
*/

                // Uriを表示
                /*
                textView.setText(
                        String.format(Locale.US, "Uri:　%s",uri.toString()));
                        */
                //仮消す
                // mVideoView = (VideoView)findViewById(R.id.video);
                // 動画の指定（mp4の読込み）
                mVideoView.setVideoURI(Uri.parse(uri.toString()));
                mVideoView.start();
                mVideoView.setMediaController(new MediaController(this));




                  /*  pfDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                    if(pfDescriptor != null){
                        FileDescriptor fileDescriptor = pfDescriptor.getFileDescriptor();
                        Bitmap bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        pfDescriptor.close();
                        imageView.setImageBitmap(bmp);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        if(pfDescriptor != null){
                            pfDescriptor.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }*/

            }
        }
    }

    public Bitmap getCustomThumbnail(File path, long time){
        Bitmap thumbnail = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path.getPath());

        //秒単位で指定
        thumbnail = retriever.getFrameAtTime(1000 * 1000 * time);
        //サムネイルを任意のサイズにリサイズ
        thumbnail = ThumbnailUtils.extractThumbnail(thumbnail, 640, 360);

        return thumbnail;
    }

}
