///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Pablo R.S.
//         Date:    2015-FEB-10
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc.user_interface;

import android.support.v7.app.ActionBarActivity;

import app.dmc.Hub;
import app.dmc.R;
import app.dmc.User;

/** Singleton that manage user interface.
*
*
*/
public class UserInterface {
    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Init instance
    *
    *
    */
    public static void init(ActionBarActivity _activity, User _user){
        assert sInstance == null;
        sInstance = new UserInterface(_activity, _user);
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Get instance
    *
    *
    */
    public static UserInterface get(){
        return sInstance;
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Set given hub as current hub.
    *
    *
    */
    public void onSetHub(Hub _hub){
        mMainScreen.setHub(mActivity, _hub);
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Set given room as room in room selector.
    *
    *
    */
    public void setRoom(String _roomId) {
	mMainScreen.setRoom(_roomId);
    }


    //-----------------------------------------------------------------------------------------------------------------
    //  Private Interface
    private UserInterface(ActionBarActivity _activity, User _user){
        _activity.setContentView(R.layout.activity_main);
        mActivity = _activity;
        mMainScreen = new MainScreen(_activity, _user.getCurrentHub());
        mLeftSideMenu = new SlideMenu(_activity, _user.getHubIDList());
    }

    //-----------------------------------------------------------------------------------------------------------------
    private static UserInterface        sInstance = null;

    //Views
    private static MainScreen           mMainScreen;
    private static SlideMenu            mLeftSideMenu;
    private static ActionBarActivity    mActivity;
}
