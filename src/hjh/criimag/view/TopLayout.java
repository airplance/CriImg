package hjh.criimag.view;

import hjh.criimag.R;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopLayout extends LinearLayout {

	private View view;
	private LinearLayout onBackLeft;
	private TextView onBackText;



	public TextView getOnBackText() {
		return onBackText;
	}

	public TopLayout(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		view = LayoutInflater.from(context).inflate(
				R.layout.view_toplayout_main, this);
		onBackLeft = (LinearLayout) view.findViewById(R.id.download_layout1);
		onBackText = (TextView) view.findViewById(R.id.txt_linear);
		onBackLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((Activity) context).finish();
			}
		});
	}

	public TopLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		view = LayoutInflater.from(context).inflate(
				R.layout.view_toplayout_main, this);
		onBackLeft = (LinearLayout) view.findViewById(R.id.download_layout1);
		onBackText = (TextView) view.findViewById(R.id.txt_linear);

		if (onBackLeft != null)
			onBackLeft.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((Activity) context).finish();
				}
			});
	}

	public void setVisibility(String Text) {
		onBackText.setText(Text);
	}
}
