package com.cloudant;

import java.net.MalformedURLException;
import java.util.HashMap;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import com.ibm.iic.FileReader;

public class CrudWithEktorp {

	public CrudRepository cloudantConnect() {
		FileReader fr = new FileReader();
		HashMap<String, String> map = fr.getCloudantMap();
		String user = map.get("user");
		String pass = map.get("pass");
		String db = "iicsupport";
		// create the http connection
		HttpClient httpClient;
		CrudRepository repo = null;
		try {
			httpClient = new StdHttpClient.Builder()
					.url("https://" + user + ".cloudant.com").username(user)
					.password(pass).build();
			// initialize couch instance and connector
			CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
			CouchDbConnector dbc = new StdCouchDbConnector(db, dbInstance);

			// create a new "repo" that allows for built-in CRUD functionality
			repo = new CrudRepository(dbc);
			return repo;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return repo;
	}

	public void addDoc(CrudRepository repo) {
		// the new document id and age
		String id = "John Doe";
		// create a document for use in this example
		CrudDocument doc = new CrudDocument();
		doc.setId(id);
		doc.setName(id);

		// add the doc to the database
		System.out.println("Adding new document to database...");
		repo.add(doc);

		// neat method that returns a boolean on whether the id exists in the
		// database
		if (repo.contains(id))
			System.out.println("Successfully wrote document!");
		else
			System.out.println("FAILED to write document to database!");
	}

	public String readDoc(CrudRepository repo, String sid) {
		String id = sid;
		CrudDocument doc = new CrudDocument();

		// read the document back based on id
		System.out.println("Reading document back from database...");
		doc = (CrudDocument) repo.get(id);
		System.out.println("Revision of the document is: " + doc.getRevision());
		System.out.println("Datetime of the document is: " + doc.getTimedata());
		System.out.println("Name of the document is: " + doc.getName());
		return doc.getRevision();
	}

	public CrudDocument getAllDoc(CrudRepository repo, String sid) {
		String id = sid;
		CrudDocument doc = new CrudDocument();

		// read the document back based on id
		System.out.println("Reading document back from database...");
		doc = repo.get(id);
		return doc;
	}

	public String readMainDoc(MainRepository repo, String sid) {
		String id = sid;
		ConstantDocument doc = new ConstantDocument();
		// read the document back based on id
		System.out.println("Reading document back from database...");
		doc = repo.get(id);
		System.out.println("Main_ID of the document is: " + doc.getMainID());
		if (doc.getFlag().equals("1"))
			return doc.getMainID();
		return "false";
	}

	public void updateDoc(CrudRepository repo, String uid, String tag) {
		CrudDocument doc = new CrudDocument();
		// update the doc and re-commit
		System.out.println("Updating document with new value...");
		doc = repo.get(uid);
		doc.setTag(tag);
		repo.update(doc);
		System.out.println("Wrote updated document to database!");
	}
	
	public void updateMainDoc(MainRepository repo) {
		final String sid = "iicsupport_constant";
		ConstantDocument doc = new ConstantDocument();
		doc = repo.get(sid);
		doc.setMainID("");
		// update the doc and re-commit
		System.out.println("Updating document with new value...");
		doc.setFlag("0");
		repo.update(doc);
		System.out.println("Wrote updated document to database!");

	}

	public static void removeDoc(CrudRepository repo) {
		String id = "John Doe";
		CrudDocument doc = new CrudDocument();
		doc.setId(id);
		// delete the document
		System.out.println("Deleting document from the database...");
		repo.remove(doc);
		System.out.println("Successfully deleted the document!");
	}
}
