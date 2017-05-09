package br.com.m2m.meuonibus.roche.socket;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import br.com.m2m.meuonibus.roche.util.Util;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketConnector {

	private Socket socket = null;
	private Context context;
	private int delay = 15000;   // delay de 15 seg.
    private int interval = 5000;  // intervalo de 5 seg.
    private Timer timer;

	public SocketConnector(Context context) {
		this.context = context;
	}

	public void connect(final SocketListener listener) {
		initializeSocket(listener, context);

		socket.connect();
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

	        @Override
	        public void call(Object... args) {
	            setupSubs(listener);
	        }

	    }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

	      @Override
	      public void call(Object... args) {
	          listener.onConnectError(args);
	      }
	  });
	}

	private void setupSubs(final SocketListener listener) {
		socket.emit("setupSubs", listener.getSubscription());
		socket.on("setupSubs", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            	sync(listener);
            }
        });		
	}
	
	public void setupSubs(String subscription) {
		socket.emit("setupSubs", subscription);
	}

	private void sync(final SocketListener listener) {
		socket.emit("sync");
		scheduleSync();
		
		socket.on("sync", new Emitter.Listener() {

		    @Override
		    public void call(Object... args) {
				try {
					JSONObject response = (JSONObject) args[0];
					JSONArray data = (JSONArray) response.get("data");

					listener.onSync(data);
				} catch (JSONException e) {
					Log.e("#sync: ", e.getMessage());
					listener.onEventError(e);
				}
		    }
		});
	}
	
	private void initializeSocket(SocketListener listener, Context context) {
		String baseUrl = Util.getSocketUrl(context);
		try {
			socket = IO.socket(baseUrl);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			listener.onEventError(e1);
		}
	}

	private void scheduleSync() {
		if (timer != null) {
			return;
		}
		timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
				socket.emit("sync");
            }

        }, delay, interval);
	}
}
