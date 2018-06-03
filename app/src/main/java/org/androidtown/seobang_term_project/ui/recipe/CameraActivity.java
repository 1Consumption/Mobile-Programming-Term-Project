
package org.androidtown.seobang_term_project.ui.recipe;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Build.VERSION_CODES.M;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn = null;
    ImageView iv = null;
    String str = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download";

    @Override
    public void onResume() {

        checkPermissionF();
        super.onResume();
    }

    private void checkPermissionF() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            // only for LOLLIPOP and newer versions
            System.out.println("Hello Marshmallow (마시멜로우)");
            int permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("단말기의 파일쓰기 권한이 필요합니다.\\n계속하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= M) {
                                        System.out.println("감사합니다. 권한을 허락했네요 (마시멜로우)");
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "권한 요청 취소", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();
                    //최초로 권한을 요청할 때.
                } else {
                    System.out.println("최초로 권한을 요청할 때. (마시멜로우)");
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    //        getThumbInfo();
                }
            } else {
                //권한이 있을 때.
                //       getThumbInfo();
            }

        } else {
            System.out.println("(마시멜로우 이하 버전입니다.)");
            //   getThumbInfo();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult WRITE_EXTERNAL_STORAGE ( 권한 성공 ) ");
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult READ_PHONE_STATE ( 권한 성공 ) ");
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult READ_EXTERNAL_STORAGE ( 권한 성공 ) ");
                    }
                }
            }
        } else {
            System.out.println("onRequestPermissionsResult ( 권한 거부) ");
            Toast.makeText(getApplicationContext(), "요청 권한 거부", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        btn = findViewById(R.id.btn1);
        iv = findViewById(R.id.iv);
        btn.setOnClickListener(this);
        File file = new File(str);
        file.mkdirs();
        try {
            file.createNewFile();
            Toast.makeText(this, "이미지 디렉토리 및 파일생성 성공~", Toast.LENGTH_LONG).show();
        } catch (IOException ie) {
            Toast.makeText(this, "이미지 디렉토리 및 파일생성 실패", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView) findViewById(R.id.iv)).setImageBitmap(imageBitmap);
            FileOutputStream out = null;
            try {
                File file = new File(str, "qwer.jpg");
                out = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
