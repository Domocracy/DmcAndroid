///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//      Domocracy Android App
//          Author:  Joscormir
//         Date:    2015-FEB-19
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//

package app.dmc.user_interface;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import app.dmc.R;
import app.dmc.User;

    /** Class that manage the Top bar (ActionBar)
    *
    *
    */
public class TopBar {

    //-----------------------------------------------------------------------------------------------------------------
    /** \brief Add to the top bar menu a butto to change the current hub's IP.
    *
    *
    */
    public boolean setIpButton(Context _context){

            AlertDialog.Builder builderDialogSetIp = new AlertDialog.Builder(_context);
            LayoutInflater inflaterSetIp = (LayoutInflater)_context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);

            final View dialogLayout = inflaterSetIp.inflate(R.layout.alert_dialog_set_ip,null);
            builderDialogSetIp.setView(dialogLayout);
            EditText lastIp = (EditText)dialogLayout.findViewById(R.id.ipEditor);

            lastIp.setText(User.get().getCurrentHub().ip()); //Fill it with the currentHub IP

            builderDialogSetIp.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText ip = (EditText)dialogLayout.findViewById(R.id.ipEditor);

                    String ipToString = ip.getText().toString();
                    User.get().getCurrentHub().modifyIp(ipToString); //modify IP 
                }
            });
            builderDialogSetIp.setNegativeButton("Cancel",null);
            builderDialogSetIp.create().show();
            return true;
        }
    //-----------------------------------------------------------------------------------------------------------------

}
