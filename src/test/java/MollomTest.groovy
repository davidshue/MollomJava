import org.apache.commons.lang.math.RandomUtils
import org.junit.After
import org.junit.Before
import org.junit.Test

import com.mollom.client.Captcha
import com.mollom.client.CaptchaType
import com.mollom.client.Content
import com.mollom.client.MollomClient
import com.mollom.client.MollomClientBuilder


public class MollomTest
{
	private MollomClient client
	
	@Before
	void before() {
		// Create a new Mollom client.
		client = MollomClientBuilder.create()
				.withPlatformName("Spring")
				.withPlatformVersion("3.2.4")
				//.withTesting(true)
				//.build('1xpoj741hs7qt10btkxggvdfusuyda8a', 'ymmuups1ag5r31ju1a4swxab30qxbwe4')
				// ... more client configuration ...
				.build("44188fce43d60704ae08bc2615edc341", "42637ecc1c086f0e19b854bb4e7ce747")
	}
	
	@Test
	void test() throws Exception
	{
		
		// Create a new Content to check.
		Content content = new Content()
		String ip = randomIp()
		println ip
		content.setAuthorIp(ip)
		content.postTitle = 'My first time'
		//content.postBody = 'This is a test comment.' // unsure
		//content.postBody = 'This is my first baby ever.'
		content.postBody = """
Please find attached your Advice containing information on your transactions of last working day with the bank.

 Please do not reply to this e-mail address. If you have any queries, please contact our Customer Services. 

 Yours faithfully

 HSBC Bank (UK) Company Limited

"""
		//content.setPostBody('I had an interesting encounter yesterday') // ham
		
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
			
			(0..3).each {it->
				println it
				// Output the image from captcha.getUrl() to the user to get solution.
				//solution = getUserSolution(captcha.getUrl());
				println "captcha url: ${captcha.url}"
				println "captcha solution: ${captcha.solution}"
				
				captcha.setSolution('fake solution')
				client.checkCaptcha(captcha)
				
				// If the user is able to change the post while solving the CAPTCHA,
				// ensure to re-check the content. If the CAPTCHA was solved, a
				// subsequent checkContent() call will return "ham" -- unless the user
				// edited the post to turn it into spam.
				client.checkContent(content)
				
				if (content.isHam()) {
					println 'after captcha verification, it is ham'
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
	
	@After
	void tearDown() {
		client.destroy()
	}
	
	private String randomIp() {
		return (RandomUtils.nextInt(254) + 1) + '.' + (RandomUtils.nextInt(254) + 1) + '.' + (RandomUtils.nextInt(254) + 1) + '.' + (RandomUtils.nextInt(254) + 1)
	}

}
