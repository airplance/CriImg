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
 * Բ�Ǹ�ѡ���������������
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
		// �������ɫ��Բ�ǵ���ɫ��Բ�ǵİ뾶
		String gradColor = null, radiusColor = null, radiusInt = null, strokeColor = null;
		String[] split = null;
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {

			case R.styleable.criView_groundnormal:
				GradientDrawable grade = null;
				// ����ɫ
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
				// Բ��
				radiusInt = a.getString(R.styleable.criView_cornersradius);
				if (radiusInt != null) {
					// �ǲ����н���
					boolean isGrad = true;
					if (grade == null) {
						grade = new GradientDrawable();
						isGrad = false;
					}
					split = radiusInt.split("-");
					// ��ֵ����radius��û������4��ֵ
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
					// û�н���
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

				// �������Բ�Ƕ�û�У�ŪͼƬ��������ǲ����ý����Բ�ǵģ�
				// ҪôŪ����ͼƬ����ʱ��������Բ�ǻ��߽��䣬ֻҪһ���ã���ô����ͼƬ��û��
				// ��������Բ�ǻ��߽����ʱ����ô�Ͱ�groundpress����Ϊ��ɫֵ������ͼƬ
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
				// ����ɫ
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
				// Բ��
				radiusInt = a.getString(R.styleable.criView_cornersradius);
				if (radiusInt != null) {
					// �ǲ����н���
					boolean isGrad = true;
					if (gradepress == null) {
						gradepress = new GradientDrawable();
						isGrad = false;
					}
					split = radiusInt.split("-");
					// ��ֵ����radius��û������4��ֵ
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
					// û�н���
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
