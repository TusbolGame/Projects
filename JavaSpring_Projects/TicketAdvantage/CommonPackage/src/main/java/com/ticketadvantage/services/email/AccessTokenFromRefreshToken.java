package com.ticketadvantage.services.email;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

public class AccessTokenFromRefreshToken {
	private String clientid;
	private String clientsecret;
	private String refreshtoken;
	private String granttype;

	public static void main(String[] args) {
		// ticketadvantage@gmail.com
		String clientid = "529498092660-bcn5kjtr7iqr1k7fasv28ld16hgdi8p0.apps.googleusercontent.com";
		String clientsecret = "o4VwTH0ykC3qjyeMlI7FdlaM";						
		String refreshtoken = "1/CA1PWWUpXS6Y4N0jEGtK6KnR5fCCSD4mvg2gO-ERKC4";
		String granttype = "refresh_token";
		// String granttype = "authorization_code";

		// shootfromanywhere@gmail.com
/*
		String clientid = "439144314873-61i70o8fo7qrjikv9tae33msfo1cdpjq.apps.googleusercontent.com";
		String clientsecret = "XLYVqvLcab38BTXoBkPQsI4L";						
		String refreshtoken = "1/Fbd9Cr__juoc3GhZjp5JHkbuSjA4N_IthZcqumozftk";
		String granttype = "refresh_token";


 		https://console.developers.google.com/apis/credentials?project=ticketadvantage-181200
		https://developers.google.com/identity/protocols/googlescopes
		https://myaccount.google.com/secureaccount?ft=2&continue=https%3A%2F%2Fmyaccount.google.com%2Fnotifications&cs=5
		https://accounts.google.com/o/oauth2/v2/auth?scope=https%3A%2F%2Fmail.google.com/&access_type=offline&include_granted_scopes=true&state=state_parameter_passthrough_value&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code&client_id=529498092660-bcn5kjtr7iqr1k7fasv28ld16hgdi8p0.apps.googleusercontent.com
		POST - https://www.googleapis.com/oauth2/v4/token?code=4/AAAKqLt7JVPY4WCxZw-jDkUUlsH2lfKK51Nigo5cRET3flk1I89JEPIiq_eauKCww5hMNTVqChniSlgQBJm2Mxk&client_id=529498092660-bcn5kjtr7iqr1k7fasv28ld16hgdi8p0.apps.googleusercontent.com&client_secret=o4VwTH0ykC3qjyeMlI7FdlaM&redirect_uri=urn:ietf:wg:oauth:2.0:oob&grant_type=authorization_code
*/

		final AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(clientid, clientsecret, refreshtoken, granttype);
		String accessToken = accessTokenFromRefreshToken.getAccessToken();
		System.out.println("accessToken: " + accessToken);
	}

	/**
	 * 
	 */
	public AccessTokenFromRefreshToken() {
		super();
	}

	/**
	 * 
	 * @param clientid
	 * @param clientsecret
	 * @param refreshtoken
	 * @param granttype
	 */
	public AccessTokenFromRefreshToken(String clientid, String clientsecret, String refreshtoken, String granttype) {
		super();
		this.clientid = clientid;
		this.clientsecret = clientsecret;
		this.refreshtoken = refreshtoken;
		this.granttype = granttype;
	}

	/**
	 * @return the clientid
	 */
	public String getClientid() {
		return clientid;
	}

	/**
	 * @param clientid the clientid to set
	 */
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	/**
	 * @return the clientsecret
	 */
	public String getClientsecret() {
		return clientsecret;
	}

	/**
	 * @param clientsecret the clientsecret to set
	 */
	public void setClientsecret(String clientsecret) {
		this.clientsecret = clientsecret;
	}

	/**
	 * @return the refreshtoken
	 */
	public String getRefreshtoken() {
		return refreshtoken;
	}

	/**
	 * @param refreshtoken the refreshtoken to set
	 */
	public void setRefreshtoken(String refreshtoken) {
		this.refreshtoken = refreshtoken;
	}

	/**
	 * @return the granttype
	 */
	public String getGranttype() {
		return granttype;
	}

	/**
	 * @param granttype the granttype to set
	 */
	public void setGranttype(String granttype) {
		this.granttype = granttype;
	}

	/**
	 * 
	 * @return
	 */
    public String getAccessToken() {
        HttpURLConnection conn = null;
        String accessToken = null;

        try {
            final URL url = new URL("https://accounts.google.com/o/oauth2/token");

            final Map<String,Object> params = new LinkedHashMap<>();
            params.put("client_id", clientid);
            params.put("client_secret", clientsecret);
            params.put("refresh_token", refreshtoken);
            params.put("grant_type", granttype);

            final StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.setRequestProperty("Content-Length",
                                String.valueOf(postDataBytes.length));
            conn.setRequestProperty("Content-language", "en-US");
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream (
                            conn.getOutputStream());
            wr.write(postDataBytes);
            wr.close();

            StringBuilder sb = new StringBuilder();
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            for ( int c = in.read(); c != -1; c = in.read() ) {
                sb.append((char)c);
            }

            // Read access token from json response
            final AccessTokenObject accessTokenObj = new AccessTokenObject();
            final JSONObject obj = new JSONObject(sb.toString());
            accessTokenObj.setAccessToken(obj.getString("access_token"));
            accessTokenObj.setTokenType(obj.getString("token_type"));
            accessTokenObj.setExpiresIn(obj.getInt("expires_in"));

            accessToken = accessTokenObj.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect(); 
            }
        }

        return(accessToken);
    }
}