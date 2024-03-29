package app.dmc.devices;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//         Author: Pablo R.S.
//         Date:    2015-FEB-16
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.dmc.devices.supported_devices.philips_hue.HueLight;
import app.dmc.devices.supported_devices.kodi.Kodi;
import app.dmc.devices.supported_devices.scene.Scene;

/** This class is responsible of loading factories of devices. These factories are basically interfaces to unify device
 *  creation.
 *
 */
public class DeviceFactory {
    //-----------------------------------------------------------------------------------------------------------------
    // Static Interface
    /** \brief Get instance.
     *
     * @return
     */

    static public DeviceFactory get() {
        if(sDevFactory == null)
            sDevFactory = new DeviceFactory();

        return sDevFactory;
    }

    //-----------------------------------------------------------------------------------------------------------------
    //  Public Interface
    /** \brief Create a device of the given type with the given data.
     *
     * @param _type
     * @param _data
     * @return
     */
    public Device create(String _type, JSONObject _data){
        return sFactories.get(_type).create(_data);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Private Interface
    private DeviceFactory() {
        loadFactories();
    }

    private void loadFactories() {
        // HueLight Factory
        sFactories.put("HueLight", new Factory() {
            @Override
            public Device create(JSONObject _data) {
                return new HueLight(_data);
            }
        });

        // Scene Factory
        sFactories.put("Scene", new Factory() {
            @Override
            public Device create(JSONObject _data) {
                return new Scene(_data);
            }
        });

        // Kodi Factory
        sFactories.put("Kodi", new Factory() {
            @Override
            public Device create(JSONObject _data) {
                return new Kodi(_data);
            }
        });

        // Add other Factories
    }


    //-----------------------------------------------------------------------------------------------------------------
    // Members
    static private DeviceFactory sDevFactory = null;
    static private Map<String, Factory> sFactories =  new HashMap<>();

    //-----------------------------------------------------------------------------------------------------------------

    /** \brief Factory interface definition.
     *
     */
    public interface Factory{
        Device create(JSONObject _data);
    }
}
