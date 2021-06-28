package mbn.libs.fragmanager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BasicMbnPopupMenu extends BaseMbnDialog {

    private RecyclerView menuRecycler;

    private float density;
    protected int iconColor = Color.parseColor("#00b4ff");

    public float getDensity() {
        return density;
    }

    public int getDpInt(float dp) {
        return (int) (dp * density);
    }

    public void resetAdapter() {
        menuRecycler.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onPrepareDialog() {
        density = getResources().getDisplayMetrics().density;
        menuRecycler = new RecyclerView(getContext());
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, getGravity());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, getGravity());
//        layoutParams.setMargins((int) (24 * density), (int) (24 * density), (int) (24 * density), (int) (24 * density));
        setMargins(layoutParams);
//        menuRecycler.setPadding(0, 0, getDpInt(120), 0);
        menuRecycler.setLayoutParams(layoutParams);
        addLayout(menuRecycler);

        float corner = 20 * density;
        RoundRectShape roundRectShape = new RoundRectShape(new float[]{corner, corner, corner, corner, corner, corner, corner, corner}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
//        shapeDrawable.setColorFilter(Color.argb(100, 255, 255, 255), PorterDuff.Mode.SRC_IN);
        shapeDrawable.setColorFilter(Color.argb(215, 255, 255, 255), PorterDuff.Mode.SRC_IN);
        menuRecycler.setBackground(shapeDrawable);
        menuRecycler.setClipToOutline(true);

        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        menuRecycler.setLayoutManager(layoutManager);
        menuRecycler.setHasFixedSize(true);

        menuRecycler.setAdapter(new Adapter(getItems()));
        menuRecycler.addItemDecoration(new PopupDivider());
    }

    public void setMargins(FrameLayout.LayoutParams layoutParams) {
        layoutParams.setMargins(getDpInt(30), getDpInt(30), getDpInt(30), getDpInt(30));
    }

    public abstract int getGravity();

    public abstract PopupItem[] getItems();

    public abstract void onItemClicked(PopupItem[] items, int position);

    //------------- CLASSES -----------//

    public static class PopupItem {
        private Drawable icon;
        private String text;

        public PopupItem(Drawable icon, String text) {
            this.icon = icon;
            this.text = text;
        }

        public Drawable getIcon() {
            return icon;
        }

        public String getText() {
            return text;
        }
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private PopupItem[] items;

        public Adapter(PopupItem[] items) {
            this.items = items;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new Holder(new ItemView(viewGroup.getContext()));
        }

        @Override
        public void onBindViewHolder(Holder holder, int i) {
            holder.onBind();
        }

        @Override
        public int getItemCount() {
            return items.length;
        }


        class Holder extends RecyclerView.ViewHolder {

            ItemView view;

            public Holder(View itemView) {
                super(itemView);
                view = (ItemView) itemView;

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentSwipeBackManager().popFragment();
                        onItemClicked(items, getAdapterPosition());
                    }
                });

            }

            void onBind() {
                view.setIcon(items[getAdapterPosition()].getIcon());
                view.setText(items[getAdapterPosition()].getText());
            }

        }
    }

    class ItemView extends LinearLayout {

        private ImageView icon;
        private TextView text;

        public ItemView(Context context) {
            super(context);
            init();
        }

        private void init() {
            setOrientation(HORIZONTAL);
            setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            setPadding((int) (8 * density), (int) (8 * density), (int) (128 * density), (int) (8 * density));


            icon = new ImageView(getContext());
            icon.setLayoutParams(new LinearLayoutCompat.LayoutParams(getDpInt(25), getDpInt(25)));
            icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            icon.setImageTintMode(PorterDuff.Mode.SRC_IN);
            icon.setImageTintList(ColorStateList.valueOf(iconColor));
            addView(icon);

            text = new TextView(getContext());
            text.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            text.setTextSize(16);
            text.setPadding(getDpInt(8), 0, 0, 0);
//            text.setGravity(Gravity.CENTER);
            addView(text);


            ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
            RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(Color.GRAY), null, shapeDrawable);
            setBackground(rippleDrawable);

        }

        public void setIcon(Drawable bitmap) {
            icon.setImageDrawable(bitmap);
            if (bitmap == null) icon.setVisibility(GONE);
            else icon.setVisibility(VISIBLE);
        }

        public void setText(String text) {
            this.text.setText(text);
        }
    }

    private class PopupDivider extends RecyclerView.ItemDecoration {
        private Paint mPaint;

        public PopupDivider() {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.LTGRAY);
            mPaint.setStrokeWidth(density);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            c.save();
            for (int i = 0; i < parent.getChildCount(); i++) {

                if (!(parent.getChildAdapterPosition(parent.getChildAt(i)) == parent.getAdapter().getItemCount() - 1)) {
                    int y = parent.getChildAt(i).getBottom();
                    c.drawLine(density * 30, (float) (y + (density * 0.5)), parent.getWidth() - (density * 30), (float) (y + (density * 0.5)), mPaint);
                }
//            c.drawLine(scale * 95, (float) (y + (scale * 3)), parent.getWidth() - (scale * 35), (float) (y + (scale * 3)), mPaint);

//            c.drawRoundRect(scale * 90, y, parent.getWidth() - (scale * 30), y + (6 * scale), 20, 20, mPaint);

            }
            c.restore();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);

            outRect.set(0, 0, 0, (int) density);


        }
    }

}
