package my.edu.unikl.icdsystemfirebase.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mirolhusni95 on 08-Oct-17.
 */

public class statusView extends View {


    public statusView(Context context) {
        super(context);

        init(null);
    }

    public statusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public statusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public statusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){


    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLUE);
    }
}
