package hjh.criimag.util;

import android.util.SparseArray;
import android.view.View;

/**
 * @author Voctex 2015-08-13
 * @param 有点击时间间隔的点击事件
 * 
 */
public abstract class IntervalOnClick implements View.OnClickListener {

	private long beforeTime = 0;
	private int interval_time = 1000;
	private SparseArray<Long> sparseArray = new SparseArray<Long>();

	/**
	 * @param default interval_time is 1000ms
	 */
	public IntervalOnClick(int interval_time) {
		this.interval_time = interval_time;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		long currentTime = System.currentTimeMillis();
		beforeTime = sparseArray.get(v.getId()) != null ? sparseArray.get(v
				.getId()) : 0;
		if (currentTime - beforeTime >= interval_time) {
			sparseArray.put(v.getId(), beforeTime = currentTime);
			IonClick(v);
		}
	}

	public abstract void IonClick(View v);

}
