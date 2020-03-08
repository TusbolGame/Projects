package com.ticketadvantage.services.captcha;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class APIMain
{
	private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(APIMain.class);

    // your connect information
    public static String HOST          = "api.de-captcher.com";		// YOUR ADDRESS
    public static int    PORT          = 3500;						// YOUR PORT
    public static String USERNAME      = "kc2coast";
    public static String PASSWORD      = "4kstate4";
    public static String PIC_FILE_NAME = "/Users/jmiller/Documents/TicketAdvantage/captcha.bmp";

    public static void main(String[] args)
    {
        if( args.length != 0 ) {
            System.err.println( "Args: port username password image-file" );
            return;
        }

        // set the values from the args
//        PIC_FILE_NAME = args[3];
//        PASSWORD = args[2];
//        USERNAME = args[1];
        try {
//            PORT = Integer.decode(args[0]);
        } catch( Throwable t ) {
            System.err.println( "Unable to understand port number from start arguments" );
            return;
        }
    }

    /**
     * 
     * @param fileName
     */
    public static Map<String, String> getImageInfo(byte[] imagBytes) {
        LOGGER.info("Entering getImageInfo()");
        final Map<String, String> map = new HashMap<String, String>();
        String text = "";

        CCProto ccp = new CCProto();
        int retValue = 0;

        LOGGER.debug( "Logging in..." );
        if( (retValue = ccp.login( HOST, PORT, USERNAME, PASSWORD )) < 0 ) {
        	LOGGER.debug( " RetValue: " + retValue );
            LOGGER.debug( " FAILED" );
            return null;
        }
        LOGGER.debug( " OK" );

        String[] balance_arr = new String[1];
        if( ccp.balance(balance_arr) != APIConsts.ccERR_OK ) {
            System.err.println( "balance() FAILED" );
            return null;
        }
        LOGGER.debug( "Balance=" + balance_arr[0] );

        int major_id = 0;
        int minor_id = 0;
        for( int i = 0; i < 1; i++ ) {

            // binary picture data
            byte[] pict = imagBytes;
            LOGGER.debug( "sending a picture..." );

            int pict_to = APIConsts.ptoDEFAULT;
            int pict_type = APIConsts.ptUNSPECIFIED;

            int[] pict_to_arr = new int[2];
            pict_to_arr[0] = pict_to;

            int[] pict_type_arr = new int[2];
            pict_type_arr[0] = pict_type;

            String[] text_arr = new String[2];
            text_arr[0] = text;

            int[] major_id_arr = new int[2];
            major_id_arr[0] = major_id;

            int[] minor_id_arr = new int[2];
            minor_id_arr[0] = minor_id;

            int res = ccp.picture2(pict, pict_to_arr, pict_type_arr, text_arr, major_id_arr, minor_id_arr);
            switch (res) {
                // most common return codes
                case APIConsts.ccERR_OK:
                    pict_to = pict_to_arr[0];
                    pict_type = pict_type_arr[0];
                    text = text_arr[0];
                    major_id = major_id_arr[0];
                    minor_id = minor_id_arr[0];

                    LOGGER.debug(
                    		"got text for id=" + major_id + "/"
                            + minor_id + ", type=" + pict_type + ", to="
                            + pict_to + ", text='" + text + "'"
					);
                    map.put("text", text);
                    map.put("status", Integer.toString(APIConsts.ccERR_OK));
                    return map;
                case APIConsts.ccERR_BALANCE:
                	LOGGER.debug("not enough funds to process a picture, balance is depleted" );
                    map.put("text", "");
                    map.put("status", Integer.toString(APIConsts.ccERR_BALANCE));
                    return map;
                case APIConsts.ccERR_TIMEOUT:
                	LOGGER.debug("picture has been timed out on server (payment not taken)" );
                    map.put("text", "");
                    map.put("status", Integer.toString(APIConsts.ccERR_TIMEOUT));
                    return map;
                case APIConsts.ccERR_OVERLOAD:
                	LOGGER.debug( "temporarily server-side error" );
                	LOGGER.debug( " server's overloaded, wait a little before sending a new picture" );
                    map.put("text", "");
                    map.put("status", Integer.toString(APIConsts.ccERR_OVERLOAD));
                    return map;
                // local errors
                case APIConsts.ccERR_STATUS:
                    LOGGER.debug( "local error." );
                    LOGGER.debug( " either ccproto_init() or ccproto_login() has not been successfully called prior to ccproto_picture()" );
                    LOGGER.debug( " need ccproto_init() and ccproto_login() to be called" );
                    break;
                // network errors
                case APIConsts.ccERR_NET_ERROR:
                    LOGGER.debug( "network troubles, better to call ccproto_login() again" );
                    break;

                // server-side errors
                case APIConsts.ccERR_TEXT_SIZE:
                    LOGGER.debug( "size of the text returned is too big" );
                    break;
                case APIConsts.ccERR_GENERAL:
                    LOGGER.debug( "server-side error, better to call ccproto_login() again" );
                    break;
                case APIConsts.ccERR_UNKNOWN:
                    LOGGER.debug( " unknown error, better to call ccproto_login() again" );
                    break;

                default:
                    // any other known errors?
                    break;
            }

            // process a picture and if it is badly recognized
            // call picture_bad2() to name it as error.
            // pictures named bad are not charged

            // ccp.picture_bad2( major_id, minor_id );
        }

        if( ccp.balance( balance_arr ) != APIConsts.ccERR_OK ) {
            LOGGER.debug( "balance() FAILED\n" );
            return null;
        }
        LOGGER.debug( "Balance=" + balance_arr[0] );

        ccp.close();

        // also you can mark picture as bad after session is closed, but you
        // need to be logged in again
        LOGGER.debug( "Logging in..." );
        if( ccp.login( HOST, PORT, USERNAME, PASSWORD ) < 0 ) {
            LOGGER.debug( " FAILED" );
            return null;
        }
        LOGGER.debug( " OK" );
        LOGGER.debug( "Naming picture " + major_id + "/" + minor_id + " as bad" );
        ccp.picture_bad2( major_id, minor_id );

        ccp.close();
        
        LOGGER.info("Exiting getImageInfo()");
        return map;
    }
}
