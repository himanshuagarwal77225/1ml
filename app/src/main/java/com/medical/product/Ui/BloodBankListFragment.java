package com.medical.product.Ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.PlaceAutocompleteAdapter;
import com.medical.product.helpingFile.ApiUtils;

import com.medical.product.helpingFile.GPSTracker;
import com.medical.product.models.BloodBank;
import com.medical.product.models.BloodBankChild;
import com.medical.product.models.Slot;
import com.medical.product.rest.ApiClient;
import com.medical.product.rest.ApiInterface;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodBankListFragment  extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BloodBankListFragment";
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RecyclerView recyclerView;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    ProgressDialog dialog;
    ImageView curL;
    Geocoder geocoder;
    List<Marker> markerList = new ArrayList<Marker>();
    Float rangee=20f;
    GeoPoint geoPoint;
    Double mUserLocationLat,mUserLocationLong;
    TextView name,address,government,phone;
    EditText range;
    GPSTracker gpsTracker;
    Dialog dialog1;
    private AutoCompleteTextView mSearchText;
    RelativeLayout  mMapContainer;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private LatLngBounds mBoundary;
    private String mRange="2";
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    List<Address> addresses;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<BloodBankChild> bloodBankChildren = new ArrayList<>();
    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int MAP_LAYOUT_STATE_EXPANDED = 1;
    Marker marker;
    Button submit;
    LocationManager manager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.blood_bank_activity);
        mMapView = findViewById(R.id.user_list_map);
        range=findViewById(R.id.range);
        range.setText(mRange);
        gpsTracker = new GPSTracker(this);
        submit=findViewById(R.id.submit);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mSearchText=findViewById(R.id.input_search);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
       curL=findViewById(R.id.ic_magnify);
       curL.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                   Utlity.displayLocationSettingsRequest(BloodBankListFragment.this);
               } else {
                   mSearchText.setText(getAddressLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
               }
           }
       });
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, Fetching response");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             if(mSearchText.getText().toString()!=null && range.getText().toString()!=null){
                 dialog.setMessage("Please wait, Fetching response");
                 dialog.setCanceledOnTouchOutside(false);
                 dialog.show();
                 rangee= Float.valueOf(range.getText().toString().trim());
                createMarkerUser(getLocationFromAddress(mSearchText.getText().toString()).getLatitude(),getLocationFromAddress(mSearchText.getText().toString()).getLongitude(),mSearchText.getText().toString().trim());
                 getData(Double.toString(getLocationFromAddress(mSearchText.getText().toString()).getLatitude())
                         ,Double.toString(getLocationFromAddress(mSearchText.getText().toString()).getLongitude()),range.getText().toString().trim());
             }
            }
        });
        initUserListRecyclerView();
        initGoogleMap(savedInstanceState);
        getLastKnowLocation();

    }
    private void setCameraView(){
        double bottom = mUserLocationLat-.1;
        double left = mUserLocationLong-.1;
        double top = mUserLocationLat+.1;
        double right = mUserLocationLong+.1;

        mBoundary=new LatLngBounds(new LatLng(bottom,left),new LatLng(top,right));

    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBoundary,0));

    }
    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    private void initUserListRecyclerView() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

       placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(placeAutocompleteAdapter);
        hideSoftKeyboard();

