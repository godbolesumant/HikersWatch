package sumant.androidapplication.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("LocationChnaged: ", String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));

                Geocoder geocoder =new Geocoder(getApplicationContext(), Locale.getDefault());
                String address = "";
                try {
                    List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (listAddress != null && listAddress.size() > 0) {

                        if(listAddress.get(0).getAddressLine(0) != null) {
                            address += listAddress.get(0).getAddressLine(0) + " ";
                        }

                        /*if(listAddress.get(0).getThoroughfare() != null) {
                            address += listAddress.get(0).getThoroughfare() + " ";
                        }
                        if(listAddress.get(0).getPostalCode() != null) {
                            address += listAddress.get(0).getPostalCode() + " ";
                        }
                        if(listAddress.get(0).getAdminArea() != null) {
                            address += listAddress.get(0).getAdminArea() + " ";
                        }

                        if(listAddress.get(0).getCountryName() != null) {
                            address += listAddress.get(0).getCountryName();
                        }*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                TextView textView2 = findViewById(R.id.textView2);

                String locationInfo = "Latitude: " + String.valueOf(location.getLatitude())
                        + "\n\nLongitude: " + String.valueOf(location.getLongitude())
                        + "\n\nAltitude: " + String.valueOf(location.getAltitude())
                        + "\n\nAccuracy: " + String.valueOf(location.getAccuracy())
                        + "\n\nAddress: " + address;
                textView2.setText("");
                textView2.setText(locationInfo);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}
