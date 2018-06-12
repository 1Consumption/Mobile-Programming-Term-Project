
package org.androidtown.seobang_term_project.ui.recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * @When:
 * In the pagefragment, when the user presses the camera button, it is executed.
 *
 * @Function:
 *  If the permission is set, the picture is taken and the picture is saved in the picture.
 * You can share your saved photos on Twitter, KakaoTalk, Instagram, Facebook, and Lines.
 * Create a file and save the photo.
 * @Technique:
 *  Save the picture using the date and time the picture was taken.
 *   Create a file in the main path of each cell phone.
 *    It is shot through ACTION_IMAGE_CAPTURE.
 *
 */

public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Seobang";
    File file = null;
    ImageView imageView;
    ImageButton shareBtn;
    Long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat YMD = new SimpleDateFormat("yyyy_MM_dd(hh:mm:ss)");
    String nowDate = YMD.format(date);
    String imageFileName = "Seobang_" + nowDate + ".jpg";
    String[] shareList = {"com.kakao.talk", "jp.naver.line.android", "com.facebook.katana", "com.instagram.android", "com.twitter.android"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendShare();
            }
        });

        imageView = (ImageView) findViewById(R.id.iv);
        file = new File(str);
        file.mkdirs();
        try {
            file = createFile();
        } catch (IOException ie) {
            Toast.makeText(this, "이미지 디렉토리 및 파일생성 실패", Toast.LENGTH_LONG).show();
            finish();
        }

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (android.os.Build.VERSION.SDK_INT >= M)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), "org.androidtown.seobang_term_project", file));
        else
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }

        findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }


    private File createFile() throws IOException {
        File curFile = new File(str, imageFileName);
        return curFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            if (file != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(file.getAbsolutePath());
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int exifOrientation;
                int exifDegree;

                if (exif != null) {
                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegrees(exifOrientation);
                } else {
                    exifDegree = 0;
                }
                imageView.setImageBitmap(rotate(bitmap, exifDegree));
            } else {
                Toast.makeText(getApplicationContext(), "File is null.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private void sendShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        List<Intent> shareIntentList = new ArrayList<>();
        for (int i = 0; i < shareList.length; i++) {
            Intent shareIntent = (Intent) intent.clone();
            if (android.os.Build.VERSION.SDK_INT >= M) {
                shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, "org.androidtown.seobang_term_project", file));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + str + "/" + imageFileName)); // image file path 예시. file:/// 요건 반드시 붙어야함!!
            }

            shareIntent.setPackage(shareList[i]);
            shareIntentList.add(shareIntent);
        }

        Intent chooserIntent = Intent.createChooser(shareIntentList.remove(0), "공유");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
        startActivity(chooserIntent);
    }

}
