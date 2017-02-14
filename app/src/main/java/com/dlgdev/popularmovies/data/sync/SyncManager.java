package com.dlgdev.popularmovies.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.os.Build;
import android.os.Bundle;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.settings.Settings;

public class SyncManager {
	private Context context;

	public SyncManager(Context context) {
		this.context = context;
	}

	public void initializeAdapter() {
		getOrCreateSyncAccount();
	}

	Account getOrCreateSyncAccount() {
		AccountManager accountManager =
				(AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

		Account newAccount = new Account(context.getString(R.string.app_name),
				context.getString(R.string.sync_account_type));

		if (null == accountManager.getPassword(newAccount)) {
			if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
				return null;
			}
			onAccountCreated(newAccount);
		}
		return newAccount;
	}

	void onAccountCreated(Account newAccount) {
		configurePeriodicSync(Settings.SYNC_INTERVAL, Settings.SYNC_FLEXTIME);
		ContentResolver
				.setSyncAutomatically(newAccount, context.getString(R.string.content_authority),
						true);
		syncImmediately();
	}

	void configurePeriodicSync(int syncInterval, int flexTime) {
		Account account = getOrCreateSyncAccount();
		String authority = context.getString(R.string.content_authority);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// we can enable inexact timers in our periodic sync
			SyncRequest request = new SyncRequest.Builder().
					syncPeriodic(syncInterval, flexTime).
					setSyncAdapter(account, authority).
					setExtras(new Bundle()).build();
			ContentResolver.requestSync(request);
		} else {
			ContentResolver.addPeriodicSync(account,
					authority, new Bundle(), syncInterval);
		}
	}

	public void syncImmediately() {
		Bundle bundle = new Bundle();
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		ContentResolver
				.requestSync(getOrCreateSyncAccount(), context.getString(R.string.content_authority),
						bundle);
	}
}
