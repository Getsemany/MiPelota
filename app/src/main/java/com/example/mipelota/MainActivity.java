package com.example.mipelota;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    DrawView view;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Creamos la nueva instancia de la clase, y le enviamos como contexto
        // nuestra actividad
        drawView = new DrawView(this);
        // La definimos como lo que muestra la actividad
        setContentView(drawView);
        // Definimos que la aplicacion ocupe toda la pantalla
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public class DrawView extends View implements SensorEventListener {
        // Bitmap para la imagen de la bola
        public Bitmap ball;
        // Enteros para guardar el tamaño de la pantalla
        public int HEIGHT;
        public int WIDTH;
        // Punto de decimales para guardar la posicion de la bola
        PointF position = new PointF();
        // Variables para acceder a los datos del accelerometro
        final int X = 0;
        final int Y = 1;
        final int Z = 2;

        public DrawView(Context context) {
            super(context);

            // Obtenemos los "manager" de los sensores del movil
            SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            // Miramos a ver si hay por lo menos uno de tipo ACCELEROMETRO
            if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
                // Si es el caso, obtenemos el primero de la lista
                Sensor accelerometer = manager.getSensorList(
                        Sensor.TYPE_ACCELEROMETER).get(0);
                // Le definimos como listener "this" (nuestra clase) el sensor
                // que hemos obtenido, y le definimos una frequencia
                // SENSOR_DELAY_GAME
                if (!manager.registerListener(this, accelerometer,
                        SensorManager.SENSOR_DELAY_GAME)) {
                    // Aqui podemos mostrar un mensaje de error en caso de que
                    // no se pueda definir el listener
                }
            }

            try {
                // Obtenemos los assets de la aplicacion
                AssetManager assetManager = context.getAssets();
                InputStream inputStream;
                // De los assets, obtenemos la imagen "ball.png"
                inputStream = assetManager.open("ball.png");
                // Decodificamos del binario inputStream a un bitmap
                ball = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Obtenemos las medidas de la pantalla
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            // Guardamos el alto
            HEIGHT = displaymetrics.heightPixels;
            // Guardamos el ancho
            WIDTH = displaymetrics.widthPixels;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // Modificamos la posicion de la bola en el eje X
            position.x -= event.values[X];
            // Comprobamos si se sale de la pantalla, y en ese caso, modificamos
            // su valor
            if (position.x < 0)
                position.x = 0;
            else if (position.x > this.WIDTH - ball.getWidth())
                position.x = this.WIDTH - ball.getWidth();
            // Modificamos la posicion de la bola en el eje Y
            position.y += event.values[Y];
            // Comprobamos si se sale de la pantalla, y en ese caso, modificamos
            // su valor
            if (position.y < 0)
                position.y = 0;
            else if (position.y > this.HEIGHT - ball.getHeight())
                position.y = this.HEIGHT - ball.getHeight();
            // Llamamos a la funcion invalidate()
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(ball, position.x, position.y, null);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    }
}
