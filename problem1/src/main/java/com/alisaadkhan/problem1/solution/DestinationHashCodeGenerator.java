package com.alisaadkhan.problem1.solution;

import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Random;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DestinationHashCodeGenerator {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN> <jsonFilePath>");
			return;
		}

		String prn = args[0].toLowerCase();
		String jsonFilePath = args[1];

		JSONObject jsonObject = new JSONObject(new JSONTokener(new FileReader(jsonFilePath)));

		String destinationValue = findDestination(jsonObject);
		if (destinationValue == null) {
			System.out.println("No 'destination' key found in the JSON file.");
			return;
		}

		String randomString = generateRandomString(8);

		String input = prn + destinationValue + randomString;

		String md5Hash = generateMD5Hash(input);

		// Output the result
		System.out.println(md5Hash + ";" + randomString);
	}

	private static String findDestination(JSONObject jsonObject) {
		for (String key : jsonObject.keySet()) {
			Object value = jsonObject.get(key);
			if (key.equals("destination")) {
				return value.toString();
			} else if (value instanceof JSONObject) {
				String result = findDestination((JSONObject) value);
				if (result != null)
					return result;
			}
		}
		return null;
	}

	private static String generateRandomString(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	private static String generateMD5Hash(String input) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hashInBytes = md.digest(input.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte b : hashInBytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
