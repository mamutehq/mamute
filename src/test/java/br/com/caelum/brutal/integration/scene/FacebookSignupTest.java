package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;

import br.com.caelum.brutal.auth.OAuthServiceCreator;
import br.com.caelum.brutal.integration.pages.FacebookLoginPage;
import br.com.caelum.brutal.integration.pages.Home;

import com.google.gson.JsonParser;

public class FacebookSignupTest extends AcceptanceTestBase {
	
	@Test
	public void should_signup_through_facebook() throws Exception {
		String appToken = getAppToken();
		FacebookUser facebookUser = createFacebookTestUser(appToken);
		FacebookLoginPage signupWithFacebook = home().toSignUpPage().signupWithFacebook();
		Home home = signupWithFacebook
			.writeEmail(facebookUser.email)
			.writePassword(facebookUser.password)
			.submit().confirm();
		
		assertTrue(home.isLoggedIn());
		assertEquals(facebookUser.email, home.toProfilePage().userEmail());
			
	}

	@SuppressWarnings("deprecation")
	private FacebookUser createFacebookTestUser(String appToken) throws URIException,
			IOException, HttpException {
		String clientId = env.get(OAuthServiceCreator.FACEBOOK_CLIENT_ID);
		GetMethod method = new GetMethod("https://graph.facebook.com/"+clientId+"/accounts/test-users?" +
				"installed=true" +
				"&name=TESTUSER" +
				"&method=post" +
				"&access_token=" + URLEncoder.encode(appToken));
		System.out.println(method.getURI());
		int status = client.executeMethod(method);
		if (status != 200) {
			fail("could not create test user, facebook sent " + status + " status code");
		}
		return new FacebookUser(method.getResponseBodyAsString());
	}

	private String getAppToken() throws IOException, HttpException {
		String appSecret = env.get(OAuthServiceCreator.FACEBOOK_APP_SECRET);
		String clientId = env.get(OAuthServiceCreator.FACEBOOK_CLIENT_ID);
		
		GetMethod method = new GetMethod("https://graph.facebook.com/oauth/access_token" +
				"?client_id=" + clientId +
				"&client_secret=" + appSecret +
				"&grant_type=client_credentials");
		int status = client.executeMethod(method);
		if (status != 200) {
			fail("could not create app access_token");
		}
		String responseBody = method.getResponseBodyAsString();
		return responseBody.split("=")[1];
	}

	
	private static class FacebookUser {

		private String email;
		private String password;

		public FacebookUser(String json) {
			this.email = new JsonParser().parse(json).getAsJsonObject().get("email").getAsString();
			this.password = new JsonParser().parse(json).getAsJsonObject().get("password").getAsString();
		}
		
	}
}
