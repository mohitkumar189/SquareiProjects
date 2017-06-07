package com.app.justclap.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.justclap.utils.AppUtils;


public class JsonParser {

	public ArrayList<HashMap<String, String>> parseCategoriesData(
			String jsonString, ArrayList<String> ids) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			if (jObject.getString("success").equalsIgnoreCase("1")) {
				
				JSONArray jArray = jObject.getJSONArray("data");
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject category = jArray.getJSONObject(i);
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("categoryID", category.getString("CatId"));
					hm.put("categoryName", category.getString("catName"));
					hm.put("imageUrl", category.getString("imageUrl"));
					if (ids.contains(category.getString("CatId"))) {
						hm.put("isSelected", "1");
					} else {
						hm.put("isSelected", "0");
					}

					data.add(hm);
					
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> parseMyWorksData(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("myPostedJobs");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("jobId", category.getString("jobID"));
				hm.put("title", category.getString("jobTitle"));
				hm.put("desc", category.getString("jobDescription"));
				hm.put("budget", category.getString("jobPrice"));
				hm.put("jobAddress", category.getString("jobAddress"));
				hm.put("jobStatus", category.getString("jobStatus"));

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> parseBidListData(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("jobBids");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("bidID", category.getString("bidID"));
				hm.put("bidSalutation", category.getString("bidSalutation"));
				hm.put("bidCovernote", category.getString("bidCovernote"));
				hm.put("bidOfferPrice", category.getString("bidOfferPrice"));
				hm.put("bidDateCreated", category.getString("bidDateCreated"));
				hm.put("bidderEmail", category.getString("bidderEmail"));
				hm.put("bidderPhone", category.getString("bidderPhone"));
				hm.put("agencyTitle", category.getString("agencyTitle"));
				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> parseMyJobsData(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("myJobs");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("jobId", category.getString("jobID"));
				hm.put("title", category.getString("jobTitle"));
				hm.put("desc", category.getString("jobDescription"));
				hm.put("budget", category.getString("jobPrice"));
				hm.put("jobStatus", category.getString("jobStatus"));
				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> parseAgencyListData(
			String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("agencyID", category.getString("id"));
				hm.put("name", category.getString("username"));
				hm.put("address", category.getString("location"));
				hm.put("keywords", category.getString("keywords"));
				hm.put("mobile", category.getString("phone"));
				hm.put("agencyRatting", category.getString("rating"));
				hm.put("image_name", category.getString("image_name"));
				hm.put("description", category.getString("description"));
				
				hm.put("lat", category.getString("lat"));
				hm.put("lng", category.getString("lng"));
				
				if(category.has("distance")){
					hm.put("distance", category.getString("distance"));
				}else{
					hm.put("distance", "0.0");
				}
				
				
				hm.put("is_fav", category.getString("is_fav"));
				hm.put("isChecked", "0");
				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> parseJobListData(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("jobsList");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("jobID", category.getString("jobID"));
				hm.put("jobTitle", category.getString("jobTitle"));
				hm.put("jobDescription", category.getString("jobDescription"));
				hm.put("jobPrice", category.getString("jobPrice"));
				hm.put("jobAddress", category.getString("jobAddress"));
				hm.put("jobLatitude", category.getString("jobLatitude"));
				hm.put("jobLongitude", category.getString("jobLongitude"));
				hm.put("jobJobStatus", category.getString("jobJobStatus"));
				hm.put("JobDistance", category.getString("JobDistance"));

				hm.put("userName", category.getString("userName"));
				hm.put("userID", category.getString("userID"));
				hm.put("userEmail", category.getString("userEmail"));
				hm.put("userPhoneNumber", category.getString("userPhoneNumber"));
				hm.put("userAddress", category.getString("userAddress"));
				hm.put("userLatitude", category.getString("userLatitude"));
				hm.put("userLongitude", category.getString("userLongitude"));
				hm.put("createdAgo", category.getString("createdAgo"));
				if (category.has("createdAgoDays")) {
					hm.put("createdAgoDays",
							category.getString("createdAgoDays"));
				} else {
					hm.put("createdAgoDays", "61");
				}
				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> parseAgencyReviewListData(
			String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("agencyReviews");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("review", category.getString("review"));
				hm.put("ratting", category.getString("ratting"));
				hm.put("reviewBy", category.getString("reviewBy"));

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> parseMessageData(String jsonString, String clat, String clong) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("message", category.getString("message"));
				hm.put("id",
						category.getString("id"));
				hm.put("notification_type",
						category.getString("notification_type"));
				hm.put("sender_id", category.getString("sender_id"));
				hm.put("request_id", category.getString("request_id"));
				hm.put("created_on", category.getString("created_on"));
				
				JSONObject sender=category.getJSONObject("sender");
				
				hm.put("username", sender.getString("username"));
				hm.put("phone", sender.getString("phone"));
				hm.put("location", sender.getString("location"));
				hm.put("lng", sender.getString("lng"));
				hm.put("lat", sender.getString("lat"));
				
				
				
				
				hm.put("keywords", sender.getString("keywords"));
				//hm.put("description", sender.getString("description"));
				hm.put("image_name", sender.getString("image_name"));
				
				JSONObject job=category.getJSONObject("job");
				
				hm.put("job_msg", job.getString("job_msg"));
				
				try {
					String dis= AppUtils.getDistance(Double.valueOf(clat), Double.valueOf(clong), Double.valueOf(sender.getString("lat")), Double.valueOf(sender.getString("lng")));
					hm.put("distance", ""+dis);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> myfavouritedata(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("username", category.getString("username"));
				hm.put("email", category.getString("email"));
				hm.put("phone", category.getString("phone"));
				hm.put("location", category.getString("location"));
				hm.put("keywords", category.getString("keywords"));
				hm.put("id", category.getString("id"));
				hm.put("ratting", category.getString("ratting"));
				hm.put("image_name", category.getString("image_name"));
				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}
	public ArrayList<HashMap<String, String>> myreqpending(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("jobID", category.getString("jobID"));
				hm.put("requestMsg", category.getString("requestMsg"));
				hm.put("requestedUserName", category.getString("requestedUserName"));
				hm.put("requestedUserEmail", category.getString("requestedUserEmail"));
				hm.put("requestedUserImage", category.getString("requestedUserImage"));
				hm.put("requestedUserLocation", category.getString("requestedUserLocation"));
				hm.put("requestedUserKey", category.getString("requestedUserKey"));
				hm.put("requestStatus", category.getString("requestStatus"));
				hm.put("requestedUserPhone",
						category.getString("requestedUserPhone"));
				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> myjobdata(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONObject jobj1 = jObject.getJSONObject("data");
			JSONArray jArray = jobj1.getJSONArray("confirmJobs");

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();

				hm.put("jobID", category.getString("jobID"));
				hm.put("jobStatus", category.getString("jobStatus"));
				hm.put("jobToID", category.getString("jobToID"));
				hm.put("jobToUsername", category.getString("jobToUsername"));
				hm.put("jobToEmail", category.getString("jobToEmail"));
				hm.put("jobToPhone", category.getString("jobToPhone"));
				hm.put("jobToLocation", category.getString("jobToLocation"));
				hm.put("jobRequiredOn", category.getString("jobRequiredOn"));
				hm.put("jobMsg", category.getString("jobMsg"));
				hm.put("jobToProfileImage",
						category.getString("jobToProfileImage"));

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> myjobdatapending(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONObject jobj1 = jObject.getJSONObject("data");
			JSONArray jArray = jobj1.getJSONArray("pendingJobs");

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();

				hm.put("jobID", category.getString("jobID"));
				hm.put("jobStatus", category.getString("jobStatus"));
				hm.put("jobToID", category.getString("jobToID"));
				hm.put("jobToUsername", category.getString("jobToUsername"));
				hm.put("jobToEmail", category.getString("jobToEmail"));
				hm.put("jobToPhone", category.getString("jobToPhone"));
				hm.put("jobToLocation", category.getString("jobToLocation"));
				hm.put("jobRequiredOn", category.getString("jobRequiredOn"));
				hm.put("jobMsg", category.getString("jobMsg"));
				hm.put("jobToProfileImage",
						category.getString("jobToProfileImage"));

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> myjobdataignore(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONObject jobj1 = jObject.getJSONObject("data");
			JSONArray jArray = jobj1.getJSONArray("ignoredJobs");

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();

				hm.put("jobID", category.getString("jobID"));
				hm.put("jobStatus", category.getString("jobStatus"));
				hm.put("jobToID", category.getString("jobToID"));
				hm.put("jobToUsername", category.getString("jobToUsername"));
				hm.put("jobToEmail", category.getString("jobToEmail"));
				hm.put("jobToPhone", category.getString("jobToPhone"));
				hm.put("jobToLocation", category.getString("jobToLocation"));
				hm.put("jobRequiredOn", category.getString("jobRequiredOn"));
				hm.put("jobMsg", category.getString("jobMsg"));
				hm.put("jobToProfileImage",
						category.getString("jobToProfileImage"));

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> myjobdatacompleted(
			String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONObject jobj1 = jObject.getJSONObject("data");
			JSONArray jArray = jobj1.getJSONArray("completedJobs");

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();

				hm.put("jobID", category.getString("jobID"));
				hm.put("jobStatus", category.getString("jobStatus"));
				hm.put("jobToID", category.getString("jobToID"));
				hm.put("jobToUsername", category.getString("jobToUsername"));
				hm.put("jobToEmail", category.getString("jobToEmail"));
				hm.put("jobToPhone", category.getString("jobToPhone"));
				hm.put("jobToLocation", category.getString("jobToLocation"));
				hm.put("jobRequiredOn", category.getString("jobRequiredOn"));
				hm.put("jobMsg", category.getString("jobMsg"));
				hm.put("jobToProfileImage",
						category.getString("jobToProfileImage"));

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> usereviewdata(String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("reviewBy", category.getString("reviewBy"));
				hm.put("comment", category.getString("comment"));
				hm.put("ratting", category.getString("ratting"));
				hm.put("image_user", category.getString("image_user"));

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<HashMap<String, String>> userrequest_pending(
			String jsonString) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jArray = jObject.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject category = jArray.getJSONObject(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("requestedUserName",
						category.getString("requestedUserName"));
				hm.put("requestedUserEmail",
						category.getString("requestedUserEmail"));
				hm.put("requestedUserImage",
						category.getString("requestedUserImage"));
				hm.put("requestedUserLocation",
						category.getString("requestedUserLocation"));
				hm.put("requestedUserPhone",
						category.getString("requestedUserPhone"));
				hm.put("requestedUserKey",
						category.getString("requestedUserKey"));

				data.add(hm);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

}
