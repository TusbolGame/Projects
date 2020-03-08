package com.ticketadvantage.services.email;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Vector;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import org.apache.log4j.Logger;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.SortTerm;
import com.sun.mail.imap.protocol.IMAPProtocol;

/**
 * 
 * @author jmiller
 *
 */
public abstract class RetrieveEmail implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(RetrieveEmail.class);
	public final static int READ_ONLY_FOLDER = Folder.READ_ONLY;
	public final static int READ_WRITE_FOLDER = Folder.READ_WRITE;
	protected final OAuth2Authenticator oAuth2Authenticator = new OAuth2Authenticator();
	private boolean connected = false;
	private boolean usePush = true;
	private String accountName;
	private String serverAddress;
	private String username;
	private String password;
	private String accesstoken;
	private int serverPort;
	private boolean useSSL;
	private IMAPStore server;
	private IMAPFolder folder;
	private MessageCountListener messageCountListener, externalCountListener;
	private MessageChangedListener messageChangedListener, externalChangedListener;
	private NetworkProber prober;
	private MailPoller poller;
	private Thread pushThread;

	/**
	 * 
	 * @param accountName
	 * @param serverAddress
	 * @param serverPort
	 * @param useSSL
	 */
	public RetrieveEmail(String accountName, String serverAddress, int serverPort, boolean useSSL) {
		super();
		this.accountName = accountName;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.useSSL = useSSL;

		oAuth2Authenticator.initialize();
	}

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public void setCredentials(String username, String password) {
		LOGGER.info("Entering setCredentials()");

		this.username = username;
		this.password = password;

		LOGGER.info("Exiting setCredentials()");
	}

	/**
	 * 
	 * @param username
	 * @param accesstoken
	 */
	public void setToken(String username, String accesstoken) {
		LOGGER.info("Entering setToken()");

		this.username = username;
		this.accesstoken = accesstoken;

		LOGGER.info("Exiting setToken()");
	}

	/**
	 * 
	 */
	public void run() {
		LOGGER.info("Entering run()");

		this.initConnection();

		LOGGER.info("Exiting run()");
	}

	/**
	 * 
	 */
	public void connect() {
		LOGGER.info("Entering connect()");
		try {
			selectFolder("");
			prober.start();
			connected = true;
			LOGGER.info(accountName + " connected!");
			onConnect();
		} catch (IllegalStateException ex) {
			LOGGER.warn(accountName, ex);
			connected = true;
			onConnect();
		}

		/*
		 * try { server.connect(serverAddress, serverPort, username, password);
		 * selectFolder(""); prober.start(); connected = true; LOGGER.info(accountName +
		 * " connected!"); onConnect(); } catch (AuthenticationFailedException ex) {
		 * connected = false; LOGGER.warn(accountName, ex); onError(ex); } catch
		 * (MessagingException ex) { connected = false; folder = null;
		 * messageChangedListener = null; messageCountListener = null;
		 * LOGGER.warn(accountName, ex); onError(ex); } catch (IllegalStateException ex)
		 * { LOGGER.warn(accountName, ex); connected = true; onConnect(); }
		 */

		LOGGER.info("Exiting connect()");
	}

	/**
	 * 
	 */
	public void disconnect() {
		LOGGER.info("Entering disconnect()");

		if (!connected && server == null && !server.isConnected())
			return;

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					closeFolder();
					server.close();
					prober.stop();
					poller.stop();
					connected = false;
					LOGGER.info(accountName + " disconnected!");
					onDisconnect();
				} catch (Exception e) {
					LOGGER.debug(e);
					onError(e);
				}
			}
		});
		t.start();

		LOGGER.info("Exiting disconnect()");
	}

	/**
	 * 
	 * @param listener
	 */
	public void setMessageChangedListerer(MessageChangedListener listener) {
		removeListener(externalChangedListener);
		externalChangedListener = listener;
		addListener(externalChangedListener);
	}

	/**
	 * 
	 * @param listener
	 */
	public void setMessageCounterListerer(MessageCountListener listener) {
		removeListener(externalCountListener);
		externalCountListener = listener;
		addListener(externalCountListener);
	}

	/**
	 * 
	 */
	private void initConnection() {
		LOGGER.info("Entering initConnection()");

		prober = new NetworkProber(this) {
			@Override
			public void onNetworkChange(boolean status) {
				LOGGER.debug("status: " + status);
				LOGGER.debug("connected: " + connected);

				if (status != connected) { // if two states do not match, something has truly changed!
					if (status && !connected) { // if connection up, but not connected...
						LOGGER.debug("Calling connect()");
						connect();
					} else if (!status && connected) { // if previously connected, but link down... then just
														// disconnect...
						if (getSessionFailureCount() >= 2 || getPingFailureCount() >= 2) {
							LOGGER.debug("Failure count is greater than/equals to 2");
							connected = false;
							if (!usePush) {
								poller.stop();
							}

							LOGGER.debug("Calling onDisconnect()");
							onDisconnect();
							// connect();
						}
					}
				} else { // if link (either session or net connection) and connection down, something
							// gone wrong...
					if (!isSessionValid() && getNetConnectivity()) { // need to make sure that session is down, but link
																		// is up...
						LOGGER.debug("Calling connect()");
						connect();
					}
				}
			}

			@Override
			public void missedBeat() { // missed beat, because of going to sleep, probably?!
				LOGGER.debug("Entering missedBeat()");

				connected = false;

				LOGGER.debug("Exiting missedBeat()");
			}
		};

		poller = new MailPoller(folder) {
			@Override
			public void onNewMessage() {
				try {
					if (externalCountListener != null) {
						externalCountListener.messagesAdded(
								new MessageCountEvent(folder, MessageCountEvent.ADDED, false, getNewMessages()));
						messageCountListener.messagesAdded(
								new MessageCountEvent(folder, MessageCountEvent.ADDED, false, getNewMessages()));
					}
				} catch (Exception e) {
					onError(e);
				}

			}
		};

		try {
			oAuth2Authenticator.initialize();
			server = oAuth2Authenticator.connectToImap(serverAddress, serverPort, username, accesstoken, false);
			connect();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		/*
		 * // enable to throw out everything... //props.put("mail.debug", "true");
		 * 
		 * final Properties props = System.getProperties(); String imapProtocol =
		 * "imap"; if (useSSL) { imapProtocol = "imaps";
		 * props.setProperty("mail.imap.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory");
		 * props.setProperty("mail.imap.socketFactory.fallback", "false"); }
		 * props.setProperty("mail.store.protocol", imapProtocol); session =
		 * Session.getDefaultInstance(props, null); try { server = (IMAPStore)
		 * session.getStore(imapProtocol); LOGGER.debug("Calling connect()"); connect();
		 * } catch (MessagingException ex) { LOGGER.debug(ex); onError(ex); }
		 */

		LOGGER.info("Exiting initConnection()");
	}

	/**
	 * 
	 * @param folderName
	 */
	private void selectFolder(String folderName) {
		LOGGER.info("Entering selectFolder()");

		try {
			closeFolder();
			if (folderName.equalsIgnoreCase("")) {
				folder = (IMAPFolder) server.getFolder("INBOX");
			} else {
				folder = (IMAPFolder) server.getFolder(folderName);
			}
			openFolder();
		} catch (MessagingException ex) {
			LOGGER.debug(ex);
			onError(ex);
		} catch (IllegalStateException ex) {
			LOGGER.debug(ex);
		}

		LOGGER.info("Exiting selectFolder()");
	}

	/**
	 * 
	 * @throws MessagingException
	 */
	private void openFolder() throws MessagingException {
		LOGGER.info("Entering openFolder()");

		if (folder == null)
			return;

		folder.open(Folder.READ_ONLY);
		folder.setSubscribed(true);
		removeAllListenersFromFolder();
		addAllListenersFromFolder();
		poller.setFolder(folder);

		if (usePush) {
			usePush();
		} else {
			poller.start(accountName);
		}

		LOGGER.info("Exiting openFolder()");
	}

	/**
	 * 
	 * @throws MessagingException
	 */
	private void closeFolder() throws MessagingException {
		LOGGER.info("Entering closeFolder()");

		if (folder == null || !folder.isOpen())
			return;

		removeAllListenersFromFolder();
		folder.setSubscribed(false);
		folder.close(false);
		folder = null;

		LOGGER.info("Exiting closeFolder()");
	}

	/**
	 * 
	 */
	private void usePush() {
		LOGGER.info("Entering usePush()");

		if (folder == null || !usePush)
			return;

		Runnable r = new Runnable() {
			public void run() {
				LOGGER.info("Entering run()");

				try {
					// We need to create a new thread to keep alive the connection
					Thread t = new Thread(new KeepAliveRunnable(folder), "IdleConnectionKeepAlive");
					t.start();

					while (!Thread.interrupted()) {
						folder.idle(false);
					}

					// Shutdown keep alive thread
					if (t.isAlive()) {
						t.interrupt();
					}
				} catch (FolderClosedException e) {
					LOGGER.warn("Push Error: [DISCONNECT] " + accountName, e);
					usePush = true;
					nullifyListeners();
					selectFolder("");
				} catch (javax.mail.StoreClosedException e) {
					LOGGER.warn("Push Error: [GLOBAL DISCONNECT] " + accountName, e);
					usePush = true;
				} catch (MessagingException e) {
					LOGGER.warn("Push Error: [NO IDLE] " + accountName, e);
					usePush = false;
					selectFolder("");
				} catch (Exception e) {
					LOGGER.warn("Push Error: [UNKNOWN] " + accountName, e);
					usePush = false;
					selectFolder("");
				}

				LOGGER.info("Exiting run()");
			}
		};
		pushThread = new Thread(r, "Push-" + accountName);
		pushThread.setDaemon(true);
		pushThread.start();

		LOGGER.info("Exiting usePush()");
	}

	/**
	 * 
	 */
	private void removeAllListenersFromFolder() {
		LOGGER.info("Entering removeAllListenersFromFolder()");

		removeListener(externalChangedListener);
		removeListener(externalCountListener);

		LOGGER.info("Exiting removeAllListenersFromFolder()");
	}

	/**
	 * 
	 * @param listener
	 */
	private void removeListener(EventListener listener) {
		LOGGER.info("Entering removeListener()");

		if (listener == null || folder == null) {
			return;
		}

		if (listener instanceof MessageChangedListener) {
			folder.removeMessageChangedListener((MessageChangedListener) listener);
		} else {
			if (listener instanceof MessageCountListener) {
				folder.removeMessageCountListener((MessageCountListener) listener);
			}
		}

		LOGGER.info("Exiting removeListener()");
	}

	/**
	 * 
	 */
	private void addAllListenersFromFolder() {
		LOGGER.info("Entering addAllListenersFromFolder()");

		addListener(externalCountListener);
		addListener(externalChangedListener);

		LOGGER.info("Exiting addAllListenersFromFolder()");
	}

	/**
	 * 
	 * @param listener
	 */
	private void addListener(EventListener listener) {
		LOGGER.info("Entering addListener()");

		if (listener == null || folder == null) {
			return;
		}

		if (listener instanceof MessageChangedListener) {
			LOGGER.debug("Adding changed listener");
			folder.addMessageChangedListener((MessageChangedListener) listener);
		} else if (listener instanceof MessageCountListener) {
			LOGGER.debug("Adding count listener");
			folder.addMessageCountListener((MessageCountListener) listener);
		}

		addInternalListeners(listener);

		LOGGER.info("Exiting addListener()");
	}

	/**
	 * 
	 * @param listener
	 */
	private void addInternalListeners(EventListener listener) {
		LOGGER.info("Entering addInternalListeners()");

		if (listener == null || folder == null) {
			return;
		}

		if (listener instanceof MessageChangedListener && messageChangedListener == null) {
			LOGGER.debug("messageChangedListener is null");
			messageChangedListener = new MessageChangedListener() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see javax.mail.event.MessageChangedListener#messageChanged(javax.mail.event.
				 * MessageChangedEvent)
				 */
				@Override
				public void messageChanged(MessageChangedEvent mce) {
					LOGGER.info("Entering messageChanged()");

					usePush();

					LOGGER.info("Exiting messageChanged()");
				}
			};
			// folder.addMessageChangedListener(messageChangedListener);
		} else {
			if (listener instanceof MessageCountListener && messageCountListener == null) {
				LOGGER.debug("messageCountListener is null");
				messageCountListener = new MessageCountListener() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see javax.mail.event.MessageCountListener#messagesAdded(javax.mail.event.
					 * MessageCountEvent)
					 */
					@Override
					public void messagesAdded(MessageCountEvent mce) {
						LOGGER.info("Entering messagesAdded()");

						usePush();

						LOGGER.info("Exiting messagesAdded()");
					}

					public void messagesRemoved(MessageCountEvent mce) {
						LOGGER.info("Entering messagesRemoved()");

						usePush();

						LOGGER.info("Exiting messagesRemoved()");
					}
				};
				// folder.addMessageCountListener(messageCountListener);
			}
		}

		LOGGER.info("Exiting addInternalListeners()");
	}

	/**
	 * Runnable used to keep alive the connection to the IMAP server
	 * 
	 * @author jmiller
	 */
	private static class KeepAliveRunnable implements Runnable {
		private static final long KEEP_ALIVE_FREQ = 300000; // 5 minutes
		private IMAPFolder folder;

		public KeepAliveRunnable(IMAPFolder folder) {
			this.folder = folder;
		}

		@Override
		public void run() {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(KEEP_ALIVE_FREQ);

					// Perform a NOOP just to keep alive the connection
					LOGGER.debug("Performing a NOOP to keep alive the connection");
					folder.doCommand(new IMAPFolder.ProtocolCommand() {
						public Object doCommand(IMAPProtocol p) throws ProtocolException {
							p.simpleCommand("NOOP", null);
							return null;
						}
					});
				} catch (InterruptedException e) {
					// Ignore, just aborting the thread...
				} catch (MessagingException e) {
					// Shouldn't really happen...
					LOGGER.warn("Unexpected exception while keeping alive the IDLE connection", e);
				}
			}
		}
	}

	/**
	 * 
	 */
	private void nullifyListeners() {
		messageChangedListener = null;
		messageCountListener = null;
	}

	/**
	 * 
	 * @return
	 * @throws MessagingException
	 */
	public Message[] getNewMessages() throws MessagingException {
		LOGGER.info("Entering getNewMessages()");

		ArrayList<Message> mess = new ArrayList<Message>();

		Message[] allmess = folder.getSortedMessages(new SortTerm[] { SortTerm.ARRIVAL, SortTerm.DATE });

		for (int i = 0; i < poller.getDiffCount(); i++) {
			if (allmess[i].isSet(Flags.Flag.SEEN) == false) {
				mess.add(allmess[i]);
			}
		}

		Message[] messages = new Message[mess.size()];
		for (int i = 0; i < mess.size(); i++) {
			messages[i] = mess.get(i);
		}

		LOGGER.info("Exiting getNewMessages()");
		return messages;
	}

	/**
	 * 
	 * @return
	 * @throws MessagingException
	 */
	public Message[] getMessages() throws MessagingException {
		return folder.getMessages();
	}

	public String getAccountName() {
		return accountName;
	}

	public String getPassword() {
		return password;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public boolean isSSL() {
		return useSSL;
	}

	public String getUsername() {
		return username;
	}

	public boolean isConnected() {
		return connected;
	}

	public boolean isSessionValid() {
		return server.isConnected();
	}

	@Override
	public String toString() {
		return accountName;
	}

	/**
	 * 
	 * @return
	 */
	public Vector getVectorData() {
		final Vector data = new Vector();
		data.add(accountName);
		data.add(serverAddress);
		data.add(serverPort);
		data.add(useSSL);
		data.add(username);
		data.add(password);
		return data;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @param e
	 */
	public abstract void onError(Exception e);

	/**
	 * 
	 */
	public abstract void onDisconnect();

	/**
	 * 
	 */
	public abstract void onConnect();
}