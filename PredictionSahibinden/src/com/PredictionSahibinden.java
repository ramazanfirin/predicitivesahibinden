package com;
/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.PredictionScopes;
import com.google.api.services.prediction.model.Input;
import com.google.api.services.prediction.model.Input.InputInput;
import com.google.api.services.prediction.model.Output;

/**
 * @author Yaniv Inbar
 */
public class PredictionSahibinden {
	
 // static String privateKey = "D:/calismalar/gurkan/PredictionSahibinden/key.p12";
  static String privateKey = "/mnt/ebs1/key.p12";

  static final String MODEL_ID = "sahibinden_new";
  static final String STORAGE_DATA_LOCATION = "enter_bucket/language_id.txt";
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();


//  private static Credential authorize() throws Exception {
//   
//	  String secretFile ="D:/calismalar/gurkan/PredictionSahibinden/client_secret.json ";
//	  Reader clientSecretReader = new InputStreamReader(new FileInputStream(secretFile));  
//	  GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);
//      FileCredentialStore credentialStore = new FileCredentialStore(new File(System.getProperty("user.home"), ".credentials/prediction.json"), JSON_FACTORY);
//      GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//    		  HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singleton(PredictionScopes.PREDICTION)).setCredentialStore(credentialStore)
//    		  .build();
//    
//     return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//  }
//  
  
  
  public static GoogleCredential getCredencial() throws Exception{
	  URL url = PredictionSahibinden.class.getResource("key.p12");
  
	  GoogleCredential credential = new GoogleCredential.Builder()
			    .setTransport(HTTP_TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId("160087228600-j055cknmb6jbks7ma92bjejl2ig98gdo@developer.gserviceaccount.com")
				.setServiceAccountScopes(Collections.singleton(PredictionScopes.PREDICTION))
				.setServiceAccountPrivateKeyFromP12File(new File(privateKey)).build();
	 
	  return credential;
  }

  private static String run(List<Object> list ) throws Exception {
    
    Credential credential = getCredencial();
    Prediction prediction = new Prediction.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("cloudStorqge").build();

    System.out.println(prediction.trainedmodels().list("nth-suprstate-560").size());
    analyze(prediction);
    return predict(prediction, list);
    
  }

//  private static void train(Prediction prediction) throws IOException {
//    Training training = new Training();
//    training.setId(MODEL_ID);
//    training.setStorageDataLocation(STORAGE_DATA_LOCATION);
//    prediction.trainedmodels().insert(training).execute();
//    System.out.println("Training started.");
//    System.out.print("Waiting for training to complete");
//    System.out.flush();
//
//    int triesCounter = 0;
//    while (triesCounter < 100) {
//      // NOTE: if model not found, it will throw an HttpResponseException with a 404 error
//      try {
//        HttpResponse response = prediction.trainedmodels().get(MODEL_ID).executeUnparsed();
//        if (response.getStatusCode() == 200) {
//          training = response.parseAs(Training.class);
//          String trainingStatus = training.getTrainingStatus();
//          if (trainingStatus.equals("DONE")) {
//            System.out.println();
//            System.out.println("Training completed.");
//            System.out.println(training.getModelInfo());
//            return;
//          }
//        }
//        response.ignore();
//      } catch (HttpResponseException e) {
//      }
//
//      try {
//        // 5 seconds times the tries counter
//        Thread.sleep(5000 * (triesCounter + 1));
//      } catch (InterruptedException e) {
//        break;
//      }
//      System.out.print(".");
//      System.out.flush();
//      triesCounter++;
//    }
//    error("ERROR: training not completed.");
//  }

  private static void error(String errorMessage) {
    System.err.println();
    System.err.println(errorMessage);
    System.exit(1);
  }
  
  private static void analyze(Prediction prediction) throws IOException{
	  prediction.trainedmodels().analyze("nth-suprstate-560","sahibinden").execute();
	  System.out.println("analiz bitti");
	  
  }

  private static String predict(Prediction prediction, List<Object> list) throws IOException {
    Input input = new Input();
    InputInput inputInput = new InputInput();
    inputInput.setCsvInstance(list);
    input.setInput(inputInput);
    Output output = prediction.trainedmodels().predict("nth-suprstate-560",MODEL_ID, input).execute();
 //   System.out.println("Text: " + text);output.getOutputValue();
    System.out.println("Predicted language: " + output.getOutputValue());
    return output.getOutputValue();
  }

  public static String makePrediction(List<Object> list){
	  try {
		 System.out.println(MODEL_ID+"-----query parametreleri ----");
		  for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			System.out.println(object);
			
		}
		  System.out.println("-----query parametreleri ----");
		return run(list);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		 return "error";
	}
	 
  }
  
  public static void main(String[] args) {
    try {
      try {
    	  List<Object> list = new ArrayList<Object>() ;
    	  list.add("Istanbul");
           run(list);
        // success!
        return;
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
}
