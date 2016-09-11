package com.yetwish.contactsdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;


/**
 * base activity,dialog and something
 * Created by Yetwish on 2016/4/8 0008.
 */
public class BaseActivity extends AppCompatActivity {

    protected MaterialDialog mProgressDialog;

    protected MaterialDialog mInputDialog;

    protected MaterialDialog mBasicDialog;

    protected MaterialDialog mListDialog;

    protected
    @ColorInt
    int mDialogBgColor;
    protected
    @ColorInt
    int mDialogTitleTextColor;
    protected
    @ColorInt
    int mDialogContentTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogBgColor = ContextCompat.getColor(this, R.color.colorWhite);
        mDialogTitleTextColor = ContextCompat.getColor(this, R.color.colorPrimary);
        mDialogContentTextColor = ContextCompat.getColor(this, R.color.colorText);
    }

    protected void showSoftKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showProgressDialog() {
        showProgressDialog(getString(R.string.loading));
    }

    protected void showProgressDialog(DialogInterface.OnDismissListener listener) {
        showProgressDialog(getString(R.string.loading), listener);
    }

    protected void showProgressDialog(CharSequence msg) {
        showProgressDialog(msg, false, null);
    }

    protected void showProgressDialog(CharSequence msg, DialogInterface.OnDismissListener listener) {
        showProgressDialog(msg, false, listener);
    }

    protected void showProgressDialog(CharSequence msg, boolean cancelable, DialogInterface.OnDismissListener listener) {
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(this)
                    .content(msg)
                    .progress(true, 0)
                    .backgroundColor(mDialogBgColor)
                    .contentColor(mDialogTitleTextColor)
                    .canceledOnTouchOutside(cancelable)
                    .dismissListener(listener)
                    .show();
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.setContent(msg);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setOnDismissListener(listener);
            mProgressDialog.show();
        }
    }

    protected void showListDialog(List<String> list, CharSequence title, MaterialDialog.ListCallbackSingleChoice listener) {
        mListDialog = new MaterialDialog.Builder(this)
                .title(title)
                .backgroundColor(mDialogBgColor)
                .titleColor(mDialogTitleTextColor)
                .contentColor(mDialogContentTextColor)
                .items(list)
                .itemsCallbackSingleChoice(-1, listener)
                .show();
    }

    protected void showInputDialog(CharSequence title, CharSequence hintText, MaterialDialog.InputCallback callback) {
        showInputDialog(title, hintText, "", callback);
    }

    protected void showInputDialog(CharSequence title, CharSequence hintText,
                                   CharSequence preFill, MaterialDialog.InputCallback callback) {
        if (mInputDialog == null) {
            mInputDialog = new MaterialDialog.Builder(this)
                    .title(title)
                    .backgroundColor(mDialogBgColor)
                    .titleColor(mDialogTitleTextColor)
                    .contentColor(mDialogContentTextColor)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(hintText, preFill, callback)
                    .show();

        } else if (!mInputDialog.isShowing()) {
            mInputDialog.getInputEditText().setText(preFill);
            mInputDialog.show();
        }
    }

    protected void showBasicDialog(CharSequence title, CharSequence content,
                                   MaterialDialog.SingleButtonCallback callback) {
        showBasicDialog(title, content, getString(R.string.confirm), getString(R.string.cancel), callback);
    }


    protected void showBasicDialog(CharSequence title, CharSequence content, CharSequence positiveText,
                                   CharSequence negativeText, final MaterialDialog.SingleButtonCallback callback) {
        mBasicDialog = null;
        mBasicDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .backgroundColor(mDialogBgColor)
                .titleColor(mDialogTitleTextColor)
                .contentColor(mDialogContentTextColor)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        callback.onClick(mBasicDialog, DialogAction.NEGATIVE);
                    }
                })
                .onAny(callback).show();
    }


    protected void hideListDialog(){
        if (mListDialog != null && mListDialog.isShowing()) {
            mListDialog.dismiss();
        }
    }

    protected void hideBasicDialog() {
        if (mBasicDialog != null && mBasicDialog.isShowing()) {
            mBasicDialog.dismiss();
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void hideInputDialog() {
        if (mInputDialog != null && mInputDialog.isShowing()) {
            mInputDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyDialog();
    }

    private void hideDialog() {
        hideProgressDialog();
        hideInputDialog();
        hideBasicDialog();
        hideListDialog();
    }

    private void destroyDialog() {
        hideDialog();
        mProgressDialog = null;
        mInputDialog = null;
        mBasicDialog = null;
        mListDialog = null;
    }


}
