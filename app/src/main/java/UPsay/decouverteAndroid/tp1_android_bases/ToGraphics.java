package UPsay.decouverteAndroid.tp1_android_bases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ToGraphics extends View {
    float xText, yText;
    float x1, x2, y1, y2;
    int sizeT=1;
    private int pencheH, pencheV, penche ;
    Handler timerHandler = new Handler();

    public ToGraphics(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setXYText (600,600);
        timerHandler.postDelayed(updateTimerThread, 10);
        setOnTouchListener(onTouchListener);
        /* accelerometre */
        Sensor accelerometre;
        SensorManager m = (SensorManager) context.getSystemService
                (Context.SENSOR_SERVICE);
        accelerometre = m.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m.registerListener(mSensorEventListener, accelerometre,
                SensorManager.SENSOR_DELAY_UI);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xText = event.getX();
        yText = event.getY();
        invalidate();
        return false;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        /*définir la couleur de l’objet de dessin */
        p.setColor(Color.BLACK);
        /*définir son style en remplissage*/
        p.setStyle(android.graphics.Paint.Style.FILL);
        /*dessiner un rectangle qui occupe la totalité du View*/
        canvas.drawRect(0,0,getWidth(),getHeight(), p);
        /*définir une autre couleur pour dessiner un texte*/
        p.setColor(Color.GREEN);
        /*définir la taille du texte*/
        p.setTextSize(sizeT);
        /*définir le centre du texte comme étant son origine*/
        p.setTextAlign(android.graphics.Paint.Align.CENTER);
        /*dessiner le texte en positionnant son origine au centre du View */
        String texte = "Bonjour MONDE";
        canvas.drawText(texte, xText, yText, p);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.soccer);
        //b = Bitmap.createBitmap(b,20,705,50,65);
        canvas.drawBitmap(b, 200,200,p);
    }
    public void setXYText (float x, float y){
        xText = x;
        yText = y;
    }
    Runnable updateTimerThread = new Runnable() {
        public void run() {
            timerHandler.postDelayed(this,100);
            sizeT= (sizeT+10)%100;
            invalidate(); // appel de onDraw pour redessiner
        }
    };

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event){
            float dx, dy;
            String direction;
            switch(event.getAction()) {
                case(MotionEvent.ACTION_DOWN):
                    x1 = event.getX();
                    y1 = event.getY();
                    Log.i("pacman", "appuyé");
                    break;
                case(MotionEvent.ACTION_UP): {
                    x2 = event.getX();
                    y2 = event.getY();
                    dx = x2-x1;
                    dy = y2-y1;
// Use dx and dy to determine the direction of the move
                    if(Math.abs(dx) > Math.abs(dy)) {
                        if(dx>0)
                            direction = "right";
                        else
                            direction = "left";
                    } else {
                        if(dy>0)
                            direction = "down";
                        else
                            direction = "up";
                    }
                    Log.i("pacman", "laché " + direction);
                    Log.i("pacman", "dx = " + dx +"; dy = " + dy);
                    break;
                }
            }
            invalidate();
            return true;
        }
    };

    final SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            pencheH = -(int)(sensorEvent.values[0]);
            pencheV = (int)(sensorEvent.values[1]);
            penche = pencheH*pencheH+pencheV*pencheV;
        }
        @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
    };


}


