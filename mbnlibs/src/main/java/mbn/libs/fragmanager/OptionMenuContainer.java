package mbn.libs.fragmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


@SuppressLint("ViewConstructor")
class OptionMenuContainer extends FrameLayout {

    private View menuView;
    private boolean isShowing = false;

    private float density;
    protected int iconColor = Color.parseColor("#00b4ff");

    private CustomFragmentSwipeBackAnimator fragManager;

    public float getDensity() {
        return density;
    }

    public int getDpInt(float dp) {
        return (int) (dp * density);
    }


    public OptionMenuContainer(@NonNull Context context, CustomFragmentSwipeBackAnimator customFragmentSwipeBackAnimator) {
        super(context);
        fragManager = customFragmentSwipeBackAnimator;
    }

    public void show(int menuId) {
        onPrepareDialog(menuId, null, Gravity.CENTER);
        isShowing = true;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        menuView.setAlpha(0f);
        menuView.setScaleX(0.25f);
        menuView.setScaleY(0.25f);
        menuView.animate().alpha(1).scaleY(1).scaleX(1).setInterpolator(new OvershootInterpolator()).setDuration(400).start();
    }

    public void show(int menuId, String header) {
        onPrepareDialog(menuId, header, Gravity.CENTER);
        isShowing = true;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        menuView.setAlpha(0f);
        menuView.setScaleX(0.25f);
        menuView.setScaleY(0.25f);
        menuView.animate().alpha(1).scaleY(1).scaleX(1).setInterpolator(new OvershootInterpolator()).setDuration(400).start();
    }

    public void show(int menuId, String header, int gravity) {
        onPrepareDialog(menuId, header, gravity);
        isShowing = true;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        menuView.setAlpha(0f);
        menuView.setScaleX(0.25f);
        menuView.setScaleY(0.25f);
        menuView.animate().alpha(1).scaleY(1).scaleX(1).setInterpolator(new OvershootInterpolator()).setDuration(400).start();
    }

    public boolean dismiss() {
        if (isShowing) {
            isShowing = false;
            setOnClickListener(null);
            setClickable(false);
            menuView.animate().alpha(0).setDuration(400).scaleX(0.25f).scaleY(0.25f).setInterpolator(new AnticipateInterpolator()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeAllViews();
                }
            }).start();
            return true;
        }
        return false;
    }

    public void setMargins(LayoutParams layoutParams) {
        layoutParams.setMargins(getDpInt(30), getDpInt(30), getDpInt(30), getDpInt(30));
    }

    public void onPrepareDialog(int menuId, String header, int gravity) {
        density = getResources().getDisplayMetrics().density;
        RecyclerView menuRecycler = new RecyclerView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, gravity);
        setMargins(layoutParams);
        menuRecycler.setLayoutParams(layoutParams);
        addView(menuRecycler);
        menuView = menuRecycler;

        float corner = 20 * density;
        RoundRectShape roundRectShape = new RoundRectShape(new float[]{corner, corner, corner, corner, corner, corner, corner, corner}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
//        shapeDrawable.setColorFilter(Color.argb(100, 255, 255, 255), PorterDuff.Mode.SRC_IN);
//        shapeDrawable.setColorFilter(Color.argb(215, 255, 255, 255), PorterDuff.Mode.SRC_IN);
        shapeDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        menuRecycler.setBackground(shapeDrawable);
        menuRecycler.setClipToOutline(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        menuRecycler.setLayoutManager(layoutManager);
        menuRecycler.setHasFixedSize(true);

        menuRecycler.setAdapter(new Adapter(getItems(menuId), header));
        menuRecycler.addItemDecoration(new PopupDivider());
    }

    public ArrayList<MenuItem> getItems(int id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), this);
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        popupMenu.inflate(id);
        Menu menu = popupMenu.getMenu();
        popupMenu.dismiss();
        for (int i = 0; i < menu.size(); i++) {
            menuItems.add(menu.getItem(i));
        }
        return menuItems;
    }


    private class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private ArrayList<MenuItem> items;
        private String header;

        public Adapter(ArrayList<MenuItem> items, String header) {
            this.items = items;
            this.header = header;
        }

        @Override
        public int getItemViewType(int position) {
            if (header != null && position == 0) {
                return 1;
            }
            return super.getItemViewType(position);
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (i == 1) {
                return new Holder(new TextView(viewGroup.getContext()));
            }
            return new Holder(new ItemView(viewGroup.getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int i) {
            holder.onBind();
        }

        @Override
        public int getItemCount() {
            if (header != null) {
                return items.size() + 1;
            }
            return items.size();
        }


        class Holder extends RecyclerView.ViewHolder {

            ItemView view;

            private int getP() {
                if (header != null) {
                    return getAdapterPosition() - 1;
                }
                return getAdapterPosition();
            }

            public Holder(View itemView) {
                super(itemView);
                if (itemView instanceof ItemView) {
                    view = (ItemView) itemView;

                    itemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            fragManager.onMenuItemClicked(items.get(getP()).getItemId());
                        }
                    });

                } else {
                    itemView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ((TextView) itemView).setGravity(Gravity.CENTER);
                    ((TextView) itemView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                }
            }

            void onBind() {
                if (getItemViewType() == 1) {
                    ((TextView) itemView).setText(header);
                } else {
                    view.setIcon(items.get(getP()).getIcon());
                    view.setText(items.get(getP()).getTitle().toString());
                }
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
