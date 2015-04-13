package br.com.hhw.startapp.helpers;

import br.com.hhw.startapp.R;
import br.com.hhw.startapp.handlers.CustomDialogHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialogHelper {

	public static void showGenericDialog(Context context, String text, final CustomDialogHandler dialogHandler) {
		showGenericDialog(context, null, text, dialogHandler);
	}
	
	public static void showGenericDialog(Context context, String title, String text, final CustomDialogHandler dialogHandler) {
		showGenericDialog(context, title, text, null, dialogHandler);
	}

	public static void showGenericDialog(Context context, String title,
			String text, String textButton, final CustomDialogHandler dialogHandler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		Activity ownerActivity = (Activity) context;
		LayoutInflater inflater = ownerActivity.getLayoutInflater();
		ViewGroup root = null;
		ViewGroup dialogLayout = (ViewGroup) inflater.inflate(
				R.layout.dialog_simple_start_app, root);

		if (title != null && title.length() > 0) {
			TextView textTitle = (TextView) dialogLayout
					.findViewById(R.id.dialog_title);
			textTitle.setText(title);
			textTitle.setVisibility(View.VISIBLE);
		}

		if (text != null && text.length() > 0) {
			TextView textMesage = (TextView) dialogLayout
					.findViewById(R.id.dialog_subtitle);
			textMesage.setText(text);
		}
		
		Button positiveButton = (Button) dialogLayout
				.findViewById(R.id.dialog_positive_button);
		
		Button negativeButton = (Button) dialogLayout
				.findViewById(R.id.dialog_negative_button);	
		if (textButton != null && textButton.length() > 0) {
			negativeButton.setText(textButton);
		}
		
		builder.setView(dialogLayout);
		final AlertDialog dialog = builder.create();

		negativeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				dialogHandler.setNo();
			}
		});
		
		positiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				dialogHandler.setOk();
			}

		});

		dialog.show();
	}
}
