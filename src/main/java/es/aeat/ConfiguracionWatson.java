package es.aeat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.naming.InitialContext;





public class ConfiguracionWatson {
private String workspaceId="";
private String user="";
private String pass="";
private String llamada="";
private String endpoint="";
private Properties prop=null;
private String ambito="";
private String dbUser="";
private String dbPass="";
private String jdbcStr="";
private String contexto="";
private	DataSource myDS;
public String getContexto()
{
	return contexto;
}
public void setContexto(String context)
{
	contexto=context;
}
public String getConversationId()
{
	return "";
}

public ConfiguracionWatson(String amb)
{
    /*
	prop = new Properties();
	InputStream input = null;
	try {
		input=new FileInputStream(System.getProperty("shared.config.dir")+"asistente.properties");
		prop.load(input);
		ambito=amb;
		jdbcStr=prop.getProperty("jdbc");
		dbUser=prop.getProperty("usr");
		dbPass=prop.getProperty("pass");
	*/
		cargarPropiedades(amb);
	/*	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		prop=null;
		e.printStackTrace();
	}
	*/
}
public String getWorkspaceId()
{
	return workspaceId;
}
public void cargarPropiedades(String ambito)
{
	try {
		//Class.forName("com.mysql.jdbc.Driver").newInstance();
		System.out.println("select * from configuracion where ambito='"+ambito+"'");
		//Connection conexion =  (Connection) DriverManager.getConnection("jdbc:"+prop.getProperty("jdbc")+"?user="+prop.getProperty("usr")+"&password="+prop.getProperty("pass"));
		DataSource myDS = null;
		InitialContext ic = new InitialContext();
		myDS = (DataSource) ic.lookup("jdbc/DS");
		System.out.println("DS " + myDS);
		Connection conexion =  (Connection) myDS.getConnection();				

		Statement s = (Statement) conexion.createStatement(); 
		ResultSet rs = s.executeQuery ("select * from configuracion where ambito='"+ambito+"'");
		System.out.println("select * from configuracion where ambito='"+ambito+"'");
		if (rs.next())
		{
			workspaceId=rs.getString(2);
			user=rs.getString(3);
			pass=rs.getString(4);
			llamada=rs.getString(6);
			endpoint=rs.getString(5);
			System.out.println(endpoint);
		}
		else
		{
			ambito="Error: no existe configuración";
			System.out.println(ambito);
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
private String getUrlLlamada()
{
	return endpoint+workspaceId+llamada;
}
public HttpsURLConnection getConexionWatson() throws KeyManagementException, NoSuchAlgorithmException
{
	String url=getUrlLlamada();
	try {
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}
        }
    };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		URL dir=new URL(url);
		String userpass=user+":"+pass;
		HttpsURLConnection conexion=(HttpsURLConnection) dir.openConnection();
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
		conexion.setRequestProperty ("Authorization", basicAuth);
		conexion.setRequestProperty("Content-Type", "application/json");
		conexion.setRequestProperty("Accept", "application/json");
		conexion.setRequestMethod("POST");
		return conexion;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		return null;
	}
	
}
}
