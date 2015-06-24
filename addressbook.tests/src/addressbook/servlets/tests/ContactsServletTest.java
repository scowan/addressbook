package addressbook.servlets.tests;

import junit.framework.TestCase;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ContactsServletTest extends TestCase {
	
	private CloseableHttpClient httpclient = HttpClients.createDefault();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		httpclient = HttpClients.createDefault();
	}

	public void test() throws Exception {
		
		// delete contacts
		httpclient = HttpClients.createDefault();
		HttpDelete delete = new HttpDelete("http://localhost:8080//addressbook/contacts");
		CloseableHttpResponse response = httpclient.execute(delete);
		
	    assertEquals(200, response.getStatusLine().getStatusCode());

		// post Kailyn
		httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost:8080//addressbook/contacts");
		HttpEntity entity = new StringEntity("{\"name\":\"Kailyn\",\"age\":\"11\"}");
		post.setEntity(entity);
		response = httpclient.execute(post);
		
	    assertEquals(201, response.getStatusLine().getStatusCode());
	    
	    // post Ella
	    httpclient = HttpClients.createDefault();
		post = new HttpPost("http://localhost:8080//addressbook/contacts");
		entity = new StringEntity("{\"name\":\"Ella\",\"age\":\"9\"}");
		post.setEntity(entity);
		response = httpclient.execute(post);
		
	    assertEquals(201, response.getStatusLine().getStatusCode());
	    
	    // post Ella again
	    httpclient = HttpClients.createDefault();
		post = new HttpPost("http://localhost:8080//addressbook/contacts");
		entity = new StringEntity("{\"name\":\"Ella\",\"age\":\"9\"}");
		post.setEntity(entity);
		response = httpclient.execute(post);
		
	    assertEquals(400, response.getStatusLine().getStatusCode());
	    
	    // get Kailyn
	    httpclient = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://localhost:8080//addressbook/contacts/Kailyn");
		response = httpclient.execute(get);
		
	    assertEquals(200, response.getStatusLine().getStatusCode());
	    
	    // get Ella
	    httpclient = HttpClients.createDefault();
		get = new HttpGet("http://localhost:8080//addressbook/contacts/Ella");
		response = httpclient.execute(get);
		
	    assertEquals(200, response.getStatusLine().getStatusCode());
	    
	    // get contacts
		get = new HttpGet("http://localhost:8080//addressbook/contacts");
		response = httpclient.execute(get);
		
	    assertEquals(200, response.getStatusLine().getStatusCode());
	}
}
