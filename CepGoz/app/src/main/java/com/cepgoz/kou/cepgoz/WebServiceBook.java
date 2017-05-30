package com.cepgoz.kou.cepgoz;

import android.os.StrictMode;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by faruk on 10.6.2016.
 */
public class WebServiceBook {

    public static String getAllBooks(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CelsiusToFahrenheit();
        String METHOD_NAME = "getAllBooks";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/getAllBooks";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            String resultData = result.toString();
            return resultData;
        } catch (XmlPullParserException e) {
            return e.getMessage();
        } catch (SoapFault soapFault) {
            return soapFault.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public static void insertNewBook(String bookName,String writerName,String urlBook){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String METHOD_NAME = "insertNewBook";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/insertNewBook";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("bookName", bookName);
            request.addProperty("url",urlBook);
            request.addProperty("writerName", writerName);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);

        } catch (XmlPullParserException e) {
        } catch (SoapFault soapFault) {
        } catch (IOException e) {
        }
    }

    public static String getAllNewsPapers(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CelsiusToFahrenheit();
        String METHOD_NAME = "getGazeteOrMakale";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/getGazeteOrMakale";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            String resultData = result.toString();
            return resultData;
        } catch (XmlPullParserException e) {
            return e.getMessage();
        } catch (SoapFault soapFault) {
            return soapFault.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }


    }

    public static void insertNewGazete(String entryName,String urlEntry){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String METHOD_NAME = "insertNewGazeteOrMakale";
        String NAMESPACE = "http://tempuri.org/";
        String SOAP_ACTION = "http://tempuri.org/insertNewGazeteOrMakale";
        String URL = "http://farukkaradeniz.com/WebService1.asmx";
        int SOAP_VERSION = SoapEnvelope.VER11;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("entryName", entryName);
            request.addProperty("Url",urlEntry);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);

        } catch (XmlPullParserException e) {
        } catch (SoapFault soapFault) {
        } catch (IOException e) {
        }
    }
}
