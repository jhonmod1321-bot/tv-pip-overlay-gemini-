package com.example.tvpipoverlay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar permiso para dibujar sobre otras apps
        checkOverlayPermission();

        Button btnStartService = findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(v -> {
            if (Settings.CanDrawOverlays(this)) {
                Intent serviceIntent = new Intent(this, OverlayService.class);
                // Puedes pasar aquí el paquete de la app que deseas mostrar en miniatura
                serviceIntent.putExtra("package_name", "com.retroarch"); // Ejemplo con RetroArch
                startForegroundService(serviceIntent);
                Toast.makeText(this, "Iniciando modo flotante...", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la UI principal para dejar la app de fondo
            } else {
                checkOverlayPermission();
            }
        });
    }

    private void checkOverlayPermission() {
        if (!Settings.CanDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.CanDrawOverlays(this)) {
                Toast.makeText(this, "El permiso de superposición es necesario para Android TV.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
