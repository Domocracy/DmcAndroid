///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Pablo R.S.
//         Date:    2015-FEB-10
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc.user_interface;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import app.dmc.Hub;
import app.dmc.R;
import app.dmc.Room;

/** Main view where room views are placed
*
*
*/
public class MainScreen {
    //-----------------------------------------------------------------------------------------------------------------
    //  Public Interface
    /** \brief Constructor, construct the main screen with the given hub.
    *
    *
    */
    public MainScreen(ActionBarActivity _activity, Hub _currentHub) {
        setHub(_activity, _currentHub);
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief re-build the view with the given hub.
    *
    */
    public void setHub(ActionBarActivity _activity, Hub _currentHub){
        mCurrentHub = _currentHub;
		mActivity = _activity;

        LinearLayout ll = (LinearLayout) _activity.findViewById(R.id.main_screen);
        ll.removeAllViews();

        mRoomSelector = new RoomSelector(_activity, mCurrentHub.rooms());
        ll.addView(mRoomSelector.view());

        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TEST", "Someone is touching the Main Screen");
                return false;
            }
        });
        if(mCurrentHub.rooms().size() != 0)
            mCurrentRoom = mCurrentHub.room(mCurrentHub.rooms().get(0).id());

        if(mCurrentRoom != null)
            setRoomTitle();
    }

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief "666 UNCOMPLITED" Change the current room.
    *
    *
    */    
    public void setRoom(String _roomId) {
	    mCurrentRoom = mCurrentHub.room(_roomId);
	    setRoomTitle();
	    /// 666 TODO: Tell room seletor to change the room.
    }

    //-----------------------------------------------------------------------------------------------------------------
    private void setRoomTitle(){
        ActionBar ab = mActivity.getSupportActionBar();
        ab.setTitle(mCurrentRoom.name());
    }

    //-----------------------------------------------------------------------------------------------------------------
    //  Private interface
    private Hub 		mCurrentHub = null;
    private Room 		mCurrentRoom = null;
	private ActionBarActivity	mActivity = null;

    // Views
    private RoomSelector mRoomSelector = null;
}
