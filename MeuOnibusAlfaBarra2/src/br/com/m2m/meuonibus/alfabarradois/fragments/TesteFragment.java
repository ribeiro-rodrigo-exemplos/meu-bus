package br.com.m2m.meuonibus.alfabarradois.fragments;import android.os.Bundle;import android.support.v4.app.Fragment;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.TextView;import br.com.m2m.meuonibus.alfabarradois.R;public class TesteFragment extends Fragment {		public final String ARGS_CONTEUDO_TESTE = "teste_conteudo";	TextView text;	public TesteFragment newInstance(String text) {		TesteFragment fragment = new TesteFragment();		Bundle args = new Bundle();		args.putString(fragment.ARGS_CONTEUDO_TESTE, text);		fragment.setArguments(args);		return fragment;	}	    @Override    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {        View view = inflater.inflate(R.layout.teste_fragment, container, false);        if (getArguments() != null) {         	 String menu = getArguments().getString(ARGS_CONTEUDO_TESTE);        	 text= (TextView) view.findViewById(R.id.detail);             text.setText(menu);        } else {        }          return view;    }}