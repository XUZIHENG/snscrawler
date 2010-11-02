package twitter.crawler.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.http.ConsumerToken;
import twitter4j.http.OAuthSupport;
import twitter4j.http.RequestToken;

public class OAuthTwitter{
	private static final String PROPERTIES_FILE_NAME="oauth.properties";
	private static final String TWITTER4J_PORPERTIES_FILE_NAME="twitter4j.properties";
	private String consumerKey;
	private String consumerSecret;
	private String requestTokenKey;
	private String requestTokenSecret;
	private String accessTokenKey;
	private String accessTokenSecret;
	private String screenName;
	public OAuthTwitter(String fileName){
		loadProperties(fileName);
	}
	public OAuthTwitter(){
		loadProperties(PROPERTIES_FILE_NAME);
	}
	public String getConsumerKey() {
		return consumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	public String getConsumerSecret() {
		return consumerSecret;
	}
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	public String getRequestTokenKey() {
		return requestTokenKey;
	}
	public void setRequestTokenKey(String requestTokenKey) {
		this.requestTokenKey = requestTokenKey;
	}
	public String getRequestTokenSecret() {
		return requestTokenSecret;
	}
	public void setRequestTokenSecret(String requestTokenSecret) {
		this.requestTokenSecret = requestTokenSecret;
	}
	public String getAccessTokenKey() {
		return accessTokenKey;
	}
	public void setAccessTokenKey(String accessTokenKey) {
		this.accessTokenKey = accessTokenKey;
	}
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public Properties loaProperties(){
		return loadProperties(PROPERTIES_FILE_NAME);
	}
	public Properties loadProperties(String fileName){
		Properties properties=new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream(fileName));
			consumerKey=properties.getProperty("consumer.key");
			consumerSecret=properties.getProperty("consumer.secret");
			requestTokenKey=properties.getProperty("request.token.key");
			requestTokenSecret=properties.getProperty("request.token.secret");
			accessTokenKey=properties.getProperty("access.token.key");
			accessTokenSecret=properties.getProperty("access.token.secret");
			screenName=properties.getProperty("screen.name");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
	public RequestToken acquireRequestToken(OAuthSupport oAuthSupport) throws TwitterException{
		return oAuthSupport.getOAuthRequestToken();
	}
	public RequestToken acquireRequestToken(Properties properties){
		return new RequestToken(properties.getProperty("access.token.key"),properties.getProperty("access.token.secret"));
	}
	public RequestToken acquireRequestToken(){
		return new RequestToken(requestTokenKey,requestTokenSecret);
	}
	public ConsumerToken acquireConsumerToken(Properties properties){
		return new ConsumerToken(properties.getProperty("consumer.key"), properties.getProperty("consumer.secret"));
	}
	public AccessToken acquireAccessToken(OAuthSupport oAuthSupport,String oauthVerifier) throws TwitterException{
		return oAuthSupport.getOAuthAccessToken(oauthVerifier);
	}
	public AccessToken acquireAccessToken(Properties properties){
		return new AccessToken(properties.getProperty("access.token.key"), properties.getProperty("access.token.secret"));
	}
	public AccessToken acquireAccessToken(){
		return new AccessToken(accessTokenKey,accessTokenSecret);
	}
	public Twitter oAuthFirstLogin(){
		Twitter twitter=new TwitterFactory(new PropertyConfiguration(ClassLoader.getSystemResourceAsStream(TWITTER4J_PORPERTIES_FILE_NAME))).getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		try {
			RequestToken requestToken=acquireRequestToken(twitter);
			System.out.println("Got request token.");
            System.out.println("Request token: "+ requestToken.getToken());
            System.out.println("Request token secret: "+ requestToken.getTokenSecret());
            AccessToken accessToken = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());
                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                String pin = br.readLine();
                try{
                    if(pin.length() > 0){
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    }else{
                        accessToken = twitter.getOAuthAccessToken(requestToken);
                    }
                } catch (TwitterException te) {
                    if(401 == te.getStatusCode()){
                        System.out.println("Unable to get the access token.");
                    }else{
                        te.printStackTrace();
                    }
                }
            }
            System.out.println("Got access token.");
            System.out.println("Access token: "+ accessToken.getToken());
            System.out.println("Access token secret: "+ accessToken.getTokenSecret());
//            Status status = twitter.updateStatus("再次测试用twitter4j发推！");
//            System.out.println("Successfully updated the status to [" + status.getText() + "].");
		} catch (TwitterException te) {
			System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit( -1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return twitter;
	}
	public Twitter oAuthLogin(){
		AccessToken accessToken=acquireAccessToken();
		return new TwitterFactory(new PropertyConfiguration(ClassLoader.getSystemResourceAsStream(TWITTER4J_PORPERTIES_FILE_NAME))).getOAuthAuthorizedInstance(consumerKey, consumerSecret, accessToken);
		
	}
	public boolean oAuthUpdate(Twitter twitter,String updatesString){
		try {
			twitter.updateStatus(updatesString);
		} catch (TwitterException e) {
			System.out.println("update twitter status failed");
			e.printStackTrace();
			return false;
		}
		System.out.println("update twitter status success");
		return true;
	}
	public static void main(String[] args) {
		OAuthTwitter oAuthTwitter=new OAuthTwitter("snscrawler.properties");
		oAuthTwitter.oAuthUpdate(oAuthTwitter.oAuthLogin(), "@kobe00712 大宝贝，哥给空老师发推去！");
	}
}	
