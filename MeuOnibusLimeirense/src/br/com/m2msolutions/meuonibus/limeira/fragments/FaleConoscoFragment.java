package br.com.m2msolutions.meuonibus.limeira.fragments;import java.io.UnsupportedEncodingException;import org.apache.http.entity.StringEntity;import org.apache.http.message.BasicHeader;import org.apache.http.protocol.HTTP;import android.app.Activity;import android.content.Context;import android.os.Bundle;import android.support.v4.app.Fragment;import android.util.Log;import android.view.LayoutInflater;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.view.inputmethod.InputMethodManager;import android.widget.EditText;import android.widget.Toast;import br.com.m2m.meuonibuscommons.handlers.FaleConoscoHandler;import br.com.m2msolutions.meuonibus.limeira.R;import br.com.m2msolutions.meuonibus.limeira.activities.helpers.AppCustomDialogHelper;import br.com.m2msolutions.meuonibus.limeira.models.ws.MeuOnibusWS;public class FaleConoscoFragment extends Fragment {	private Activity ownerActivity;	InputMethodManager imm;	EditText textAssunto;	EditText textComentario;	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		setHasOptionsMenu(true);		// keyboard		imm = (InputMethodManager) ownerActivity				.getSystemService(Context.INPUT_METHOD_SERVICE);		View contentView = inflater.inflate(R.layout.fragment_fale_conosco,				container, false);		setLayout(contentView);		return contentView;	}	@Override	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		super.onCreateOptionsMenu(menu, inflater);		inflater.inflate(R.menu.menu_enviar, menu);	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		int id = item.getItemId();		if (id == R.id.action_enviar) {			closeKeyBoard();			sendMessage();			// Toast.makeText(ownerActivity.getApplicationContext(),			// getString(R.string.nao_foi_possivel_localizar_pontos),			// Toast.LENGTH_SHORT).show();		}		return super.onOptionsItemSelected(item);	}	@Override	public void onPause() {		super.onPause();		closeKeyBoard();	}	@Override	public void onResume() {		super.onResume();	}	@Override	public void onDestroy() {		super.onDestroy();		closeKeyBoard();	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}	private void setLayout(View contentView) {		textAssunto = (EditText) contentView				.findViewById(R.id.assunto_mensagem);		textComentario = (EditText) contentView				.findViewById(R.id.comentario_mensagem);	}	private void closeKeyBoard() {		textAssunto.clearFocus();		textComentario.clearFocus();		if (imm != null) {			imm.hideSoftInputFromWindow(textAssunto.getWindowToken(), 0);		}	}	private void sendMessage() {		String assunto = textAssunto.getText().toString();		String comentario = textComentario.getText().toString();		Toast.makeText(				ownerActivity.getApplicationContext(),				getString(R.string.mensagem_enviando),				Toast.LENGTH_SHORT).show();//		textAssunto.setText("");//		textComentario.setText("");		StringEntity entity = null;		if (!assunto.equals("") && !comentario.equals("")) {			try {				String xml = "<contactMessage><title>"+assunto+"</title><text>"+comentario+"</text></contactMessage>";				entity = new StringEntity(xml, "UTF-8");				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,						"application/xml"));			} catch (IllegalArgumentException e) {				Log.d("HTTP", "StringEntity: IllegalArgumentException");				return;			} catch (UnsupportedEncodingException e) {				Log.d("HTTP", "StringEntity: UnsupportedEncodingException");				return;			}			MeuOnibusWS.sendMessage(ownerActivity, entity,					new FaleConoscoHandler() {						@Override						public void setRetorno(boolean flag) {							Toast.makeText(									ownerActivity.getApplicationContext(),									getString(R.string.mensagem_enviada),									Toast.LENGTH_SHORT).show();						}						@Override						public void setErro(Throwable e) {							Toast.makeText(									ownerActivity.getApplicationContext(),									getString(R.string.mensagem_nao_enviada),									Toast.LENGTH_SHORT).show();						}					});		} else {			showVerifyMessage();		}	}		private void showVerifyMessage() {		String text = getString(R.string.todos_os_campos_obrigatorios);		AppCustomDialogHelper.showSimpleGenericDialog(ownerActivity, text);	}}