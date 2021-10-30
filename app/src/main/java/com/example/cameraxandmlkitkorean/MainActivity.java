package com.example.cameraxandmlkitkorean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;




import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "MainActivity";
    private ImageView mImageView;
    private Button FindButton;
    private Button toCameraButton;       // camera UI 화면으로 넘어가게 전화하는 button

    public TextView mTextResult;       // 인식한 OCR에 대한 텍스트가 담기는 TextView
    private Bitmap mSelectedImage;
    //private GraphicOverlay mGraphicOverlay;
    // Max width (portrait mode)
    private Integer mImageMaxWidth;
    // Max height (portrait mode)
    private Integer mImageMaxHeight;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Number of results to show in the UI.
     */
    private static final int RESULTS_TO_SHOW = 3;
    /**
     * Dimensions of inputs.
     */
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;
    private static final int DIM_IMG_SIZE_X = 224;
    private static final int DIM_IMG_SIZE_Y = 224;

    private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float>
                                o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });
    /* Preallocated buffers for storing image data. */
    private final int[] intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image_view);
        FindButton = (Button) findViewById(R.id.find_button);
        // Text Result 값이 나오는 곳

/////////////////////
        toCameraButton = findViewById(R.id.image);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
/////////////////////////////////////

        // 이하 MLKit Korean OCR TextResult
        mTextResult = (TextView) findViewById(R.id.textResult);
        mTextResult.setMovementMethod(new ScrollingMovementMethod());

        FindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //runTextRecognition();


                InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
                // When using Korean script library
                TextRecognizer recognizer =
                        TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
                FindButton.setEnabled(false);
                recognizer.process(image)
                        .addOnSuccessListener(
                        new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text texts) {
                                FindButton.setEnabled(true);
                                // Replace with code from the codelab to run text recognition.
                                InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
                                // When using Korean script library
                                TextRecognizer recognizer =
                                        TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
                                FindButton.setEnabled(false);
                                recognizer.process(image)
                                        .addOnSuccessListener(
                                        new OnSuccessListener<Text>() {
                                            @Override
                                            public void onSuccess(Text texts) {
                                                FindButton.setEnabled(true);
                                                List<Text.TextBlock> blocks = texts.getTextBlocks();
                                                if (blocks.size() == 0) {
                                                    showToast("No text found");
                                                    return;
                                                }


                                                mTextResult.setText(""); // textView clear
                                                for (int i = 0; i < blocks.size(); i++) {
                                                    List<Text.Line> lines = blocks.get(i).getLines();
                                                    for (int j = 0; j < lines.size(); j++) {
                                                        List<Text.Element> elements = lines.get(j).getElements();
                                                        for (int k = 0; k < elements.size(); k++) {
//                    GraphicOverlay.Graphic textGraphic = new TextGraphic(mGraphicOverlay, elements.get(k));
//                    mGraphicOverlay.add(textGraphic);
                                                            mTextResult.append(elements.get(k).getText() + "\n");
//                    Log.e(elements.get(k).toString());

                                                        }
                                                    }
                                                }


                                            }
                                        })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Task failed with an exception
                                                        FindButton.setEnabled(true);
                                                        e.printStackTrace();
                                                    }
                                                });


                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                FindButton.setEnabled(true);
                                e.printStackTrace();
                            }
                        });
                String temp = mTextResult.getText().toString();
                Intent intent = new Intent(MainActivity.this, TestResultActivity.class);
                intent.putExtra("key", temp);
                startActivity(intent);

            }
        });


        mTextResult.setVisibility(View.INVISIBLE);

        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Test Image 1 (Text)", "Test Image 2 (Face)", "Test Image3", "Test Image4", "Test Image5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);




    }

////////////////////////
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }
////////////////////////////////

    
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Functions for loading images from app assets.

    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxWidth() {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxWidth = mImageView.getWidth();
        }

        return mImageMaxWidth;
    }

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxHeight() {
        if (mImageMaxHeight == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxHeight =
                    mImageView.getHeight();
        }

        return mImageMaxHeight;
    }

    // Gets the targeted width / height.
    private Pair<Integer, Integer> getTargetedWidthHeight() {
        int targetWidth;
        int targetHeight;
        int maxWidthForPortraitMode = getImageMaxWidth();
        int maxHeightForPortraitMode = getImageMaxHeight();
        targetWidth = maxWidthForPortraitMode;
        targetHeight = maxHeightForPortraitMode;
        return new Pair<>(targetWidth, targetHeight);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                mSelectedImage = getBitmapFromAsset(this, "Please_walk_on_the_grass.jpg");
                break;
            case 1:
                // Whatever you want to happen when the thrid item gets selected
                mSelectedImage = getBitmapFromAsset(this, "grace_hopper.jpg");
                break;
            case 2:
                mSelectedImage = getBitmapFromAsset(this, "test.jpg");
                break;
            case 3:
                mSelectedImage = getBitmapFromAsset(this, "test2.jpg");
                break;
            case 4:
                mSelectedImage = getBitmapFromAsset(this, "test3.jpg");
                break;
        }
        if (mSelectedImage != null) {
            // Get the dimensions of the View
            Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();

            int targetWidth = targetedSize.first;
            int maxHeight = targetedSize.second;

            // Determine how much to scale down the image
            float scaleFactor =
                    Math.max(
                            (float) mSelectedImage.getWidth() / (float) targetWidth,
                            (float) mSelectedImage.getHeight() / (float) maxHeight);

            Bitmap resizedBitmap =
                    Bitmap.createScaledBitmap(
                            mSelectedImage,
                            (int) (mSelectedImage.getWidth() / scaleFactor),
                            (int) (mSelectedImage.getHeight() / scaleFactor),
                            true);

            mImageView.setImageBitmap(resizedBitmap);
            mSelectedImage = resizedBitmap;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream is;
        Bitmap bitmap = null;
        try {
            is = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
