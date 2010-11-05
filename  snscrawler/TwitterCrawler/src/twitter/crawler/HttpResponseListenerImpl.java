package twitter.crawler;

import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseEvent;
import twitter4j.internal.http.HttpResponseListener;

public class HttpResponseListenerImpl implements HttpResponseListener {

	@Override
	public void httpResponseReceived(HttpResponseEvent event) {
		HttpResponse httpResponse=event.getResponse();
		System.out.println("StatusCode:"+httpResponse.getStatusCode());
		try {
			System.out.println(httpResponse.asString());
			System.out.println(httpResponse.asDocument().getTextContent());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
