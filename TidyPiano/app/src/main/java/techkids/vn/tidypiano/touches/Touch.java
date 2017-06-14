package techkids.vn.tidypiano.touches;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.*;

/**
 * Created by huynq on 6/14/17.
 */

public class Touch {
    private float x;
    private float y;
    private TouchAction action;
    private int touchId;

    public Touch(float x, float y, TouchAction action, int touchId) {
        this.x = x;
        this.y = y;
        this.action = action;
        this.touchId = touchId;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public TouchAction getAction() {
        return action;
    }

    public int getTouchId() {
        return touchId;
    }

    public boolean checkHit(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        int left = location[0];
        int top = location[1];

        int right = left + v.getWidth();
        int bot = top + v.getHeight();

        return x > left && x < right && y > top && y < bot;
    }

    public static List<Touch> processEvent(MotionEvent event) {
        ArrayList<Touch> touches = new ArrayList<>();

        int maskedAction = event.getActionMasked();

        if (maskedAction == ACTION_DOWN || maskedAction == ACTION_POINTER_DOWN) {
            Touch touch = getTouch(event, event.getActionIndex(), TouchAction.DOWN);
            touches.add(touch);
        } else if (maskedAction == ACTION_UP || maskedAction == ACTION_POINTER_UP) {
            Touch touch = getTouch(event, event.getActionIndex(), TouchAction.UP);
            touches.add(touch);
        } else if (maskedAction == ACTION_MOVE) {
            for (int pointerIndex = 0; pointerIndex < event.getPointerCount(); pointerIndex++) {
                Touch touch = getTouch(event, pointerIndex, TouchAction.MOVE);
                touches.add(touch);
            }
        }

        return touches;
    }

    private static Touch getTouch(MotionEvent event, int pointerIndex, TouchAction action) {
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        int id = event.getPointerId(pointerIndex);
        return new Touch(x, y, action, id);
    }

    @Override
    public String toString() {
        return "Touch{" +
                "x=" + x +
                ", y=" + y +
                ", action=" + action +
                ", touchId=" + touchId +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Touch other = (Touch)obj;
        return other.touchId == this.touchId;
    }
}
