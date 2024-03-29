///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//         Author: Pablo R.S.
//         Date:    2015-FEB-12
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//


package app.dmc.comm;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JsonRequest {
    //-----------------------------------------------------------------------------------------------------------------
    // Public Interface
    /** \brief Constructor. Create a JSON request with the given URL. This request hasn't got of Headers, Methods and body.
     *
     * @param _url
     */
    public JsonRequest(String _url){
        try {
            // Create new URL. This class check if string is properly written, and can create a connection from it.
            mUrl = new URL(_url);

            // Open a connection with the given URL.
            mConnection = (HttpURLConnection) mUrl.openConnection();
            if (mConnection == null) {
                Log.d("DOMOCRACY", "Fail opening URL: " + _url);
                return;
            }

        }catch (MalformedURLException eMalformed){
            eMalformed.printStackTrace();
        }catch (IOException eIOException){
            eIOException.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Set the method to the request
     *
     * @param _method
     */
    public void setMethod(String _method){
        try {
            mConnection.setRequestMethod(_method);
        }catch(ProtocolException _protocolException){
            _protocolException.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Add the given header to the request
     *
     * @param _key
     * @param _value
     */
    public void setHeader(String _key, String _value){
        mConnection.setRequestProperty(_key, _value);
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Set the given string as request's body
     *
     * @param _body
     */
    public void setBody(String _body){
        mConnection.setDoOutput(true);
        mConnection.setRequestProperty("Content-Length", Integer.toString(_body.length()));
        try {
            OutputStream os = mConnection.getOutputStream();
            os.write(_body.getBytes("UTF8"));
            os.close();
        }catch (IOException _ioException){
            Log.d("DOMOCRACY", "Couldn't add body to request");
            _ioException.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Send request.
     *
     * @return
     */
    public JSONObject sendRequest(){
        JSONObject response = null;
        try {
            int responseCode = mConnection.getResponseCode();
            System.out.println("\nSending 'PUT' request to URL : " + mUrl);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader( new InputStreamReader(mConnection.getInputStream()));
            String inputLine;
            StringBuffer responseString = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseString.append(inputLine);
            }
            in.close();

            response = new JSONObject(responseString.toString());
            //print result
            //System.out.println(responseString.toString());
        }catch (IOException _ioException){
            _ioException.printStackTrace();
        } catch (JSONException _jsonException){
            _jsonException.printStackTrace();
        }

        return response;
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Private Interface

    //-----------------------------------------------------------------------------------------------------------------
    // Net
    HttpURLConnection   mConnection = null;
    URL                 mUrl = null;
}
