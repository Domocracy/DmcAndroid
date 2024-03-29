///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//         Author: Pablo R.S.
//         Date:    2015-FEB-10
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc.devices;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.dmc.Hub;
import app.dmc.HubManager;

/** Base class with common members and methods related to devices.
 *
 */
public abstract class Device {
    //-----------------------------------------------------------------------------------------------------------------
	/** \brief Construct device with data from JSON.
	 *
	 * @param _devData
	 */
    public Device(JSONObject _devData){
        mRegisteredPanels = new HashSet<>();

        try{
            mId     = _devData.getInt("id");
            mName   = _devData.getString("name");
            mHubId  = _devData.getString("hub");

        }catch (JSONException _exception){
            _exception.printStackTrace();
        }
    }
	//-----------------------------------------------------------------------------------------------------------------
	/**	\brief Get device name.
	 *
	 * @return
	 */
    public String name(){ return mName; };

	//-----------------------------------------------------------------------------------------------------------------
	/**	\brief Get device id.
	 *
	 * @return
	 */
    public int id()     { return mId; };

	//-----------------------------------------------------------------------------------------------------------------
	/** \brief Get hub id.
	 *
	 * @return
	 */
    public String hub() {return mHubId;};

	//-----------------------------------------------------------------------------------------------------------------
	/**	\brief This method generate an http request object from a json o and send to hub.
	 *
	 * @param _request
	 */
	final public void runCommand(final JSONObject _request) {
		if(_request == null)
			return;
		Thread commThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Hub hub = HubManager.get().hub(hub());
				try {
                    JSONObject response = null;
					String method = _request.getString("method");
					if(method.equals("GET")) {
						response = hub.get("/device/" + id() + "/" + _request.getString("urlget"));
                        if(response != null && response.getString("result").equals("ok")) {
                            sendNotifications(response);
                        }else{
                            Log.e("Device", "Error: Received a \"result\":\"error\" from Hub\n");
                        }
					}
					if(method.equals("PUT")) {
						JSONObject command = _request.getJSONObject("cmd");
						response = hub.send("/device/" + id(), command);
						if(response != null && response.getString("result").equals("ok")) {
							sendNotifications(command);
						} else {
							Log.e("Device", "Error: Received a \"result\":\"error\" from Hub\n");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		commThread.start();
	}

	//-----------------------------------------------------------------------------------------------------------------
	private void sendNotifications(JSONObject _notification) {
		onStateChange(_notification);
		notifyPanels(_notification);
	}

	//-----------------------------------------------------------------------------------------------------------------
	final public void setState(JSONObject _state) {
		if(_state == null)
			return;
		JSONObject command = new JSONObject();
		try {
			command.put("method", "PUT");
			command.put("cmd", _state);
		} catch (Exception e) {
			e.printStackTrace();
		}
		runCommand(command);
	}

	//-----------------------------------------------------------------------------------------------------------------
	public JSONObject state() {
		return new JSONObject();
	}

	//-----------------------------------------------------------------------------------------------------------------
	public void onStateChange(JSONObject _state) {
		//
	}

    //-----------------------------------------------------------------------------------------------------------------
    public abstract List<Pair<String, Boolean>> panelTypes();

    //-----------------------------------------------------------------------------------------------------------------
    public abstract DevicePanel createPanel(String _type, Context _context);

    //-----------------------------------------------------------------------------------------------------------------
    final public DevicePanel newPanel(String _type, Context _context){
        DevicePanel panel = createPanel(_type, _context);
        mRegisteredPanels.add(panel);
        return panel;
    }

    //-----------------------------------------------------------------------------------------------------------------
    public void unregisterPanel(DevicePanel _panel){
        if(mRegisteredPanels.contains(_panel))
            mRegisteredPanels.remove(_panel);
    }

    //-----------------------------------------------------------------------------------------------------------------
    public void onUpdateState(JSONObject _state){
        // Intentionally blank.
    }

    //-----------------------------------------------------------------------------------------------------------------
    final public void updateState(JSONObject _state) {
        onUpdateState(_state);
		notifyPanels(_state);
    }

    public void changeName(String _name){
        mName = _name;
        for(DevicePanel panel :mRegisteredPanels){
            panel.updateName();
        }
    }

	//-----------------------------------------------------------------------------------------------------------------
	protected final void notifyPanels(JSONObject _state) {
		for(DevicePanel panel : mRegisteredPanels){
			panel.onStateChange(_state);
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	protected JSONObject serialize() {
		JSONObject serial = new JSONObject();
		try{
			serial.put("id", mId);
			serial.put("name", mName);
			serial.put("hub", mHubId);

		}catch (JSONException _exception){
			_exception.printStackTrace();
		}
		return serial;
	}

    //-----------------------------------------------------------------------------------------------------------------
    private String mName;
    private int     mId;
    private String mHubId;

    private Set<DevicePanel> mRegisteredPanels;
}
