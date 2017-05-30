package com.cepgoz.kou.cepgoz;

import android.os.StrictMode;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Lenovo on 24.1.2016.
 */
public class WebService {

    public static String UpdateNotificationState(int id,int state){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CelsiusToFahrenheit();
        String METHOD_NAME = "setNotificationState";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/setNotificationState";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        //SOAP must be the same version as the webservice.
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("state", state);
            request.addProperty("id", id);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //Object result = envelope.getResponse();
            //String resultData = result.toString();
            return  "İşlem başarılı";
        }catch (Exception e){
            return e.getMessage();
        }

    }

    public static String insertnewuser(int id){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CelsiusToFahrenheit();
        String METHOD_NAME = "insertnewuser";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/insertnewuser";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        //SOAP must be the same version as the webservice.
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("id", id);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //Object result = envelope.getResponse();
            //String resultData = result.toString();
            return  "İşlem başarılı";
        }catch (Exception e){
            return e.getMessage();
        }


    }

    public static String getNotificationState(int id){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CelsiusToFahrenheit();
        String METHOD_NAME = "getNotificationState";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/getNotificationState";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        //SOAP must be the same version as the webservice.
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("id", id);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            String resultData = result.toString();
            return  resultData;
        }catch (Exception e){
            return e.getMessage();
        }
    }

}
