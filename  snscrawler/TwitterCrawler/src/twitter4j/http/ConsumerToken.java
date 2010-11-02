package twitter4j.http;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;


public final class ConsumerToken extends OAuthToken implements java.io.Serializable {
	private final Configuration conf;
    private OAuthSupport oauth;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8182655073931636452L;

	public ConsumerToken(String token, String tokenSecret) {
		super(token, tokenSecret);
		conf = ConfigurationContext.getInstance();
	}
	/**
     * @return authorization URL
     * since Twitter4J 2.0.0
     */
    public String getAuthorizationURL() {
        return conf.getOAuthAuthorizationURL() + "?oauth_token=" + getToken();
    }

    /**
     * @return authentication URL
     * since Twitter4J 2.0.10
     */
    public String getAuthenticationURL() {
        return conf.getOAuthAuthenticationURL() + "?oauth_token=" + getToken();
    }
}
