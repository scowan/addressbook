package addressbook.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String URI = "uri";
	private static final String NAME = "name";

	private static JSONArray contacts = new JSONArray();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int status = 404;
		String response = "";

		String[] pathSegments = getPathSegments(req);
		if (pathSegments.length == 0) {
			status = 200;
			response = contacts.toString();
		} else if (pathSegments.length == 1) {
			String name = pathSegments[0];
			JSONObject contact = findContact(name);
			if (contact != null) {
				status = 200;
				response = contact.toString();
				// response = XML.toString(contact);
			}
		}
		resp.setStatus(status);
		resp.getOutputStream().print(response);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		contacts = new JSONArray();
		resp.setStatus(200);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int status = 201;
		String response = "";

		JSONObject jsonObject = null;
		try {
			jsonObject = readJSONObject(req);
		} catch (JSONException e) {
			status = 400;
			response = "Error parsing request body JSON";
		} catch (Exception e) {
			status = 400;
			response = "Error reading request body";
		}

		if (jsonObject != null) {
			String name = (String) jsonObject.get(NAME);
			if (name != null && !name.isEmpty()) {
				if (findContact(name) == null) {
					String uri = getURI(req) + "/" + name;
					jsonObject.put(URI, uri);
					contacts.put(contacts.length(), jsonObject);
					status = 201;
					response = jsonObject.toString();
					// response = XML.toString(contact);
				} else {
					status = 400;
					response = "Contact already exists named " + name;
				}
			} else {
				status = 400;
				response = "Request body JSON requires a name";
			}
		}

		resp.setStatus(status);
		resp.getOutputStream().print(response);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(req, resp);
	}

	private String[] getPathSegments(HttpServletRequest req) {
		String[] pathSegments = new String[] {};
		String pathInfo = req.getPathInfo();
		if (pathInfo != null) {
			pathSegments = pathInfo.substring(1).split("/");
		}
		return pathSegments;
	}

	private JSONObject findContact(String name) {
		JSONObject contact = null;
		if (name != null) {
			for (int i = 0; i < contacts.length(); i++) {
				JSONObject aContact = contacts.getJSONObject(i);
				if (name.equals(aContact.get(NAME))) {
					contact = aContact;
					break;
				}
			}
		}
		return contact;
	}

	private String getURI(HttpServletRequest req) {

		int serverPort = req.getServerPort();
		String pathInfo = req.getPathInfo();

		StringBuffer url = new StringBuffer();
		url.append(req.getScheme()).append("://").append(req.getServerName());

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}

		url.append(req.getContextPath()).append(req.getServletPath());

		if (pathInfo != null) {
			url.append(pathInfo);
		}
		return url.toString();
	}

	private JSONObject readJSONObject(HttpServletRequest req)
			throws IOException {
		StringBuffer jb = new StringBuffer();
		String line = null;
		BufferedReader reader = req.getReader();
		while ((line = reader.readLine()) != null) {
			jb.append(line);
		}
		return new JSONObject(jb.toString());
	}
}
