///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Joscormir
//         Date:    2015-FEB-13
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;


/** This singleton class is responsible of storing, creating and giving Hubs instances.
 *
 */
public class HubManager {
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Init singleton of Hub manager with given context.
     *
     * @param _context
     */
    static public void init(Context _context){
        sInstance =  new HubManager(_context);
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get instance of HubManager
     *
     * @return
     */
    public static HubManager get(){
        return sInstance;
    }

	//-----------------------------------------------------------------------------------------------------------------
    /** \brief Properly end HubManager.
     *
     */

	public static void end() {
		sInstance.onEnd();
		sInstance = null;
	}

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get Hub from id.
     *
     * @param _id
     * @return
     */

    public Hub hub(String _id){
        Hub hub = mHubMap.get(_id);
		if(hub == null) {
			hub = new Hub(_id, mContext);
			mHubMap.put(_id, hub);
		}
        return hub;
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Private Interface.
    private HubManager(Context _context){
		mContext = _context;
		mHubMap  = new HashMap<String,Hub>();
	}

	//-----------------------------------------------------------------------------------------------------------------
	private void onEnd() {
		for( Map.Entry<String, Hub> hub : mHubMap.entrySet()) {
			hub.getValue().save();
		}
		mHubMap = null;
	}

    //-----------------------------------------------------------------------------------------------------------------
    private Map<String,Hub> mHubMap;
	private Context			mContext;

    //-----------------------------------------------------------------------------------------------------------------
    private static HubManager sInstance = null;

}
