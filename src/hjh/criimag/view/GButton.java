package hjh.criimag.view;

import hjh.criimag.util.CriCGUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class GButton extends Button {

	public GButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public GButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		CriCGUtil.setGroundFromView(this, attrs);
	}
}
