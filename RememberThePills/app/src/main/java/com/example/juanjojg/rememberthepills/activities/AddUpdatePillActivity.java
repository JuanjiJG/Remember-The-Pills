package com.example.juanjojg.rememberthepills.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.juanjojg.rememberthepills.BuildConfig;
import com.example.juanjojg.rememberthepills.R;
import com.example.juanjojg.rememberthepills.database.PillOperations;
import com.example.juanjojg.rememberthepills.models.Pill;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddUpdatePillActivity extends BaseActivity {
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final String EXTRA_PILL_ID = "pillID";
    private static final String EXTRA_ADD_UPDATE = "add_update_mode";
    private EditText mEditTextName;
    private EditText mEditTextDescription;
    private Button mButtonTakePhoto;
    private ImageView mImageViewPhoto;
    private Pill mNewPill;
    private Pill mOldPill;
    private String mMode;
    private PillOperations mPillOps;
    private Uri mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_pill);
        this.mOldPill = new Pill();
        this.mNewPill = new Pill();
        this.mEditTextName = (EditText) findViewById(R.id.edit_name);
        this.mEditTextDescription = (EditText) findViewById(R.id.edit_description);
        this.mImageViewPhoto = (ImageView) findViewById(R.id.pill_image);
        this.mButtonTakePhoto = (Button) findViewById(R.id.button_take_photo);
        this.mPillOps = new PillOperations(this);
        this.mPillOps.open();

        mButtonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mFile);

                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mButtonTakePhoto.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        this.mMode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if (mMode.equals("Update")) {
            initializePill(getIntent().getLongExtra(EXTRA_PILL_ID, 0));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                mButtonTakePhoto.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_add_update_pill, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_menu_item:
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                return true;
            case R.id.save_pill_menu_item:
                if (mMode.equals("Add")) {
                    mNewPill.setPillName(mEditTextName.getText().toString());
                    mNewPill.setPillDescription(mEditTextDescription.getText().toString());
                    mNewPill.setPillPhoto(((BitmapDrawable)mImageViewPhoto.getDrawable()).getBitmap());
                    mPillOps.addPill(mNewPill);
                    Toast t = Toast.makeText(this, R.string.add_pill_success, Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    mOldPill.setPillName(mEditTextName.getText().toString());
                    mOldPill.setPillDescription(mEditTextDescription.getText().toString());
                    mOldPill.setPillPhoto(((BitmapDrawable)mImageViewPhoto.getDrawable()).getBitmap());
                    mPillOps.updatePill(mOldPill);
                    Toast t = Toast.makeText(AddUpdatePillActivity.this, R.string.update_pill_success, Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdatePillActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializePill(long pillID) {
        mOldPill = mPillOps.getPill(pillID);
        mEditTextName.setText(mOldPill.getPillName());
        mEditTextDescription.setText(mOldPill.getPillDescription());
        mImageViewPhoto.setImageBitmap(mOldPill.getPillPhoto());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                mImageViewPhoto.setImageURI(mFile);
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
}
