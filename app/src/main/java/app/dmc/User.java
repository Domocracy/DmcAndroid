///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Joscormir
//         Date:    2015-MAR-02
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.dmc.core.Persistence;
import app.dmc.devices.Device;
import app.dmc.user_interface.UserInterface;

/** User singleton class. This class is responsible of loading and saving all data related to user and user's devices and rooms.
 *
 */
public class User {
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Static initialization of class
     *
     *  Instance User from giving user Id and with the main activity.
     *
     * @param _userID
     * @param _activity
     */
    public static void init(String _userID, ActionBarActivity _activity){
        assert sInstance == null;
        sInstance = new User(_userID, _activity);
    }


	//-----------------------------------------------------------------------------------------------------------------
    /** \brief finish current instance of user.
     *
     */
	public static void end() {
		sInstance.onEnd();
		sInstance = null;
	}

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get current user instance
     *
     * @return
     */
    public static User get(){
        return sInstance;
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get id of current user
     *
     * @return
     */
    public String id(){ return mId; }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get the list of hubs id that the current user has.
     *
     * @return
     */
    public List<String> getHubIDList(){
        return mHubIds;
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get the hub that is currently been used by the user.
     *
     * @return
     */
    public Hub getCurrentHub(){
        return mLastHub;
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Change current hub to the requested one given by Hub Id.
     *
     * @param _hubId
     */
    public void setHub(String _hubId){
       mLastHub = HubManager.get().hub(_hubId);
       UserInterface.get().onSetHub(mLastHub);
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief  Add a new room to the current hub.
     *
     * @param _roomInfo
     * @param _context
     */

    public void addRoom(JSONObject _roomInfo, Context _context){
        Room room = new Room(_roomInfo, getCurrentHub(), _context);
        getCurrentHub().addRoom(room);
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Add a new device to the current hub and the current room.
     *
     * @param _deviceInfo
     * @return
     */

    public Device addNewDevice(JSONObject _deviceInfo){
        // Register new device on DevMgr
        return getCurrentHub().registerDevice(_deviceInfo);
    }
    //-----------------------------------------------------------------------------------------------------------------
    //Private interface
    private User(String _userId, ActionBarActivity _activity){
		mHubIds = new ArrayList<>();
        HubManager.init(_activity);
		JSONObject userData = Persistence.get().getJSON( _userId );
		try {
			String lastHubId = userData.getString("lastHub");
			mLastHub = HubManager.get().hub(lastHubId);
			JSONArray hubList = userData.getJSONArray("hubs");
			for(int i = 0; i < hubList.length(); ++i){
				mHubIds.add(hubList.getString(i));
			}
            mId = _userId;
        }catch(JSONException e){
            e.printStackTrace();
        }
		UserInterface.init(_activity, this);
    }

	//-----------------------------------------------------------------------------------------------------------------
	public void setRoom(String _roomId) {
		mLastHub.changeRoom(_roomId);
		UserInterface.get().setRoom(_roomId);
	}

	//-----------------------------------------------------------------------------------------------------------------
	private void onEnd() {
		mLastHub = null;
		HubManager.end();
	}

    //-----------------------------------------------------------------------------------------------------------------
    private String mId;

    //-----------------------------------------------------------------------------------------------------------------
    private static  User                sInstance = null;
    private static  Hub                 mLastHub;
    private static  List<String>        mHubIds;
}
