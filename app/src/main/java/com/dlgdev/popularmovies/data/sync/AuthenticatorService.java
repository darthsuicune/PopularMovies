package com.dlgdev.popularmovies.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
	private MovieDatabaseAuthenticator authenticator;

	public AuthenticatorService() {
		authenticator = new MovieDatabaseAuthenticator(this);
	}

	@Override public IBinder onBind(Intent intent) {
		return authenticator.getIBinder();
	}
}
