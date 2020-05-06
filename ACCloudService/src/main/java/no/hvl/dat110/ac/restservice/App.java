package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device: /log, /log/:id, /code");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations described in the project description

		
		//Retur av tilgangs log som JSON, består av alle bruker forsøk
		get("/accessdevice/log/", (req, res) -> accesslog.toJson());
				
			
		//Returnerer JSON av spesefikke log adganger, basert på .../log/:id
		get("/accessdevice/log/:id", (req, res) -> {
			Gson gson = new Gson();						 //Oppretter GSON objekt
			int id = Integer.parseInt(req.params(":id"));//henter ut id parameter verdi sendt i HTTP request
			return gson.toJson(accesslog.get(id));       //Retur av adgangsforsøk som JSON basert på id
		});		
		
		//Legger inn et tilgangsforsøk i loggen
		post("/accessdevice/log/", (req, res) -> {
			Gson gson = new Gson();											     //oppretter GSON
			AccessMessage msg = gson.fromJson(req.body(), AccessMessage.class); //gjør om msg til gson
			int id = accesslog.add(msg.getMessage());						    //legger til msg i accesslog og returnerer innsettings id
			return gson.toJson(accesslog.get(id));                              //gjør om int id til JSON ra gson
		});
		
		// deletes all entries in the access log and returns empty log
		//sletter loggen og gir en tom log i retur
		delete("/accessdevice/log/", (req, res) -> {
			accesslog.clear();
			return accesslog.toJson();
		});
		
		//Bruker HTTP PUT for å oppdatere tilgangs-koden
		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();										  //Oppretter GSON
			AccessCode code = gson.fromJson(req.body(), AccessCode.class);//Henter ut kode fra HTTP request-body, gjør om inngangs-kode GSON
			accesscode.setAccesscode(code.getAccesscode());				 //setter ny access code
			return gson.toJson(accesscode);								 //Retur av ny accesscode som JSON
		});

		//eturnerer tilgangskoden som ble sendt med PUT
		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			return gson.toJson(accesscode);
		});
		
    }
    
}