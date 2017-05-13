package com.example.ldy.notes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Observable;

import me.iwf.photopicker.PhotoPicker;
import rx.Subscriber;
import rx.Subscription;

public class NewActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mDate;
    private EditText mTime;
    private EditText mContent;

    private Note newNote;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;

    private String key;

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference storageRef = mStorage.getReference();

    private ProgressDialog loadingDialog;
    private ProgressDialog insertDialog;
    private Subscription subsLoading;
    private Subscription subsInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mTitle = (EditText)findViewById(R.id.noteTitle);
        mDate = (EditText)findViewById(R.id.setDate);
        mTime = (EditText)findViewById(R.id.setTime);
        mContent = (EditText)findViewById(R.id.noteContent);

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mUserId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("notes/" + mUserId);

        Intent getIntent = getIntent();
        key = getIntent.getStringExtra("noteKey");

        if (key != null) {
            ref.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Note newNote = dataSnapshot.getValue(Note.class);
                    mTitle.setText(newNote.getTitle());
                    mContent.setText(newNote.getContent());
                    mDate.setText(newNote.getDate());
                    mTime.setText(newNote.getTime());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save,menu);
        return true;
    }

    public void setDate(View v){
        Intent go = new Intent(this, DateActivity.class);
        startActivityForResult(go, 1);
    }

    public void setTime(View v){
        Intent go = new Intent(this, TimeActivity.class);
        startActivityForResult(go, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("chosenDate");
                    mDate.setText(returnValue);
                }
                break;
            }
            case (2) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("chosenTime");
                    mTime.setText(returnValue);
                }
                break;
            }
            case (RESULT_OK): {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (requestCode == 1){
                            //处理调用系统图库
                            insertImagesSync(data);
                        } else if (requestCode == PhotoPicker.REQUEST_CODE){
                            //异步方式插入图片
                            insertImagesSync(data);
                        }
                    }
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveEvent:
                saveEvent();
                break;
            case R.id.cancelEvent:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveEvent() {
        newNote = new Note();
        newNote.setTitle(mTitle.getText().toString());
        newNote.setContent(mContent.getText().toString());
        newNote.setDate(mDate.getText().toString());
        newNote.setTime(mTime.getText().toString());
        DatabaseReference newNoteRef;
        if (key != null) {
            newNoteRef = ref.child(key);
        } else {
            newNoteRef = ref.push();
        }
        newNoteRef.setValue(newNote);
        newNote.setKey(newNoteRef.getKey());
        finish();
    }

    public void uploadImg(View view) {
        PhotoPicker.builder()
                .setPhotoCount(5)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }
    private void insertImagesSync(final Intent data){
        insertDialog.show();

        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    et_new_content.measure(0, 0);
                    int width = ScreenUtils.getScreenWidth(NewActivity.this);
                    int height = ScreenUtils.getScreenHeight(NewActivity.this);
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //可以同时插入多张图片
                    for (String imagePath : photos) {
                        //Log.i("NewActivity", "###path=" + imagePath);
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片
                        //bitmap = BitmapFactory.decodeFile(imagePath);
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
                        //Log.i("NewActivity", "###imagePath="+imagePath);

                        final String[] downloadUriPath = new String[1];
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] data = baos.toByteArray();

                        HashFunction hashFunction = Hashing.farmHashFingerprint64();
                        HashCode hashCode = hashFunction.newHasher().putString(baos.toString(), Charset.defaultCharset()).hash();
                        String filename = hashCode.toString();

                        UploadTask uploadTask = storageRef.child(filename).putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        });

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                downloadUriPath[0] = taskSnapshot.getDownloadUrl().toString();
                            }
                        });

                        UploadTask.TaskSnapshot result = com.google.android.gms.tasks.Tasks.await(uploadTask);
                        downloadUriPath[0] = result.getDownloadUrl().toString();

                        subscriber.onNext(downloadUriPath[0]);
                    }
                    subscriber.onCompleted();
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        insertDialog.dismiss();
                        et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), " ");
                        showToast("图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        insertDialog.dismiss();
                        showToast("图片插入失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        et_new_content.insertImage(imagePath, et_new_content.getMeasuredWidth());
                    }
                });
    }
}
