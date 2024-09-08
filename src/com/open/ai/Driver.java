package com.open.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.open.ai.config.OpenAIKeyConfig;

public class Driver {

	public static void main(String[] args) {
		
		Scanner scan  = new Scanner(System.in);
		StringBuffer userInput = new StringBuffer();
		String text;
		while((text=scan.nextLine()) != null){
			if(text.isEmpty()) {
				break;
			}
			userInput.append(text);
		}
		scan.close();
		System.out.println(chatGPT(userInput.toString()));

	}

	private static String chatGPT(String string) {
		Properties properties = OpenAIKeyConfig.getProperties();
        String apiKey = properties.getProperty("apiKey");
        String gptUrl = properties.getProperty("gptUrl");
        String model = properties.getProperty("model");
		
		try {
			URL url = new URL(gptUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			//Preparing the request to GPT model
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer "+apiKey);
			connection.setRequestProperty("Content-Type","application/json");
			connection.setDoOutput(true);
			String requestBody = "{\"model\":\""+model+"\",\"messages\":[{\"role\":\"system\",\"content\":\""+string+"\"}]}";
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(requestBody);
			writer.flush();
			writer.close();

			//Retrieving the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String input ;
			StringBuffer output = new StringBuffer();
			while((input=reader.readLine())!=null) {
				output.append(input);
			}
			reader.close();
			String regex = "\"content\":\\s*\"([^\"]*)\"";
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(output);

	        if (matcher.find()) {
	            return matcher.group(1);	           
	        } 
			return output.toString().split("\"content\":\"")[0];
			//return (output.toString().split("\"content\":\"")[1].split("\"")[0]).substring(4);
		}catch(IOException e) {
			throw new RuntimeException("Error while calling GPT API",e);
		}
		
	}

}
