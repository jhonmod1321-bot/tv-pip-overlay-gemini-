package com.example.tvpipoverlay;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class OverlayService extends Service {

    private WindowManager windowManager;
    private View floatingView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (floatingView == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            // Inflar el diseño de la ventana flotante en miniatura
            LayoutInflater inflater = LayoutInflater.from(this);
            floatingView = inflater.inflate(R.layout.overlay_layout, null);

            // Configuración de los parámetros de la ventana flotante para Android
            int layoutParamsType;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                layoutParamsType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParamsType = WindowManager.LayoutParams.TYPE_PHONE;
            }

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    400, // Ancho de la miniatura en píxeles
                    300, // Alto de la miniatura en píxeles
                    layoutParamsType,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // Permite interactuar con la app de atrás si se desea
                    PixelFormat.TRANSLUCENT
            );

            // Posicionar en la esquina superior derecha
            params.gravity = Gravity.TOP | Gravity.END;
            params.x = 50;
            params.y = 50;

            windowManager.addView(floatingView, params);

            // Botón para cerrar la ventana flotante desde el propio layout flotante
            floatingView.findViewById(R.id.btnCloseOverlay).setOnClickListener(v -> stopSelf());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null && windowManager != null) {
            windowManager.removeView(floatingView);
            floatingView = null;
        }
    }
}
