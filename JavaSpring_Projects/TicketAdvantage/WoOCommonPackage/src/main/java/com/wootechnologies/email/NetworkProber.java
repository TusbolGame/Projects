package com.wootechnologies.email;

import java.net.Socket;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * 
 * @author jmiller
 *
 */
public abstract class NetworkProber {
	private static final Logger LOGGER = Logger.getLogger(NetworkProber.class);
    private final static int SLEEP_TIME = 1200000; // wait 20 secs between each probe
    private RetrieveEmail mail;
    private String host;
    private int port = 993;
    private String name = "NetworkProber";
    private int pingFailureCount = 0;
    private int sessionFailureCount = 0;
    private long lastBeat = -1;
    private boolean netConnectivity = false;
    private Timer timer;

    /**
     * 
     * @param host
     * @param port
     * @param accountName
     */
    public NetworkProber(String host, int port, String accountName) {
        System.err.println("host: " + host);
        this.host = host;
        this.port = port;
        this.name = "NetworkProper-" + accountName;
    }

    /**
     * 
     * @param mail
     */
    public NetworkProber(RetrieveEmail mail) {
        this.mail = mail;
        this.host = mail.getServerAddress();
        this.port = mail.getServerPort();
        this.name = "NetworkProper-" + mail.getAccountName();
    }

    /**
     * 
     * @return
     */
    private boolean probe() {
    	LOGGER.info("Entering probe()");
        boolean status = true;
        Socket socket = null;

        try {
            socket = new Socket(host, port);
            status = true;
            pingFailureCount = 0;
        } catch (Exception ex) {
            status = false;
            pingFailureCount++;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                }
            }
        }
        netConnectivity = status;

        LOGGER.info("Exiting probe()");
        return status;
    }

    /**
     * 
     * @return
     */
    private boolean probeWithSessionCheck() {
    	LOGGER.info("Entering probeWithSessionCheck()");

        boolean status = probe();
        LOGGER.debug("status: " + status);
        if (status) {
            if (mail.isSessionValid()) {
                sessionFailureCount = 0;
                LOGGER.info("Exiting probeWithSessionCheck()");
                return true;
            } else {
                sessionFailureCount++;
                LOGGER.info("Exiting probeWithSessionCheck()");
                return false;
            }
        }

        LOGGER.info("Exiting probeWithSessionCheck()");
        return false;
    }

    /**
     * 
     */
    private void periodicProber() {
    	LOGGER.info("Entering periodicProber()");

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	LOGGER.info("Entering run()");

                if (lastBeat != -1) {
                    long currentTime = System.currentTimeMillis();
                    long nTime = currentTime - lastBeat;
                    LOGGER.debug("nTime: " + nTime);
                    if (nTime > (SLEEP_TIME + 60000)) { // missed beat, probably because of sleep...
                        lastBeat = currentTime;    
                        missedBeat();
                        return;
                    } 
                    lastBeat = currentTime;
                } else {
                    lastBeat = System.currentTimeMillis();
                }

                if (mail == null)
                    onNetworkChange(probe());
                else
                    onNetworkChange(probeWithSessionCheck());

                LOGGER.info("Exiting run()");
            }
        };
        stop();
        timer = new Timer(name, true);
        timer.scheduleAtFixedRate(task, Calendar.getInstance().getTime(), SLEEP_TIME);

        LOGGER.info("Exiting periodicProber()");
    }

    /**
     * 
     */
    public void start() {
    	LOGGER.info("Entering start()");

        Runnable r = new Runnable() {
            public void run() {
                periodicProber();
            }
        };
        sessionFailureCount = 0;
        pingFailureCount = 0;
        Thread t = new Thread(r, name);
        t.setDaemon(true);
        t.start();

        LOGGER.info("Exiting start()");
    }

    /**
     * 
     */
    public void stop() {
    	LOGGER.info("Entering stop()");

        if (timer == null)
            return;

        timer.cancel();
        timer.purge();
        timer = null;

        LOGGER.info("Exiting stop()");
    }

    /**
     * 
     * @return
     */
    public int getPingFailureCount() {
        return pingFailureCount;
    }

    /**
     * 
     * @return
     */
    public int getSessionFailureCount() {
        return sessionFailureCount;
    }

    /**
     * 
     * @return
     */
    public boolean getNetConnectivity() {
        return netConnectivity;
    }

    /**
     * 
     * @param status
     */
    public abstract void onNetworkChange(boolean status);

    /**
     * 
     */
    public abstract void missedBeat();
}