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
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations described in in the project description
		// record an access attempt by storing the log-message
				post("/accessdevice/log/", (req, res) -> {
					Gson gson = new Gson();
					AccessMessage msg = gson.fromJson(req.body(), AccessMessage.class);
					int id = accesslog.add(msg.getMessage());

					return gson.toJson(accesslog.get(id));
				});

				// returns a JSON-representation of all access log entries
				get("/accessdevice/log/", (req, res) -> accesslog.toJson());


				// returns a JSON-representation of the access log entries of the id
				get("/accessdevice/log/:id", (req, res) -> {
					Gson gson = new Gson();
					int id = Integer.parseInt(req.params(":id"));

					return gson.toJson(accesslog.get(id));
				});

				// updates the access code in the cloud service
				put("/accessdevice/code", (req, res) -> {
					Gson gson = new Gson();
					AccessCode code = gson.fromJson(req.body(), AccessCode.class);
					accesscode.setAccesscode(code.getAccesscode());

					return gson.toJson(accesscode);
				});

				// returns current access code
				get("/accessdevice/code", (req, res) -> {
					Gson gson = new Gson();

					return gson.toJson(accesscode);
				});

				// deletes all entries in the access log and returns empty log
				delete("/accessdevice/log/", (req, res) -> {
					accesslog.clear();

					return accesslog.toJson();
				});
				
		    }
    
}
