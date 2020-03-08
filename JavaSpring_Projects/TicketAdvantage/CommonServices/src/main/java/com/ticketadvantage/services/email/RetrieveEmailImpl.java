package com.ticketadvantage.services.email;

public class RetrieveEmailImpl extends RetrieveEmail {
	private boolean connected = false;

	/**
	 * 
	 * @param accountName
	 * @param serverAddress
	 * @param serverPort
	 * @param useSSL
	 */
	public RetrieveEmailImpl(String accountName, String serverAddress, int serverPort, boolean useSSL) {
		super(accountName, serverAddress, serverPort, useSSL);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// ticketadvantage@gmail.com
		final RetrieveEmailImpl retrieveIMAPEmail = new RetrieveEmailImpl("ticketadvantage@gmail.com",
				"imap.gmail.com",
				993,
				true);
		
		AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(
				"529498092660-bcn5kjtr7iqr1k7fasv28ld16hgdi8p0.apps.googleusercontent.com", 
				"o4VwTH0ykC3qjyeMlI7FdlaM", 						
				"1/CA1PWWUpXS6Y4N0jEGtK6KnR5fCCSD4mvg2gO-ERKC4", 
				"refresh_token");
		retrieveIMAPEmail.setToken("ticketadvantage@gmail.com", accessTokenFromRefreshToken.getAccessToken());

		// shootfromanywhere@gmail.com
/*		final RetrieveEmailImpl retrieveIMAPEmail = new RetrieveEmailImpl("shootfromanywhere@gmail.com",
				"imap.gmail.com",
				993,
				true);

		final AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(
				"439144314873-61i70o8fo7qrjikv9tae33msfo1cdpjq.apps.googleusercontent.com", 
				"XLYVqvLcab38BTXoBkPQsI4L", 						
				"1/Fbd9Cr__juoc3GhZjp5JHkbuSjA4N_IthZcqumozftk", 
				"refresh_token");
		retrieveIMAPEmail.setToken("shootfromanywhere@gmail.com", accessTokenFromRefreshToken.getAccessToken());
*/
		retrieveIMAPEmail.setMessageCounterListerer(new MessageCountListenerIMAP(null));
		retrieveIMAPEmail.setMessageChangedListerer(new MessageChangedListenerIMAP(null));

		Thread t = new Thread(retrieveIMAPEmail);
		t.setName("JPM-" + retrieveIMAPEmail.getAccountName());
		t.start();

		while (true) {
			try {
				Thread.sleep(1000000);
			} catch (Throwable te) {
				te.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.email.JavaPushMailAccount#onError(java.lang.Exception)
	 */
	@Override
	public void onError(Exception e) {
		connected = false;
//        handleError(this, e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.email.JavaPushMailAccount#onDisconnect()
	 */
	@Override
	public void onDisconnect() {
		 connected = true;
//		 onStateChange();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.email.JavaPushMailAccount#onConnect()
	 */
	@Override
	public void onConnect() {
        connected = false;
//        onStateChange();
	}
}