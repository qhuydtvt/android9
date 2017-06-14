package techkids.vn.tidypiano;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import techkids.vn.tidypiano.noteplayers.NotePlayer;
import techkids.vn.tidypiano.touches.Touch;
import techkids.vn.tidypiano.touches.TouchAction;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.toString();
    private List<ImageView> blackKeys = new ArrayList<>();
    private List<ImageView> whiteKeys = new ArrayList<>();

    private List<TouchInfo> touchInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blackKeys.add((ImageView) findViewById(R.id.iv_black_1));
        blackKeys.add((ImageView) findViewById(R.id.iv_black_2));
        blackKeys.add((ImageView) findViewById(R.id.iv_black_3));
        blackKeys.add((ImageView) findViewById(R.id.iv_black_4));
        blackKeys.add((ImageView) findViewById(R.id.iv_black_5));

        whiteKeys.add((ImageView) findViewById(R.id.iv_white_1));
        whiteKeys.add((ImageView) findViewById(R.id.iv_white_2));
        whiteKeys.add((ImageView) findViewById(R.id.iv_white_3));
        whiteKeys.add((ImageView) findViewById(R.id.iv_white_4));
        whiteKeys.add((ImageView) findViewById(R.id.iv_white_5));
        whiteKeys.add((ImageView) findViewById(R.id.iv_white_6));
        whiteKeys.add((ImageView) findViewById(R.id.iv_white_7));

        NotePlayer.loadSounds(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        List<Touch> touches = Touch.processEvent(event);

        Log.d(TAG, String.format("onTouchEvent: %s", touches));

        if (touches.size() == 0) return false;

        Touch firstTouch = touches.get(0);

        if (firstTouch.getAction() == TouchAction.DOWN) {
            ImageView pressedKey = findKeyByTouch(firstTouch);
            if (!checkPressedKey(pressedKey)) {
                //TODO: Play note
                String note = pressedKey.getTag().toString();
                NotePlayer.play(note);

                touchInfoList.add(new TouchInfo(pressedKey, firstTouch));
            }
        }
        else if (firstTouch.getAction() == TouchAction.UP) {
            ImageView pressedKey = findKeyByTouch(firstTouch);

            Iterator<TouchInfo> touchInfoIterator = touchInfoList.iterator();
            while(touchInfoIterator.hasNext()) {
                TouchInfo touchInfo = touchInfoIterator.next();
                if (touchInfo.pressedKey == pressedKey) {
                    touchInfoIterator.remove();
                }
            }
        }
        else if (firstTouch.getAction() == TouchAction.MOVE) {
            for (Touch touch: touches) {
                ImageView pressedKey = findKeyByTouch(touch);

                Iterator<TouchInfo> touchInfoIterator = touchInfoList.iterator();
                while(touchInfoIterator.hasNext()) {
                    TouchInfo touchInfo = touchInfoIterator.next();
                    if (touchInfo.touch.equals(touch) && touchInfo.pressedKey != pressedKey) {
                        touchInfoIterator.remove();
                    }
                }

                if (!checkPressedKey(pressedKey)) {
                    //TODO: Play note
                    String note = pressedKey.getTag().toString();
                    NotePlayer.play(note);

                    touchInfoList.add(new TouchInfo(pressedKey, touch));
                }
            }
        }



//        Log.d(TAG, String.format("onTouchEvent: %s", touches));

//        for (ImageView blackKey : blackKeys) {
//            if (isInside(event.getX(), event.getY(), blackKey)) {
//                Log.d(TAG, "onTouchEvent: " + blackKey.getTag());
//            }
//        }
//
//        for (ImageView whiteKey : whiteKeys) {
//            if (isInside(event.getX(), event.getY(), whiteKey)) {
//                Log.d(TAG, "onTouchEvent: " + whiteKey.getTag());
//            }
//        }

        updateKeyImages();
        return super.onTouchEvent(event);
    }

    private void updateKeyImages() {
        for (ImageView blackKey: blackKeys) {
            if (checkPressedKey(blackKey)) {
                blackKey.setImageResource(R.drawable.pressed_black_key);
            } else {
                blackKey.setImageResource(R.drawable.default_black_key);
            }
        }

        for (ImageView whiteKey: whiteKeys) {
            if (checkPressedKey(whiteKey)) {
                whiteKey.setImageResource(R.drawable.pressed_white_key);
            } else {
                whiteKey.setImageResource(R.drawable.default_white_key);
            }
        }
    }

    private boolean checkPressedKey(ImageView pressedKey) {
        for (TouchInfo touchInfo: touchInfoList) {
            if (touchInfo.pressedKey == pressedKey) {
                return true;
            }
        }

        return false;
    }

    private ImageView findKeyByTouch(Touch touch) {
        for (ImageView blackKey: blackKeys) {
            if (touch.checkHit(blackKey)) {
                return blackKey;
            }
        }

        for (ImageView whiteKey: whiteKeys) {
            if (touch.checkHit(whiteKey)) {
                return whiteKey;
            }
        }

        return null;
    }



//    public boolean isInside (float x, float y, View v) {
//        int[] location = new int[2];
//        v.getLocationOnScreen(location);
//
//        int left = location[0];
//        int top = location[1];
//
//        int right = left + v.getWidth();
//        int bot = top + v.getHeight();
//
//        return x > left && x < right && y > top && y < bot;
//    }

    class TouchInfo {
        public ImageView pressedKey;
        public Touch touch;

        public TouchInfo(ImageView pressedKey, Touch touch) {
            this.pressedKey = pressedKey;
            this.touch = touch;
        }
    }
}
