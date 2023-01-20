package com.example.smartparkingone;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.smartparkingone.databinding.FragmentMapsBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MapsFragment extends Fragment {
    FragmentMapsBinding binding;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    FirebaseDatabase database;
    FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment mapFragment = null;
    double lat;
    double lon;
    private Marker userLocationMarker;

    public MapsFragment() {
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            View mapView = mapFragment.getView();

            placeMarkers();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            moveCompassButton(mapView);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    checkGPS();
                    return true;
                }
            });


        }

        private void placeMarkers() {
            LatLng latLng = new LatLng(27.689666, 85.313600);
            MarkerOptions markerOption = new MarkerOptions().position(latLng);
            //.title("You are Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            MarkerOptions optionOne = new MarkerOptions().position(new LatLng(27.699374, 85.312663)).title("Civil Mall");
            MarkerOptions optionTwo = new MarkerOptions().position(new LatLng(27.7016987, 85.3107309)).title("People's Plaza");
            MarkerOptions optionThree = new MarkerOptions().position(new LatLng(27.71532, 85.31463)).title("Bishal Bazar");
            MarkerOptions optionFour = new MarkerOptions().position(new LatLng(27.656007069163856, 85.32059301202294)).title("NCE");

            mMap.addMarker(optionOne);
            mMap.addMarker(optionTwo);
            mMap.addMarker(optionThree);
            mMap.addMarker(optionFour);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
    };

    private void checkGPS() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true);

        Task<LocationSettingsResponse> locationSettingsRequestTask = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());

        locationSettingsRequestTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    getCurrentLocation();

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            ResolvableApiException resolvableApiException = new ResolvableApiException(e.getStatus());
//                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(getActivity(), 101);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        Toast.makeText(getActivity(), "Setting not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        getActivity().setTitle("City");
        binding.tableCardView.setVisibility(View.GONE);
        database = FirebaseDatabase.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        managePermissions();
        getVacantSpaceNumber();
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
//-----------------------OnClicksStart-----------------------------------------------------------------------------------------------------
        binding.nce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = new LatLng(27.656007069163856, 85.32059301202294);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                delayHidingTable();
            }
        });
        binding.bishalBazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = new LatLng(27.71532, 85.31463);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                delayHidingTable();
            }
        });
        binding.civilMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = new LatLng(27.699374, 85.312663);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                delayHidingTable();
            }
        });
        binding.peoplesPlaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = new LatLng(27.7016987, 85.3107309);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                delayHidingTable();
            }
        });
        binding.floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tableCardView.getVisibility() == View.VISIBLE) {
                    binding.tableCardView.setVisibility(View.GONE);
                } else {
                    binding.tableCardView.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.nce.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getActivity(), ParkingSlotsActivity.class);
                startActivity(intent);
                return false;
            }
        });
//-------------------------OnClicksEnd----------------------------------------------------------------------------------------------------
        return binding.getRoot();
    }


    public void moveCompassButton(View mapView) {
        try {
            assert mapView != null; // skip this if the mapView has not been set yet

            // View view = mapView.findViewWithTag("GoogleMapCompass");
            View view = mapView.findViewWithTag("GoogleMapMyLocationButton");

            // move the compass button to the right side, centered
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 60);

            view.setLayoutParams(layoutParams);
        } catch (Exception ex) {
            Log.e(TAG, "moveCompassButton() - failed: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    private void managePermissions() {
        Dexter.withContext(getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (userLocationMarker != null) {
                    userLocationMarker.remove();
                }
                Location location = task.getResult();
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    LatLng latLng = new LatLng(lat, lon);
                    userLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("You are Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    updateDistance(lat, lon);
                } else {
                    LocationRequest locationRequest = new LocationRequest()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(2000)
                            .setFastestInterval(1000)
                            .setNumUpdates(1);

                    LocationCallback callback = new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            Location location1 = locationResult.getLastLocation();
                            if (location1 != null) {
                                lat = location1.getLatitude();
                                lon = location1.getLongitude();
                                LatLng latLng = new LatLng(lat, lon);
                                userLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("You are Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                updateDistance(lat, lon);
                            } else {
                                Toast.makeText(getContext(), "null location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, Looper.myLooper());
                }
            }
        });
    }

    private void updateDistance(double lat, double lon) {
        LatLng userLocation = new LatLng(lat, lon);
        LatLng civilMall = new LatLng(27.699374, 85.312663);
        LatLng peoplesPlaza = new LatLng(27.7016987, 85.3107309);
        LatLng bishalBazar = new LatLng(27.71532, 85.31463);
        LatLng nce = new LatLng(27.656007069163856, 85.32059301202294);

        String distanceCivilMall = String.format("%.2f", SphericalUtil.computeDistanceBetween(userLocation, civilMall) / 1000);
        String distancePeoplesPlaza = String.format("%.2f", SphericalUtil.computeDistanceBetween(userLocation, peoplesPlaza) / 1000);
        String distanceBishalBazar = String.format("%.2f", SphericalUtil.computeDistanceBetween(userLocation, bishalBazar) / 1000);
        String distanceNce = String.format("%.2f", SphericalUtil.computeDistanceBetween(userLocation, nce) / 1000);

        binding.civilMallDistance.setText(distanceCivilMall);
        binding.peoplesPlazaDistance.setText(distancePeoplesPlaza);
        binding.bishalBazarDistance.setText(distanceBishalBazar);
        binding.nceDistance.setText(distanceNce);
    }


    private void delayHidingTable() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.tableCardView.setVisibility(View.GONE);
            }
        }, 300);
    }

    private void getVacantSpaceNumber() {
        database.getReference().child("Slots").child("NceVacant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nceVacant = snapshot.getValue(String.class);
                binding.nceVacant.setText(nceVacant);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}