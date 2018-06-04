
package org.androidtown.seobang_term_project.ui.recipe;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Seobang";
    File file = null;
    ImageView imageView;
    FloatingActionButton FAB;
    ImageButton shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendShare();
            }
        });

        imageView = (ImageView) findViewById(R.id.iv);
        file = new File(str);
        file.mkdirs();
        try {
            file = createFile();
            Toast.makeText(this, "이미지 디렉토리 및 파일생성 성공~", Toast.LENGTH_LONG).show();
        } catch (IOException ie) {
            Toast.makeText(this, "이미지 디렉토리 및 파일생성 실패", Toast.LENGTH_LONG).show();
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
        Long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat YMD = new SimpleDateFormat("yyyy_MM_dd(hh:mm:ss)");
        String nowDate = YMD.format(date);
        String imageFileName = "Seobang_" + nowDate + ".jpg";
        File curFile = new File(str, imageFileName);
        return curFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("ㅁㄴㅇ", "하위^^");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            if (file != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
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

        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        if (resInfo.isEmpty()) {
            return;
        }

        String external = Environment.getExternalStorageDirectory().getAbsolutePath();

        List<Intent> shareIntentList = new ArrayList<>();
        for (ResolveInfo info : resInfo) {
            Intent shareIntent = (Intent) intent.clone();

            if (info.activityInfo.packageName.toLowerCase().equals("com.facebook.katana")) {
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.google.com");
            } else {
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "하연하연"); // title
                shareIntent.putExtra(Intent.EXTRA_TEXT, "모프 이미지 보내기 테스트"); // content
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + external + "/Download/profile.jpg")); // image file path 예시. file:/// 요건 반드시 붙어야함!!
            }
            shareIntent.setPackage(info.activityInfo.packageName);
            shareIntentList.add(shareIntent);
        }

        Intent chooserIntent = Intent.createChooser(shareIntentList.remove(0), "select");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
        startActivity(chooserIntent);
    }

}

//    Button btn = null;
//    ImageView iv = null;
//
//    String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Seobang";
//
//    @Override
//    public void onResume() {
//
//        checkPermissionF();
//        super.onResume();
//    }
//
//    private void checkPermissionF() {
//        if (android.os.Build.VERSION.SDK_INT >= M) {
//            // only for LOLLIPOP and newer versions
//            System.out.println("Hello Marshmallow (마시멜로우)");
//            int permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (permissionResult == PackageManager.PERMISSION_DENIED) {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
//                    dialog.setTitle("권한이 필요합니다.")
//                            .setMessage("단말기의 파일쓰기 권한이 필요합니다.\\n계속하시겠습니까?")
//                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if (Build.VERSION.SDK_INT >= M) {
//                                        System.out.println("감사합니다. 권한을 허락했네요 (마시멜로우)");
//                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                                    }
//                                }
//                            })
//                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(getApplicationContext(), "권한 요청 취소", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .create()
//                            .show();
//                    //최초로 권한을 요청할 때.
//                } else {
//                    System.out.println("최초로 권한을 요청할 때. (마시멜로우)");
//                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    //        getThumbInfo();
//                }
//            } else {
//                //권한이 있을 때.
//                //       getThumbInfo();
//            }
//
//        } else {
//            System.out.println("(마시멜로우 이하 버전입니다.)");
//            //   getThumbInfo();
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == 1) {
//            for (int i = 0; i < permissions.length; i++) {
//                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        System.out.println("onRequestPermissionsResult WRITE_EXTERNAL_STORAGE ( 권한 성공 ) ");
//                    }
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        System.out.println("onRequestPermissionsResult READ_PHONE_STATE ( 권한 성공 ) ");
//                    }
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        System.out.println("onRequestPermissionsResult READ_EXTERNAL_STORAGE ( 권한 성공 ) ");
//                    }
//                }
//            }
//        } else {
//            System.out.println("onRequestPermissionsResult ( 권한 거부) ");
//            Toast.makeText(getApplicationContext(), "요청 권한 거부", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//        File test = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Seobang");
//        test.mkdirs();
//        btn = findViewById(R.id.btn1);
//        iv = findViewById(R.id.iv);
//        btn.setOnClickListener(this);
//        File file = new File(str);
//        file.mkdirs();
//        try {
//            file.createNewFile();
//            Toast.makeText(this, "이미지 디렉토리 및 파일생성 성공~", Toast.LENGTH_LONG).show();
//        } catch (IOException ie) {
//            Toast.makeText(this, "이미지 디렉토리 및 파일생성 실패", Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Date date = new Date(System.currentTimeMillis());
//            SimpleDateFormat YMD = new SimpleDateFormat("yyyy_mm_dd");
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            ((ImageView) findViewById(R.id.iv)).setImageBitmap(imageBitmap);
//            FileOutputStream out = null;
//            try {
//                File file = new File(str, "seobang_" + YMD.format(date) + ".jpg");
//                out = new FileOutputStream(file);
//                imageBitmap.compress(Bitmap.CompressFormat.PNG, 1000, out);
//                MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
//                getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (out != null) {
//                        out.close();
//
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
