///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Pablo R.S.
//         Date:    2015-FEB-16
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc.comm;

import org.json.JSONObject;

/** Hub inner class that deal with http connections
 *  WARNING: Possible deprecated.
 */

public class HubConnection {
    //-----------------------------------------------------------------------------------------------------------------
    // Public Interface
    public JSONObject send(final String _url, final JSONObject _body){
        JsonRequest request = new JsonRequest(_url);
        request.setMethod("PUT");
        request.setHeader("connection", "close");
        request.setBody(_body.toString());

        return request.sendRequest();
    }
    //-----------------------------------------------------------------------------------------------------------------
    public JSONObject get(final String _url){
        final JsonRequest request = new JsonRequest(_url);
        request.setMethod("GET");
        request.setHeader("connection", "close");

        return request.sendRequest();
    }
    //-----------------------------------------------------------------------------------------------------------------
    // Private Interface
}
