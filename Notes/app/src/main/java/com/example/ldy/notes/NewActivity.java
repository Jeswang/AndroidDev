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
import android.widget.ImageView;
import android.widget.Toast;


import com.example.ldy.ScreenUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mDate;
    private EditText mTime;
    private EditText mContent;

    private String imageURL;

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

    private boolean mIgnoreChange = false;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("数据加载中...");
        loadingDialog.setCanceledOnTouchOutside(false);

        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);

        mTitle = (EditText)findViewById(R.id.noteTitle);
        mDate = (EditText)findViewById(R.id.setDate);
        mTime = (EditText)findViewById(R.id.setTime);
        mContent = (EditText)findViewById(R.id.noteContent);
        mImageView = (ImageView)findViewById(R.id.imageView);

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
                    if (!mIgnoreChange) {
                        Note newNote = dataSnapshot.getValue(Note.class);
                        mTitle.setText(newNote.getTitle());
                        mContent.setText(newNote.getContent());
                        mDate.setText(newNote.getDate());
                        mTime.setText(newNote.getTime());
                        if (newNote.getImageURL() != null) {
                            imageURL = newNote.getImageURL();
                            showImage(newNote.getImageURL());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public void uploadImage(View view){
        PhotoPicker.builder()
                .setPhotoCount(1)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    private void insertImagesSync(final Intent data){
        insertDialog.show();

        subsInsert = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try{
                    int width = ScreenUtils.getScreenWidth(NewActivity.this);
                    int height = ScreenUtils.getScreenHeight(NewActivity.this);
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    for (String imagePath : photos) {
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片

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
                            }
                        });

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageURL = taskSnapshot.getDownloadUrl().toString();
                            }
                        });

                        UploadTask.TaskSnapshot result = com.google.android.gms.tasks.Tasks.await(uploadTask);
                        downloadUriPath[0] = result.getDownloadUrl().toString();

                        subscriber.onNext(bitmap);
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
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        insertDialog.dismiss();
                        showToast("图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        insertDialog.dismiss();
                        showToast("图片插入失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(Bitmap image) {
                        mImageView.setImageBitmap(image);
                    }
                });
    }

    /** 显示吐司 **/
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showImage(final String imageURL) {
        loadingDialog.show();

        subsLoading = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                showEditData(subscriber, imageURL);
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        showToast("解析错误：图片无法下载");
                    }

                    @Override
                    public void onNext(Bitmap image) {
                        mImageView.setImageBitmap(image);
                    }
                });
    }

    protected void showEditData(Subscriber<? super Bitmap> subscriber, String imageURL) {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

        try{
            Bitmap image = imageLoader.loadImageSync(imageURL);
            subscriber.onNext(image);
            subscriber.onCompleted();
        }catch (Exception e){
            e.printStackTrace();
            subscriber.onError(e);
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
            case (PhotoPicker.REQUEST_CODE) : {
                insertImagesSync(data);
                break;
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
            case R.id.removeEvent:
                if (key != null) {
                    DatabaseReference newNoteRef = ref.child(key);
                    mIgnoreChange = true;
                    newNoteRef.removeValue();
                }
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
        newNote.setImageURL(imageURL);
        DatabaseReference newNoteRef;
        if (key != null) {
            newNoteRef = ref.child(key);
        } else {
            newNoteRef = ref.push();
        }
        mIgnoreChange = true;
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
}
