package com.wasiollo.neverexpense.receipt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.product.domain.Product;
import com.wasiollo.neverexpense.receipt.domain.ParsedOcrResult;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.domain.ReceiptWithProducts;
import com.wasiollo.neverexpense.receipt.view_model.ReceiptViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddingReceiptActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final long MAX_IMAGE_SIZE = 1024 * 1024;

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
            takeImageAndCrop();
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
                takeImageAndCrop();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                handleCroppedImage(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error occurred during taking and cropping photo" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void handleCroppedImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            Bitmap monochromedBitmap = monochromeBitmap(bitmap);
//            Bitmap sharpenedBitmap = sharpenBitmap(monochromedBitmap);
            doHttpOCR(monochromedBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void takeImageAndCrop() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    private String doHttpOCR(Bitmap sharpenedBitmap) {
        String base64Image = convertBitmapToBase64String(sharpenedBitmap);
        return executeApi(base64Image);
    }

    private String executeApi(String base64Image) {

        final OkHttpClient client = new OkHttpClient();
        final RequestBody formBody = new FormBody.Builder()
                .add("language", "pol")
                .add("base64Image", base64Image)
                .build();
        final Request request = new Request.Builder()
                .url("https://api.ocr.space/parse/image")
                .addHeader("apikey", "447f7de7e188957")
                .post(formBody)
                .build();

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        return null;
                    }
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String apiResult) {
                super.onPostExecute(apiResult);
                if (apiResult != null) {
                    handleApiResult(apiResult);
                }
            }
        };
        asyncTask.execute();

        return "";
    }

    private void handleApiResult(String apiResult) {
        Gson gson = new Gson();
        JsonObject apiResponse = gson.fromJson(apiResult, JsonObject.class);
        String ocrResult = apiResponse.getAsJsonArray("ParsedResults").get(0).getAsJsonObject().get("ParsedText").getAsString();

        ParsedOcrResult parsedOcrResult = parseOcrResult(ocrResult);

        EditText companyNameField = findViewById(R.id.companyName);
//        Toast.makeText(this, "ocr text : " + parsedOcrResult.getCompanyName(), Toast.LENGTH_LONG).show();
//        companyNameField.setText(parsedOcrResult.getCompanyName());

        for (int i = 1; i < parsedOcrResult.getProducts().size(); ++i) {
            onAddField(parentLinearLayout);
        }
        final int childCount = parentLinearLayout.getChildCount();
        List<String> products = parsedOcrResult.getProducts();
        for (int i = 0; i < childCount - 1; ++i) { //last one is add field button
            LinearLayout v = (LinearLayout) parentLinearLayout.getChildAt(i);
            if (products.size() > i) {
                String productWithPrice = products.get(i);
                productWithPrice = productWithPrice.replaceAll(",", ".");
                String productName = productWithPrice.split("\\|")[0];
                String productPrice = products.get(i).split("\\|")[1];
                ((EditText) v.getChildAt(0)).setText(productName);
                ((EditText) v.getChildAt(1)).setText(productPrice);
            }
        }

    }

    private ParsedOcrResult parseOcrResult(String ocrResult) {
        EditText companyNameField = findViewById(R.id.companyName);

        ParsedOcrResult parsedOcrResult = new ParsedOcrResult();
        List<String> productList = new ArrayList<>();
        String[] splittedByBillLabel = ocrResult.split("Paragon");
        String companyName = splittedByBillLabel[0];
        if (companyName.equals(ocrResult)) {
            companyName = "";
        }

        String replacedString = ocrResult.replaceAll("([0-9]+,[0-9]{2})(?!\\s|$)", "|$1/");
        String[] partiallyCut = replacedString.split("/");
        if (!companyName.equals("")) {
            String productsString = splittedByBillLabel[1].split("suma|SUMA|Sprzedaż")[0];
            replacedString = productsString.replaceAll("([0-9]+,[0-9]{2})(?!\\s|$)", "|$1/");
            partiallyCut = replacedString.split("/");
        }

        companyNameField.setText(replacedString);
        Toast.makeText(this, "Ocr result: " + replacedString, Toast.LENGTH_LONG).show();

        for (String item : partiallyCut) {
            if (item.matches(".*[0-9]+,[0-9]{2}")) {
                productList.add(item);
            }
        }

        parsedOcrResult.setCompanyName(companyName);
        parsedOcrResult.setProducts(productList);
        return parsedOcrResult;
    }

    public static String convertBitmapToBase64String(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int currSize;
        int currQuality = 55;

//        do {
        bitmap.compress(Bitmap.CompressFormat.JPEG, currQuality, stream);
        currSize = stream.toByteArray().length;
        // limit quality by 5 percent every time
        currQuality -= 5;

//        } while (currSize >= MAX_IMAGE_SIZE);

        String encodedString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

        return "data:image/jpeg;base64," + encodedString;
    }

    private Bitmap monochromeBitmap(Bitmap bmpSource) {
        Bitmap bmpMonochrome = Bitmap.createBitmap(bmpSource.getWidth(), bmpSource.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bmpSource, 0, 0, paint);
        return bmpMonochrome;
    }

    private Bitmap sharpenBitmap(Bitmap originalBitmap) {
        float[] matrix_sharpen =
                {0, -1, 0,
                        -1, 5, -1,
                        0, -1, 0};
        return createBitmap_convolve(originalBitmap, matrix_sharpen);

    }

    private Bitmap createBitmap_convolve(Bitmap src, float[] coefficients) {

        Bitmap result = Bitmap.createBitmap(src.getWidth(),
                src.getHeight(), src.getConfig());

        RenderScript renderScript = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(renderScript, src);
        Allocation output = Allocation.createFromBitmap(renderScript, result);

        ScriptIntrinsicConvolve3x3 convolution = ScriptIntrinsicConvolve3x3
                .create(renderScript, Element.U8_4(renderScript));
        convolution.setInput(input);
        convolution.setCoefficients(coefficients);
        convolution.forEach(output);

        output.copyTo(result);
        renderScript.destroy();
        return result;
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
            if (itemPrize.equals("")) {
                itemPrize = "0";
            }
            Product newProduct = new Product();
            newProduct.setName(itemName);
            newProduct.setPrice(Double.valueOf(itemPrize));
            products.add(newProduct);
        }
        System.out.println(products.size());

        EditText companyNameField = findViewById(R.id.companyName);
        String companyName = companyNameField.getText().toString();
        if (companyName.isEmpty()) {
            companyName = "Brak Nazwy";
        }
        Receipt receipt = new Receipt();
        receipt.setCost(products.stream().mapToDouble(Product::getPrice).sum());
        receipt.setCompany(companyName);
        receipt.setDateTime(new Date());
        receipt.setUserId(1);
        receipt.setBalanceId(1);

        ReceiptWithProducts receiptWithProducts = new ReceiptWithProducts();
        receiptWithProducts.setProducts(products);
        receiptWithProducts.setReceipt(receipt);

        receiptViewModel.insert(receiptWithProducts);

        onBackPressed();
    }
}