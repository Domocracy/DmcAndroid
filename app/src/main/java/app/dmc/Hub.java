///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Pablo R.S, Jose Enrique Corchado Miralles
//         Date:    2015-FEB-11
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
package app.dmc;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.dmc.comm.HubConnection;
import app.dmc.core.Persistence;
import app.dmc.devices.Device;
import app.dmc.devices.DeviceManager;

/** This class is responsible of managing hub instances (Loading from data, storing info, saving it and modify it). It
 *  also deal with it's connections.
 *
 */

public class Hub {
    //-----------------------------------------------------------------------------------------------------------------
    //  Public Interface
    /** \brief Hub's constructor. Decode hub and instance it from given Id.
     *
     * @param _id
     * @param _context
     */
    public Hub(String _id, Context _context) {
        mConnection = new HubConnection();
		mId = _id;
		JSONObject hubData = Persistence.get().getJSON("hub_"+_id);

		try {
			mName = hubData.getString(NAME);
			mIp = hubData.getString(IP);
			mRooms = hubData.getJSONArray(ROOMS);
            mLastRoomId = hubData.getString(LAST_ROOM);

			mDevMgr = new DeviceManager(hubData.getJSONArray(DEVICES));
			mRoomList = new ArrayList<>();
			for(int i = 0; i < mRooms.length() ; i++){
				mRoomList.add( new Room(mRooms.getJSONObject(i), this, _context));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get a device instance from a given Id.
     *
     * @param _id
     * @return
     */

    public Device device(int _id) {
        return mDevMgr.device(_id);
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Register new device from given data.
     *
     * @param _deviceInfo
     * @return
     */

    public Device registerDevice(JSONObject _deviceInfo){
        return mDevMgr.register(_deviceInfo);
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Query list of devices Id.
     *
     * @return
     */
    public List<Integer> deviceIds(){
        return mDevMgr.deviceIds();
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get room instance from given Id.
     *
     * @param _id
     * @return
     */

    public Room room(String _id){
        for(int i = 0 ; i < mRoomList.size() ; i++) {
            if(mRoomList.get(i).id().equals(_id))
                return mRoomList.get(i);
        }
        return null;
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get list of rooms.
     *
     * @return
     */

    public List<Room> rooms(){
        return mRoomList;
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get current room Id.
     *
     * @return
     */
    public String currentRoom() { return mLastRoomId; }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Change current room to another with the given Id.
     *
     * @param _roomId
     */

    public void changeRoom(String _roomId){ mLastRoomId = _roomId; }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Add new room to Hub.
     *
     * @param _room
     */

    public void addRoom(Room _room){
        mRoomList.add(_room);
    }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get Hub's name
     *
     * @return
     */

    public String name(){ return mName; }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get Hub's id
     *
     * @return
     */

    public String id() { return mId; }
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get Hub's ip.
     *
     * @return
     */

    public String ip() { return mIp; }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Send PUT request to the hub with the given body.
     *
     * @param _url
     * @param _body
     * @return
     */

    public JSONObject send(final String _url, final JSONObject _body) {
        String url = "http://" + ip() + "/user/" + User.get().id() + _url;
        return mConnection.send(url, _body);
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Send GET request to the hub.
     *
     * @param _url
     * @return
     */

    public JSONObject get(final String _url) {
        String url = "http://" + ip() + "/user/dmc64" + _url;
        return mConnection.get(url);
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Modify hub's ip.
     *
     * @param _ip
     * @return
     */

    public boolean modifyIp(String _ip) {
        if (!_ip.equals(mIp)) {
            mIp = _ip;
            return true;
        } else return false;
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Save current hub's info in internal memory
     *
     */

    public void save(){
        JSONObject jsonToSave = new JSONObject();
        try {
            jsonToSave.put(HUB_ID, mId);
            jsonToSave.put(NAME, mName);
            jsonToSave.put(DEVICES,mDevMgr.serializeDevices());
			jsonToSave.put(LAST_ROOM, mLastRoomId);
            jsonToSave.put(IP, mIp);
            JSONArray roomList = new JSONArray();
            for(Room room : mRoomList){
                roomList.put(room.serialize());
            }
            jsonToSave.put(ROOMS,roomList);
        }catch(JSONException e){
            e.printStackTrace();
        }
        Persistence.get().putJSON("hub_" + mId, jsonToSave);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Identification
    private String          mIp;
    private String          mId;
    private String          mName;
    private JSONArray       mRooms;
    private String			mLastRoomId;

    private List<Room>      mRoomList;
    private DeviceManager   mDevMgr = null;
    private HubConnection   mConnection = null;

	//-----------------------------------------------------------------------------------------------------------------
	// Constants
	final static String LAST_ROOM = "lastRoom";
	final static String HUB_ID = "hubId";
	final static String NAME = "name";
	final static String DEVICES = "devices";
	final static String ROOMS = "rooms";
	final static String IP = "ip";
}
