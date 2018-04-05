package es.aeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class AsistenteVirtual
 */
@WebServlet("/AsistenteVirtual")
public class AsistenteVirtual extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String conversation_id="";
    private ConfiguracionWatson config=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AsistenteVirtual() {
        super();
		/*
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
		*/
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	private String enviarMensaje(HttpsURLConnection conexion,String mensaje)
	{
		BufferedReader in;
		String peticionJson="{\"input\": {\"text\": \""+mensaje+"\"}}";
		conexion.setDoOutput(true);
		
		try {		
		OutputStreamWriter out = new OutputStreamWriter(conexion.getOutputStream(),"UTF-8");
		out.write(peticionJson);
		out.close();
		StringWriter sw=new StringWriter();


			in = new BufferedReader(new InputStreamReader(conexion.getInputStream(),"UTF-8"));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
			sw.append(inputLine);
			in.close();
			return sw.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}

	}
	private String enviarMensaje(HttpsURLConnection conexion,String mensaje,String contexto)
	{
		BufferedReader in;
		String peticionJson="{\"input\": {\"text\": \""+mensaje+"\"},\"context\":"+contexto+"}";
		conexion.setDoOutput(true);
		//System.out.println(peticionJson);
		try {		
		OutputStreamWriter out = new OutputStreamWriter(conexion.getOutputStream(),"UTF-8");
		out.write(peticionJson);
		out.close();
		StringWriter sw=new StringWriter();


			in = new BufferedReader(new InputStreamReader(conexion.getInputStream(),"UTF-8"));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
			sw.append(inputLine);
			in.close();
			return sw.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}

	}
	private String leerRespuesta(HttpsURLConnection conexion)
	{
		BufferedReader in;
		StringWriter sw=new StringWriter();
		try {
			in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
			sw.append(inputLine);
			in.close();
			return sw.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpsURLConnection conexion;
		try {
			Enumeration<String> params = request.getParameterNames(); 
			while(params.hasMoreElements()){
 				String paramName = params.nextElement();
 				System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
			}
			String ambito=request.getParameter("ambito");
			System.out.println("*********** " + ambito);
	        config=new ConfiguracionWatson(ambito);
			conexion = config.getConexionWatson();

		String mensaje=request.getParameter("mensaje");
		String contexto=request.getParameter("contexto");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();

		if (mensaje.equals(""))
		{
			out.write(enviarMensaje(conexion,""));
			//Mensaje en blanco, nueva conversación, devolvemos conversation_id
		}
		else
		{
			//Mensaje con texto, continuación de conversación
			out.write(enviarMensaje(conexion,mensaje,contexto));
		}
		/*
		 * Si el id de conversación está en blanco es el inicio de una conversación
		 * si tiene un valor es la continuación, y enviamos un mensaje del usuario
		 * */
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
