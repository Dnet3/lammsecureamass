package org.lammsecure.lammsecureamass.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseAuthenticationActivity;
import org.lammsecure.lammsecureamass.models.LAMMImageCaptureObject;
import org.lammsecure.lammsecureamass.utilities.ApplicationUtilities;

/**
 * Created by Max on 10/3/17.
 *
 * A simple {@link BaseAuthenticationActivity} subclass.
 * Use the {@link ImageCaptureActivity#start}  method to
 * start this Activity.
 */
public class ImageCaptureActivity extends BaseAuthenticationActivity implements OnMapReadyCallback {

    private static final String ARG_IMAGE_CAPTURE_OBJECT = "image_capture_object";

    private LAMMImageCaptureObject mImageCaptureObject;

    /**
     * @param imageCaptureObject the image capture to display.
     * @return A new instance of activity ImageCaptureActivity.
     */
    public static void start(Context context, LAMMImageCaptureObject imageCaptureObject) {
        Intent starter = new Intent(context, ImageCaptureActivity.class);
        Bundle args = new Bundle();
        args.putParcelable(ARG_IMAGE_CAPTURE_OBJECT, imageCaptureObject);
        starter.putExtras(args);
        context.startActivity(starter);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_image_capture_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mImageCaptureObject = extras.getParcelable(ARG_IMAGE_CAPTURE_OBJECT);
        }

        ImageView imageCaptureImageView = (ImageView) findViewById(R.id.imageview_image_capture);

        if (mImageCaptureObject != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
            mapFragment.getMapAsync(this);

            Glide.with(this)
                    .load(mImageCaptureObject.getImageStorageAddress())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.e(ImageCaptureActivity.class.getSimpleName(), "onCreate() Glide: Exception when loading image: " + e.toString());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            findViewById(R.id.progress_bar_activity_image_capture).setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageCaptureImageView);

            getSupportActionBar().setTitle(ApplicationUtilities.convertTimeStampToDate(mImageCaptureObject.getTimestamp()));
        }
        else {
            Log.e(ImageCaptureActivity.class.getSimpleName(), "onCreate() mImageCaptureObject was null.");
        }
    }

    /**
     * Google Map listener method
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(Double.parseDouble(mImageCaptureObject.getLatitude()), Double.parseDouble(mImageCaptureObject.getLongitude()));

        String coOrds = mImageCaptureObject.getLatitude() + ", " + mImageCaptureObject.getLongitude();

        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(coOrds));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14.0f) );
    }
}
