package com.sergiocarvalho.banklogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpGateway {

	public static String BPILOGINURL = "https://www.bpinet.pt/verificaMCF.asp";
	public static String BPIMOVEMENTSURL = "https://www.bpinet.pt/areaInf/consultas/Movimentos/Movimentos.asp";
	
	
	DefaultHttpClient httpclient = new DefaultHttpClient();
	//Map<String, String> cookies = new HashMap<String, String>();
	CookieStore cookieStore = new BasicCookieStore();
	
	public boolean bpiLogin(String userId, String pass) {
		
		
		HttpPost httpPost = new HttpPost(BPILOGINURL);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("USERID", userId));
		nvps.add(new BasicNameValuePair("password", pass));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response2 = httpclient.execute(httpPost);

			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			System.out.println("Cheguei aqui");
			Header headers[] = response2.getAllHeaders();
			headers = response2.getAllHeaders();
			String[] parts;
			for (Header h : headers) {
				if (h.getName().compareTo("Set-Cookie") == 0) {
					parts = h.getValue().split("=");
					System.out.println("chave:" + parts[0]);
					System.out.println("valor:" + parts[1]);
					System.out.println(h.getName() + ": " + h.getValue());
					cookieStore.addCookie(new BasicClientCookie(parts[0], parts[1]));

				}

			}
			EntityUtils.consume(entity2);
			return true;

		} catch (Exception e) {
			System.err.println("erro:" + e);
			return false;

		} finally {
			httpPost.releaseConnection();
		}
	}
	// TODO
	// https://www.bpinet.pt/areaInf/consultas/Movimentos/Movimentos.asp?from=menu&gif=Movimentos
	public void bpiGetMovimentos()
	{
        HttpContext localContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        try{
        	
        HttpGet httpget = new HttpGet(this.BPIMOVEMENTSURL);
        System.out.println("executing request " + httpget.getURI());

        // Pass local context as a parameter
        HttpResponse response = httpclient.execute(httpget, localContext);
        HttpEntity entity = response.getEntity();
        System.out.println(entity.getContent());
        BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
        String line = null;
        while((line = in.readLine()) != null) {
          System.out.println(line);
        }
    } 
        catch (Exception e)
        {
        	System.err.println("Erro:"+e);
        	System.exit(1);
        }
        finally {
        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();
    }

	}
}
