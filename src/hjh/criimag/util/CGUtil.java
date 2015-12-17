package hjh.criimag.util;

import hjh.criimag.R;
import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 圆角跟选择器的替代方法类
 */
@SuppressLint("NewApi")
public class CGUtil {

	public static void setGroundFromView(View view, AttributeSet attrs) {
		TypedArray a = view.getContext().obtainStyledAttributes(attrs,
				R.styleable.criView);
		Drawable background = null;

		StateListDrawable drawable = new StateListDrawable();
		final int N = a.getIndexCount();
		// String gradientcolorsInt = null, cornersradiusInt = null;
		// 渐变的颜色、圆角的颜色、圆角的半径
		String gradColor = null, radiusColor = null, radiusInt = null, strokeColor = null;
		String[] split = null;
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {

			case R.styleable.criView_groundnormal:
				GradientDrawable grade = null;
				// 渐变色
				gradColor = a
						.getString(R.styleable.criView_normalgradientcolors);
				if (gradColor != null) {
					split = gradColor.split("-");
					int colors[] = new int[split.length];
					for (int j = 0; j < split.length; j++) {
						colors[j] = Color.parseColor(split[j]);
					}
					grade = new GradientDrawable(
							GradientDrawable.Orientation.TOP_BOTTOM, colors);
				}
				// 圆角
				radiusInt = a.getString(R.styleable.criView_cornersradius);
				if (radiusInt != null) {
					// 是不是有渐变
					boolean isGrad = true;
					if (grade == null) {
						grade = new GradientDrawable();
						isGrad = false;
					}
					split = radiusInt.split("-");
					// 当值设置radius，没有设置4个值
					if (split.length == 1) {
						grade.setCornerRadius(Float.valueOf(split[0]));
					} else {
						int v = 0;
						float[] radiusF = new float[split.length * 2];
						for (int j = 0; j < split.length; j++, v++) {
							radiusF[v] = Float.valueOf(split[j]);
							radiusF[++v] = Float.valueOf(split[j]);
						}
						grade.setCornerRadii(radiusF);
					}
					// 没有渐变
					if (!isGrad) {
						radiusColor = a
								.getString(R.styleable.criView_normalcornersradiuscolor);
						if (radiusColor != null)
							grade.setColor(Color.parseColor(radiusColor));
					}
					strokeColor = a.getString(R.styleable.criView_strokecolors);
					if (strokeColor != null)
						grade.setStroke(2, Color.parseColor(strokeColor));
				}

				// 代表渐变跟圆角都没有（弄图片的情况下是不能用渐变跟圆角的）
				// 要么弄背景图片，此时不能设置圆角或者渐变，只要一设置，那么背景图片就没有
				// 当设置了圆角或者渐变的时候，那么就把groundpress设置为颜色值，勿用图片
				if (grade != null) {
					drawable.addState(new int[] {
							-android.R.attr.state_focused,
							-android.R.attr.state_selected,
							-android.R.attr.state_pressed }, grade);
					drawable.addState(new int[] {
							-android.R.attr.state_focused,
							android.R.attr.state_selected,
							-android.R.attr.state_pressed }, grade);
				} else {
					Drawable drawable2 = a.getDrawable(attr);
					drawable.addState(new int[] {
							-android.R.attr.state_focused,
							-android.R.attr.state_selected,
							-android.R.attr.state_pressed }, drawable2);
					drawable.addState(new int[] {
							-android.R.attr.state_focused,
							android.R.attr.state_selected,
							-android.R.attr.state_pressed }, drawable2);
				}

				break;
			case R.styleable.criView_groundpress:

				GradientDrawable gradepress = null;
				// 渐变色
				gradColor = a
						.getString(R.styleable.criView_pressgradientcolors);
				if (gradColor != null) {
					split = gradColor.split("-");
					int colors[] = new int[split.length];
					for (int j = 0; j < split.length; j++) {
						colors[j] = Color.parseColor(split[j]);
					}
					gradepress = new GradientDrawable(
							GradientDrawable.Orientation.TOP_BOTTOM, colors);
				}
				// 圆角
				radiusInt = a.getString(R.styleable.criView_cornersradius);
				if (radiusInt != null) {
					// 是不是有渐变
					boolean isGrad = true;
					if (gradepress == null) {
						gradepress = new GradientDrawable();
						isGrad = false;
					}
					split = radiusInt.split("-");
					// 当值设置radius，没有设置4个值
					if (split.length == 1) {
						gradepress.setCornerRadius(Float.valueOf(split[0]));
					} else {
						int v = 0;
						float[] radiusF = new float[split.length * 2];
						for (int j = 0; j < split.length; j++, v++) {
							radiusF[v] = Float.valueOf(split[j]);
							radiusF[++v] = Float.valueOf(split[j]);
						}
						gradepress.setCornerRadii(radiusF);
					}
					// 没有渐变
					if (!isGrad) {
						radiusColor = a
								.getString(R.styleable.criView_presscornersradiuscolor);
						if (radiusColor != null)
							gradepress.setColor(Color.parseColor(radiusColor));
					}
				}
				if (gradepress != null) {
					drawable.addState(
							new int[] { android.R.attr.state_pressed },
							gradepress);
				} else {
					Drawable drawable2 = a.getDrawable(attr);
					drawable.addState(
							new int[] { android.R.attr.state_pressed },
							drawable2);
				}

				break;
			case R.styleable.criView_groundforce:
				background = a.getDrawable(attr);
				drawable.addState(new int[] { android.R.attr.state_focused },
						background);
				break;
			case R.styleable.criView_groundselect:
				drawable.addState(new int[] { android.R.attr.state_selected },
						background);
				background = a.getDrawable(attr);
				break;
			}
		}

		if (android.os.Build.VERSION.SDK_INT >= 16) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}

	public static void setCriFromView(View view, AttributeSet attrs) {
		TypedArray a = view.getContext().obtainStyledAttributes(attrs,
				R.styleable.criView);
		Drawable background = null;
		StateListDrawable drawable = new StateListDrawable();
		final int N = a.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.criView_groundnormal:
				background = a.getDrawable(attr);
				drawable.addState(new int[] { -android.R.attr.state_focused,
						-android.R.attr.state_selected,
						-android.R.attr.state_pressed }, background);
				drawable.addState(new int[] { -android.R.attr.state_focused,
						android.R.attr.state_selected,
						-android.R.attr.state_pressed }, background);
				break;
			case R.styleable.criView_groundpress:
				background = a.getDrawable(attr);
				drawable.addState(new int[] { android.R.attr.state_pressed },
						background);
				break;
			case R.styleable.criView_groundforce:
				background = a.getDrawable(attr);
				drawable.addState(new int[] { android.R.attr.state_focused },
						background);
				break;
			case R.styleable.criView_groundselect:
				drawable.addState(new int[] { android.R.attr.state_selected },
						background);
				background = a.getDrawable(attr);
				break;
			}
		}
		if (drawable != null) {
			if (android.os.Build.VERSION.SDK_INT >= 16) {
				view.setBackground(drawable);
			} else {
				view.setBackgroundDrawable(drawable);
			}
		}
	}
}
