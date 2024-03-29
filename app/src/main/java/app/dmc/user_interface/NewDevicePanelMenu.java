package app.dmc.user_interface;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Pablo R.S
//         Date:    2015-MAR-30
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

import android.app.AlertDialog;
import android.content.Context;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.dmc.Room;
import app.dmc.User;
import app.dmc.devices.Device;
import app.dmc.devices.DevicePanel;


/** Popup menu that allows user to add new device panels.
*
*
*/
public class NewDevicePanelMenu {
    public NewDevicePanelMenu(Context _context, Room _room){
        mDevListLayout  = new LinearLayout(_context);
        mMenuBuilder    = new AlertDialog.Builder(_context);
        mParentRoom     = _room;

        createDialog(_context);     // Create dialog with current devices
        updateDevices(_context);            // Call update devices to add those ones that were not synch.
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Private methods
    private void createDialog(Context _context) {
        // Load devices and put them into the list
        setContentView(_context);
        mMenuBuilder.setTitle("Choose device to be added:");

        mDeviceDialog = mMenuBuilder.create();
        mDeviceDialog.show();
    }

    private void updateDevices(final Context _context){
        Thread comThread = new Thread(){
            @Override
            public void run() {
                JSONObject response = User.get().getCurrentHub().send("/deviceList", new JSONObject());
                try {
                    JSONArray devices = response.getJSONArray("devices");
                    List<Integer> existingDevices = User.get().getCurrentHub().deviceIds();
                    for(int i = 0; i< devices.length(); i++){
                        final JSONObject devData = devices.getJSONObject(i);
                        if(!existingDevices.contains(devData.getInt("id"))){
                            devData.put("hub", User.get().getCurrentHub().id());
                            User.get().getCurrentHub().registerDevice(devData);    // Register device
                            final int devId = devData.getInt("id");
                            mDevListLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    mDevListLayout.addView(createListItem(_context, devId));
                                }
                            });
                        }
                    }
                }catch (JSONException _jsonException){
                    _jsonException.printStackTrace();
                } catch (NullPointerException _nullPtrException){
                    _nullPtrException.printStackTrace(); // No devices in response
                }
            }
        };
        comThread.start();

    }

    //-----------------------------------------------------------------------------------------------------------------
    private void setContentView(final Context _context){
        mDevListLayout.setOrientation(LinearLayout.VERTICAL);
        List<Integer> devices = User.get().getCurrentHub().deviceIds();
        for(final int id :devices){
            mDevListLayout.addView(createListItem(_context, id));
        }
        mMenuBuilder.setView(mDevListLayout);
    }

    private View createListItem(final Context _context, final int _id){
        TextView tv = new TextView(_context);
        tv.setText(User.get().getCurrentHub().device(_id).name());
        tv.setTextSize(30);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device dev = User.get().getCurrentHub().device(_id);
                List<Pair<String, Boolean>> types = dev.panelTypes();
                if(types.size() == 1){  // If a device has only one type of panel, add it directly
                    mParentRoom.addPanel(dev.newPanel(types.get(0).first, _context));
                    mDeviceDialog.dismiss();
                }else{                  // Else, open a new menu with panels type.
                    PanelsListMenu menu = new PanelsListMenu(_context, mParentRoom, dev);
                }
            }
        });
        return tv;
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Private members
    private AlertDialog.Builder mMenuBuilder;
    private Room                mParentRoom;
    private AlertDialog         mDeviceDialog;
    private LinearLayout         mDevListLayout;

    //-----------------------------------------------------------------------------------------------------------------
    // Inner classes
    class PanelsListMenu{
        public PanelsListMenu(Context _context, Room _room, Device _device){
            mMenuBuilder = new AlertDialog.Builder(_context);
            mParentRoom = _room;
            mDevice = _device;
            mPanels =  new ArrayList<>();

            createDialog(_context);
        }

        //-----------------------------------------------------------------------------------------------------------------
        // Private methods
        private void createDialog(Context _context) {
            // Load devices and put them into the list
            setContentView(_context);
            mMenuBuilder.setTitle("Choose controller to be added:");

            mPanelDialog = mMenuBuilder.create();
            mPanelDialog.show();

        }

        //-----------------------------------------------------------------------------------------------------------------
        private void setContentView(final Context _context){
            List<Pair<String, Boolean>> types = mDevice.panelTypes();
            LinearLayout layout = new LinearLayout(_context);
            layout.setOrientation(LinearLayout.VERTICAL);
            for(int i = 0; i < types.size(); i++){
                final String type = types.get(i).first;
                DevicePanel panel = User.get().getCurrentHub().device(mDevice.id()).newPanel(type, _context);
                panel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            mParentRoom.addPanel(User.get().getCurrentHub().device(mDevice.id()).newPanel(type, _context));
                            mPanelDialog.dismiss();
                            mDeviceDialog.dismiss();
                        }
                        return true;
                    }
                });
                mPanels.add(panel);
                layout.addView(panel);
            }
            mMenuBuilder.setView(layout);
        }

        //-----------------------------------------------------------------------------------------------------------------
        // Private members
        private AlertDialog.Builder mMenuBuilder;
        private Room                mParentRoom;
        private Device              mDevice;
        private ArrayList<DevicePanel> mPanels;
        private AlertDialog         mPanelDialog;
    }

}
