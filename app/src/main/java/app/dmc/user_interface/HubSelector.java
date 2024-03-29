///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author: Pablo R.S.
//         Date:    2015-FEB-10
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc.user_interface;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import app.dmc.R;
import app.dmc.User;
/** Expandable list that allows user to change current hub.
*
*
*/

public class HubSelector implements AdapterView.OnItemSelectedListener {
    //-----------------------------------------------------------------------------------------------------------------
    //  Public Interface

    /** \brief Constructor. Build class with the given list of hub ids
    *
    *
    */
    HubSelector(Context _context, List<String> _hubIdList){
        mHubList = _hubIdList;

        mHubSpinner = new Spinner(_context);
        ArrayAdapter<String> adapterHubList = new ArrayAdapter<String>(_context,R.layout.support_simple_spinner_dropdown_item,mHubList);
        mHubSpinner.setAdapter(adapterHubList);
        mHubSpinner.setOnItemSelectedListener(this);
    }

    //-----------------------------------------------------------------------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        User.get().setHub(mHubList.get(position));
    }

    //-----------------------------------------------------------------------------------------------------------------
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //-----------------------------------------------------------------------------------------------------------------
    View view(){
        return mHubSpinner;
    }

    //-----------------------------------------------------------------------------------------------------------------
    //  Private Interface

    // Members
    private List<String>    mHubList;
    private Spinner         mHubSpinner;
}
