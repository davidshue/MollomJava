import org.junit.Test

import com.mollom.client.Captcha
import com.mollom.client.CaptchaType
import com.mollom.client.Content
import com.mollom.client.MollomClient
import com.mollom.client.MollomClientBuilder
import com.mollom.client.MollomException


public class MollomTest
{

	@Test
	public void test() throws Exception
	{
		// Create a new Mollom client.
		MollomClient client = MollomClientBuilder.create()
		    .withPlatformName("Spring")
		    .withPlatformVersion("3.2.4")
		    // ... more client configuration ...
		    .build("44188fce43d60704ae08bc2615edc341", "42637ecc1c086f0e19b854bb4e7ce747")
		
		// Create a new Content to check.
		Content content = new Content()
		content.setAuthorIp("192.168.1.1")
		content.setPostBody("This is a test comment.")
		
		try {
		    client.checkContent(content)
		} catch (all) {
		    all.printStackTrace()
		}		

		if (content.isHam()) {
			// Accept the post.
			println 'content is ham'
		} else if (content.isUnsure()) {
			println 'content is unsure'
			// Ask the user to solve a CAPTCHA.
			Captcha captcha = client.createCaptcha(CaptchaType.IMAGE, false, content)
			captcha.setAuthorIp("192.168.1.1")
			
			int captchaRetries = 3
			String solution
			
			(0..3).each {
				// Output the image from captcha.getUrl() to the user to get solution.
				//solution = getUserSolution(captcha.getUrl());
				solution = captcha.getSolution()
				captcha.setSolution(solution)
				client.checkCaptcha(captcha)
				
				// If the user is able to change the post while solving the CAPTCHA,
				// ensure to re-check the content. If the CAPTCHA was solved, a
				// subsequent checkContent() call will return "ham" -- unless the user
				// edited the post to turn it into spam.
				client.checkContent(content)
				
				if (content.isHam()) {
					// Accept the post.
					return
				}
				
			}
			// Unsure without solved CAPTCHA: Reject the post.
		} else {
			println 'content is spam'
			// Spam: Reject the post.
		}	
	}
	

}
