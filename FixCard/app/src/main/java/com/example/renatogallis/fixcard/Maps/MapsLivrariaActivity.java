package com.example.renatogallis.fixcard.Maps;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import com.example.renatogallis.fixcard.API.LocalAPI;
import android.location.LocationListener;
import com.example.renatogallis.fixcard.R;
import com.example.renatogallis.fixcard.Utills.APIUtils;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.renatogallis.fixcard.Locals.Locals;


public class MapsLivrariaActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private int ZOOM = 10;
    private static String[] PERMISSIONS_LOCATION = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private LocalAPI localAPI;


    private int PROXIMITY_RADIUS = 50000;
    private String TYPE = "library";

    private LocationListener mlocatioLocationListener;
    private GoogleApiClient mgoogleApiClient;
    private LocationManager service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_livraria);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_LOCATION,
                1
        );
        mgoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .build();


        service = (LocationManager) getSystemService(LOCATION_SERVICE);
        mlocatioLocationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        service.requestLocationUpdates(service.GPS_PROVIDER, 1000, 0, mlocatioLocationListener);

    }


    @Override
    protected void onPause() {
        super.onPause();
       // service.removeUpdates(mlocatioLocationListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mgoogleApiClient != null)
            mgoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mgoogleApiClient != null && mgoogleApiClient.isConnected()) {
            mgoogleApiClient.disconnect();
        }
        service.removeUpdates(mlocatioLocationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.clear();
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                mMap.clear();

                Criteria criteria = new Criteria();
                String provider = service.getBestProvider(criteria, false);

                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    return true;
                }


                Location location = service.getLastKnownLocation(provider);
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());


                mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(getString(R.string.marcador_posicao_inicial_map)));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM));


                // Pegando locais proximos
                localAPI = APIUtils.getLocationAPI();
                localAPI.getDadosLocal(TYPE, location.getLatitude() + "," + location.getLongitude(), PROXIMITY_RADIUS).enqueue(new Callback<Locals>() {
                    @Override
                    public void onResponse(Call<Locals> call, Response<Locals> response) {
                        try {
                            for (int i = 0; i < response.body().getResults().size(); i++) {
                                Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                                Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                                String placeName = response.body().getResults().get(i).getName();
                                LatLng latLng = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(placeName)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.livreto)));

                            }
                        } catch (Exception e) {
                            Log.e("Menssagem:", "Ocorreu um erro no acesso a api");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Locals> call, Throwable t) {

                    }

                });

                return true;

            }
        });


    }


// tentativa de pegar o telefone da API Location a partir do ID do estabelecimento - Desemvolver na V2
    public void telefone_do_lugar(String id) {
        final StringBuilder mbuilder = new StringBuilder();
        Places.GeoDataApi.getPlaceById(mgoogleApiClient, id).setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if (places.getStatus().isSuccess()) {
                    Place place = places.get(0);
                    placeToString(place);

                }


                places.release();
            }

        });


    }

    public String placeToString(Place place) {
        String jojo;
        jojo = place.getPhoneNumber().toString();
        return jojo;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

