package com.swj.prototypealpha.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * author mardawang
 * <p>
 * email:wy363681759@163.com
 * <p>
 * date: 2018/5/17
 * <p>
 * desc:
 */

public class HintDialogFragment extends DialogFragment {

    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String REQUEST_CODE = "request_code";

    public static HintDialogFragment newInstance(int title, int message, int requestCode) {
        HintDialogFragment frag = new HintDialogFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        args.putInt(MESSAGE, message);
        args.putInt(REQUEST_CODE, requestCode);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(TITLE);
        int message = getArguments().getInt(MESSAGE);
        final int requestCode = getArguments().getInt(REQUEST_CODE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("允许",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((DialogFragmentCallback) getActivity()).doPositiveClick(requestCode);
                            }
                        }
                )
                .setNegativeButton("禁止",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((DialogFragmentCallback) getActivity()).doNegativeClick(requestCode);
                            }
                        }
                )
                .create();
    }

    public interface DialogFragmentCallback {

        void doPositiveClick(int requestCode);

        void doNegativeClick(int requestCode);
    }

}