//        mUserRecyclerAdapter = new UserRecyclerAdapter(mUserList);
//        mUserListRecyclerView.setAdapter(mUserRecyclerAdapter);
//        mUserListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(BloodBankListFragment.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap=map;

    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    private void getData(String latitude,String longitude,String range){
        Log.d(TAG, "getData: latitude  : " +latitude);
        Log.d(TAG, "getData: longitude : "+longitude);
        if(latitude==null|| longitude==null){
            showAlert("Please try again!");
        }
        ApiClient.changeConvertorToGSON(ApiUtils.BASE_URL_BLOOD);
        ApiInterface apiService = ApiClient.createService(ApiInterface.class, BloodBankListFragment.this);
        //OfferServiceApi offerServiceApi = ApiUtils.getOfferServiceBlood();
        apiService.getNearbyBloodBank(latitude, longitude, range)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BloodBank>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(BloodBank bloodBank) {
                        Log.d(TAG, "onNext: "+bloodBank.getStatus());
                        if(bloodBank.getStatus()){
                            bloodBankChildren = bloodBank.getBloodBanks();
                            Log.d(TAG, "onNext: data:" + bloodBank.getBloodBanks().get(0).getBloodBankName());
                        }else{
                            showAlert("No Blood Bank found in this range. Please modify your range");
                        }
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "onError: "+e.getStackTrace());
                        showAlert(e.getMessage().trim());
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onComplete() {
                        displayData(bloodBankChildren,getLocationFromAddress(mSearchText.getText().toString()).getLatitude(),getLocationFromAddress(mSearchText.getText().toString()).getLongitude());


                    }
                });

    }
    private void getLastKnowLocation() {

        Log.d(TAG, "getLastKnowLocation: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    try {
                        Location location = task.getResult();
                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        Log.d(TAG, "onComplete: latitude :" + geoPoint.getLatitude());
                        Log.d(TAG, "onComplete: longitude :" + geoPoint.getLongitude());
                        mUserLocationLat = geoPoint.getLatitude();
                        mUserLocationLong = geoPoint.getLongitude();
                        setText(getAddressLocation(geoPoint.getLatitude(), geoPoint.getLongitude()));
                        setCameraView();
                        getData(Double.toString(geoPoint.getLatitude()), Double.toString(geoPoint.getLongitude()), "2");
                    }catch (Exception e){
                        Log.d(TAG, "onerre: "+e.getMessage());
                        showAlert("failed to retrive data. Please try again checking internet and GPS");
                    }
                }
            }
        });
    }
    private void showAlert(String msg){
        try {
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setTitle("Alert")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getAddressLocation(double latitude, double longitude) {
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "", lat = "", logt = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            for (int i = 0; i < addresses.size(); i++) {
                address = addresses.get(i).getAddressLine(i); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(i).getLocality();
                String state = addresses.get(i).getAdminArea();
                String country = addresses.get(i).getCountryName();
                String postalCode = addresses.get(i).getPostalCode();
                String knownName = addresses.get(i).getFeatureName();
                lat = Double.toString(latitude);
                logt = Double.toString(longitude);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void setText(String address){
     mSearchText.setText(address.trim());


    }

    protected Marker createMarkerUser(double latitude, double longitude, String snippet) {

        return mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.1f, 0.1f)
                .title("Your Position")
                .snippet(snippet));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((double) (location.getLatitude()),
                    (double) (location.getLongitude()));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    private void displayData(final List<BloodBankChild> venueList,Double lat,Double longt) {
        Marker marker;

        // Removes all markers, overlays, and polylines from the map.
        mGoogleMap.clear();
        markerList.clear();
        mUserLocationLat=lat;
        mUserLocationLong=longt;
        // Zoom in, animating the camera.
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10f), 2000, null);

        // Add marker of user's position
        MarkerOptions userIndicator = new MarkerOptions()
                .position(new LatLng(mUserLocationLat, mUserLocationLong))
                .title("You are here")
                .snippet(mSearchText.getText().toString().trim());
        marker = mGoogleMap.addMarker(userIndicator);
//        Log.e(TAG, "Marker id '" + marker.getId() + "' added to list.");
        markerList.add(marker);

        // Add marker of venue if there is any
        if(venueList != null) {
            for (BloodBankChild venue : venueList) {
                String guys = venue.getBloodBankName();
                String girls = venue.getAddress();
                String checkinStatus = venue.getDistance();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(venue.getLatitude()), Double.parseDouble(venue.getLongitude())))
                        .title(venue.getBloodBankName())
                        .snippet(girls)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                marker = mGoogleMap.addMarker(markerOptions);
//                Log.e(TAG, "Marker id '" + marker.getId() + "' added to list.");
                markerList.add(marker);
            }
        }

        // Move the camera instantly to where lat and lng shows.
        if(mUserLocationLat != 0  && mUserLocationLong != 0)
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mUserLocationLat, mUserLocationLong), 10f));

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int markerId = -1;

                String str = marker.getId();
                Log.i(TAG, "Marker id: " + str);
                for(int i=0; i<markerList.size(); i++) {
                    markerId = i;
                    Marker m = markerList.get(i);
                    if(m.getId().equals(marker.getId()))
                        break;
                }

                markerId -= 1; // Because first item of markerList is user's marker
                Log.i(TAG, "Marker id " + markerId + " clicked.");

                // Ignore if User's marker clicked
                if(markerId < 0)
                    return;

                try {
                    BloodBankChild venue = venueList.get(markerId);
                    createInfo(venueList.get(markerId).getBloodBankName(),venueList.get(markerId).getAddress(),venueList.get(markerId).getMobile(),venueList.get(markerId).getCategory());
                    Log.d(TAG, "onInfoWindowClick: "+venue.getBloodBankName());

                } catch(NumberFormatException e) {
                    e.printStackTrace();
                } catch(IndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        Toast.makeText(this, "zoom out to see more results", Toast.LENGTH_SHORT).show();
    }
    public void createInfo(String namee,String adress, String phonee, String goveernment){
        if(dialog1!=null){
            if(dialog1.isShowing()) {
                dialog1.dismiss();

            }
            dialog1 = null;
        }
        dialog1 = new Dialog(BloodBankListFragment.this);
        dialog1.setContentView(R.layout.dialog_bloodbank);
       name=dialog1.findViewById(R.id.name);
        address =dialog1.findViewById(R.id.address);
        phone =dialog1.findViewById(R.id.phone);
        government=dialog1.findViewById(R.id.governement);
        name.append(namee);
        address.append(adress);
        phone.append(phonee);
        government.append(goveernment);
        dialog1.show();
    }

}