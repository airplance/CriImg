package hjh.criimag;

import hjh.criimag.view.CircleImageView;
import hjh.criimag.view.PopPhotoView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
	private CircleImageView photoView;
	private PopPhotoView popPhotoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cri_main);
		photoView = (CircleImageView) findViewById(R.id.profile_photo);
		popPhotoView = new PopPhotoView(this, R.layout.cri_view_photo_main,
				R.id.personal_main, null,photoView, null, null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (popPhotoView != null) {
			popPhotoView.onActivityResult(requestCode, resultCode, data);
		}
	}
}
