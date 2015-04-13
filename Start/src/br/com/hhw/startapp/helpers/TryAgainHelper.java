package br.com.hhw.startapp.helpers;

import br.com.hhw.startapp.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 
 * 
 */
public class TryAgainHelper extends LinearLayout implements OnClickListener {

	private Context context;
	
	private OnClickToTryAgain onClickToTryAgain;

	public interface OnClickToTryAgain {
		void tryAgain();
	}

	/*
	 * -------------- Default constructors
	 */
	public TryAgainHelper(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public TryAgainHelper(Context context,int idButton) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init(context);
	}

	private void init(Context context) {
		Button tryRecoverButton = (Button) this
				.findViewById(R.id.without_internet_button_try_again);

		tryRecoverButton.setOnClickListener(this);
	}

	public void setOnClickToTryAgain(OnClickToTryAgain onClickToTryAgain) {
		this.onClickToTryAgain = onClickToTryAgain;
	}
	
	public void setOnClickAndMessage(OnClickToTryAgain onClickToTryAgain,
			String msg) {
		setMessage(msg);
		this.onClickToTryAgain = onClickToTryAgain;
	}
	
	private void setMessage(String msg) {
		TextView text = (TextView) this
				.findViewById(R.id.without_internet_subtitle);
		text.setText(msg);
	}

	@Override
	public void onClick(View v) {
		if (onClickToTryAgain != null) {
			onClickToTryAgain.tryAgain();
		}
	}
}