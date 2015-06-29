package br.com.m2m.meuonibus.activities.helpers;import android.app.Activity;import android.app.AlertDialog;import android.content.Context;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.Button;import android.widget.EditText;import android.widget.TextView;import br.com.m2m.meuonibus.R;import br.com.m2m.meuonibuscommons.handlers.AppCustomDialogHandler;public class AppCustomDialogHelper {	public static void showSimpleGenericDialog(Context context, String text) {		AlertDialog.Builder builder = new AlertDialog.Builder(context);		Activity ownerActivity = (Activity) context;		LayoutInflater inflater = ownerActivity.getLayoutInflater();		ViewGroup root = null;		ViewGroup dialogLayout = (ViewGroup) inflater.inflate(				R.layout.dialog_custom_message, root);		if (text != null && text.length() > 0) {			TextView textMesage = (TextView) dialogLayout					.findViewById(R.id.dialog_app_subtitle);			textMesage.setText(text);		}		Button positiveButton = (Button) dialogLayout				.findViewById(R.id.dialog_app_positive_button);		positiveButton.setText("Ok");		Button negativeButton = (Button) dialogLayout				.findViewById(R.id.dialog_app_negative_button);		negativeButton.setVisibility(View.GONE);		builder.setView(dialogLayout);		final AlertDialog dialog = builder.create();		positiveButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				dialog.dismiss();			}		});		dialog.show();	}	public static void showGenericDialog(Context context, String text,			final AppCustomDialogHandler dialogHandler) {		showGenericDialog(context, null, text, dialogHandler);	}	public static void showGenericDialog(Context context, String title,			String text, final AppCustomDialogHandler dialogHandler) {		showGenericDialog(context, title, text, null, dialogHandler);	}	public static void showGenericDialog(Context context, String title,			String text, String textButton,			final AppCustomDialogHandler dialogHandler) {		AlertDialog.Builder builder = new AlertDialog.Builder(context);		Activity ownerActivity = (Activity) context;		LayoutInflater inflater = ownerActivity.getLayoutInflater();		ViewGroup root = null;		ViewGroup dialogLayout = (ViewGroup) inflater.inflate(				R.layout.dialog_custom_message, root);		if (title != null && title.length() > 0) {			TextView textTitle = (TextView) dialogLayout					.findViewById(R.id.dialog_title);			textTitle.setText(title);			textTitle.setVisibility(View.VISIBLE);		}		if (text != null && text.length() > 0) {			TextView textMesage = (TextView) dialogLayout					.findViewById(R.id.dialog_app_subtitle);			textMesage.setText(text);		}		Button positiveButton = (Button) dialogLayout				.findViewById(R.id.dialog_app_positive_button);		Button negativeButton = (Button) dialogLayout				.findViewById(R.id.dialog_app_negative_button);		if (textButton != null && textButton.length() > 0) {			negativeButton.setText(textButton);		}		builder.setView(dialogLayout);		final AlertDialog dialog = builder.create();		negativeButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				dialog.dismiss();				dialogHandler.setNo();			}		});		positiveButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				dialog.dismiss();				dialogHandler.setOk();			}		});		dialog.show();	}		public static void showDialogInput(final Context context, String title, final AppCustomDialogHandler dialogHandler) {		AlertDialog.Builder builder = new AlertDialog.Builder(context);		Activity ownerActivity = (Activity) context;		LayoutInflater inflater = ownerActivity.getLayoutInflater();		ViewGroup root = null;		final ViewGroup dialogLayout = (ViewGroup) inflater.inflate(				R.layout.dialog_input_data, root);				TextView textTitle = (TextView) dialogLayout				.findViewById(R.id.dialog_input_title);			Button positiveButton = (Button) dialogLayout				.findViewById(R.id.dialog_input_positive_button);		Button negativeButton = (Button) dialogLayout				.findViewById(R.id.dialog_input_negative_button);				textTitle.setText(title);		builder.setView(dialogLayout);		final AlertDialog dialog = builder.create();		negativeButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				dialog.dismiss();				dialogHandler.setNo();			}		});		positiveButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				EditText editText = (EditText) dialogLayout						.findViewById(R.id.dialog_input_edit_text_nome);				String nome = editText.getText().toString();				dialogHandler.setText(nome);				dialog.dismiss();			}		});				dialog.show();	}}