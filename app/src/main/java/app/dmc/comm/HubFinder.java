///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Pablo R.S.
//         Date:    2015-MAR-10
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc.comm;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.util.Pair;

import java.net.InetAddress;

/** This class look for DMC's hub in the local network. Possible deprecated due to gateway IP.
 *  WARNING: need API level 16 min
 */
public class HubFinder {
    final public static String DOMOCRACY_HUB_SERVICE = "domocracy";
    final public static String DOMOCRACY_HUB_SERVICE_TYPE = "_dmc._tcp";
    // ----------------------------------------------------------------------------------------------------------------
    // Public Interface
    /** Contructor.
     *
     * @param _context
     */
    public HubFinder(Context _context) {
        mDiscoveryListener = new NSDdiscovery();
        mResolveListener = new NSDresolver();

        mNsdManager = (NsdManager) _context.getSystemService(Context.NSD_SERVICE);
    }

    // ----------------------------------------------------------------------------------------------------------------
    /** Retreive information to connect to hub.
     *
     * @return pair
     */
    public Pair<InetAddress, Integer> hubConnectionInfo(){
        return new Pair<InetAddress, Integer>(mHost, mPort);
    }


    // ----------------------------------------------------------------------------------------------------------------
    // Private Interface
    /** Start hub discovering
     *
     */
    public void lookForHub() {
        mNsdManager.discoverServices(DOMOCRACY_HUB_SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    // ----------------------------------------------------------------------------------------------------------------
    /** Stop hub discovering
     *
     */
    public void stopLookingFor() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    // ----------------------------------------------------------------------------------------------------------------
    // Private members
    private NsdManager mNsdManager = null;
    private NsdManager.DiscoveryListener mDiscoveryListener = null;
    private NsdManager.ResolveListener mResolveListener = null;

    private NsdServiceInfo mServiceInfo = null;
    private int mPort = -1;
    private InetAddress mHost = null;

    private final int TIMEOUT = 10000;	// 1000 ms.


    // ----------------------------------------------------------------------------------------------------------------
    // Inner classes
    /************************************************************************************
     * Custom Service Resolve Listener
     * **********************************************************************************/
    private class NSDresolver implements NsdManager.ResolveListener{
        // -----------------------------------------------------------------------------------
        @Override
        public void onResolveFailed(NsdServiceInfo _serviceInfo, int _errorCode) {
            Log.d("DMC", "Resolved failed, error code: " + _errorCode);

        }

        // -----------------------------------------------------------------------------------
        @Override
        public void onServiceResolved(NsdServiceInfo _serviceInfo) {
            Log.d("DMC", "Resolved succeeded: " + _serviceInfo);

            mServiceInfo = _serviceInfo;
            mPort = mServiceInfo.getPort();
            mHost = mServiceInfo.getHost();

            Log.d("DMC", "Detected hub with addr:" + mHost + " and port: " + mPort);
        }
    }

    /************************************************************************************
     * Custom NSD Discovery Listener
     * **********************************************************************************/
    private class NSDdiscovery implements NsdManager.DiscoveryListener{
        // -----------------------------------------------------------------------------------
        @Override
        public void onDiscoveryStarted(String _regType) {
            Log.d("DMC", "Started NSD");
        }
        // -----------------------------------------------------------------------------------
        @Override
        public void onDiscoveryStopped(String _serviceType) {
            Log.d("DMC", "Stopped NSD");

        }
        // -----------------------------------------------------------------------------------
        @Override
        public void onServiceFound(NsdServiceInfo _service) {
            // Found Service
            Log.d("DMC", "Service discovery success with name: " + _service.getServiceName() + " and type: " + _service.getServiceType());
            if (_service.getServiceName().contains(DOMOCRACY_HUB_SERVICE)) {
                // Found Domocracy's service.
                mNsdManager.resolveService(_service, mResolveListener);
            }

        }
        // -----------------------------------------------------------------------------------
        @Override
        public void onServiceLost(NsdServiceInfo _service) {
            Log.d("DMC",
                    "The service is not longer available on the network");
        }
        // -----------------------------------------------------------------------------------
        @Override
        public void onStartDiscoveryFailed(String _serviceType, int _errorCode) {
            Log.d("DMC", "Discovery failed: Error code: " + _errorCode);
            mNsdManager.stopServiceDiscovery(this);

        }
        // -----------------------------------------------------------------------------------
        @Override
        public void onStopDiscoveryFailed(String _serviceType,int _errorCode) {
            Log.d("DMC", "Discovery failed: Error code: " + _errorCode);
            mNsdManager.stopServiceDiscovery(this);

        }
    }

}
