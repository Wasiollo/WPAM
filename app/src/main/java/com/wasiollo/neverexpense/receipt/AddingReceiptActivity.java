package com.wasiollo.neverexpense.receipt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.product.domain.Product;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.domain.ReceiptWithProducts;
import com.wasiollo.neverexpense.receipt.view_model.ReceiptViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddingReceiptActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final String TESS_DATA = "/tessdata";

    private TessBaseAPI tessBaseAPI;
    private LinearLayout parentLinearLayout;
    private ReceiptViewModel receiptViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.adding_activity);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        receiptViewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        parentLinearLayout = findViewById(R.id.parent_linear_layout);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            prepareTessData();
            doOCR(photo);
        }
    }

    private void prepareTessData() {
        try {
            File dir = getExternalFilesDir(TESS_DATA);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
                }
            }
            String[] fileList = getAssets().list("");
            if (fileList != null) {
                for (String fileName : fileList) {
                    if(fileName.endsWith(".traineddata")) {
                        String pathToDataFile = dir + "/" + fileName;
                        if (!(new File(pathToDataFile)).exists()) {
                            InputStream in = getAssets().open(fileName);
                            OutputStream out = new FileOutputStream(pathToDataFile);
                            byte[] buff = new byte[1024];
                            int len;
                            while ((len = in.read(buff)) > 0) {
                                out.write(buff, 0, len);
                            }
                            in.close();
                            out.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String doOCR(final Bitmap bitmap) {
        try {
            tessBaseAPI = new TessBaseAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dataPath = getExternalFilesDir("/").getPath() + "/";
        tessBaseAPI.init(dataPath, "pol");
        tessBaseAPI.setImage(bitmap);
        String retStr = "No result";
        try {
            retStr = tessBaseAPI.getUTF8Text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tessBaseAPI.end();
        Toast.makeText(this, "ocr text : "+retStr, Toast.LENGTH_LONG).show();
        return retStr;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adding_field, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

    public void onSave(View view) {
        final int childCount = parentLinearLayout.getChildCount();
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < childCount - 1; ++i) { //last one is add field button
            LinearLayout v = (LinearLayout) parentLinearLayout.getChildAt(i);
            String itemName = ((EditText) v.getChildAt(0)).getText().toString();
            String itemPrize = ((EditText) v.getChildAt(1)).getText().toString();
            Product newProduct = new Product();
            newProduct.setName(itemName);
            newProduct.setPrice(Double.valueOf(itemPrize));
            products.add(newProduct);
        }
        System.out.println(products.size());

        EditText totalCostField = findViewById(R.id.sum);
        Double totalCost = Double.valueOf(totalCostField.getText().toString());
        Receipt receipt = new Receipt();
        receipt.setCost(totalCost);
        receipt.setCompany("TEST");
        receipt.setDateTime(new Date());
        receipt.setUserId(1);   //todo
        receipt.setBalanceId(1);    //todo


        ReceiptWithProducts receiptWithProducts = new ReceiptWithProducts();
        receiptWithProducts.setProducts(products);
        receiptWithProducts.setReceipt(receipt);

        receiptViewModel.insert(receiptWithProducts);

        /*Snackbar.make((View) view.getParent(), "Save clicked", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/

        onBackPressed();
    }
}