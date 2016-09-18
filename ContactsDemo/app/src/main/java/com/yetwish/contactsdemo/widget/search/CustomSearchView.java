package com.yetwish.contactsdemo.widget.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yetwish.contactsdemo.R;

/**
 * 自定义搜索框 TODO emptyView
 * Created by yetwish on 2016/9/12.
 */
public class CustomSearchView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private EditText mEtSearch;
    private Button mBtnCancel;
    private ImageView mIvClear;
    private boolean inSearchMode = true; //init将searchMode赋为true 初始化视图时将设为false
    private ISearchListener mListener;
    private View mFrameLayer;
    private ListView mlvSearchResult;
    private SearchAdapter mAdapter;

    public CustomSearchView(Context context) {
        this(context, null);
    }


    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.search_view, this);
        initViews();

    }

    public void setSearchListener(ISearchListener listener) {
        this.mListener = listener;
    }

    private void initViews() {
        mEtSearch = (EditText) findViewById(R.id.etSearchText);
        mBtnCancel = (Button) findViewById(R.id.btnSearchCancel);
        mIvClear = (ImageView) findViewById(R.id.ivSearchClear);
        mFrameLayer = findViewById(R.id.frameLayer);
        mlvSearchResult = (ListView) findViewById(R.id.lvSearchResult);
        mAdapter = new SearchAdapter(mContext);
        mlvSearchResult.setAdapter(mAdapter);

        mlvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSearchMode(false);
                mListener.onQuitSearch((int)id);
            }
        });


        mEtSearch.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mIvClear.setOnClickListener(this);
        mFrameLayer.setOnClickListener(this);

        mEtSearch.addTextChangedListener(mTextWatcher);
        updateSearchMode(false);
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        //控制clearBtn和匹配列表
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mListener != null) {
                mListener.onSearch(s);
            }
            if ("".equals(s.toString())) {
                mIvClear.setVisibility(GONE);
                mlvSearchResult.setVisibility(GONE);
                if (inSearchMode)
                    mFrameLayer.setVisibility(VISIBLE);
                else
                    mFrameLayer.setVisibility(GONE);

            } else {
                mIvClear.setVisibility(VISIBLE);
                mFrameLayer.setVisibility(GONE);
                mlvSearchResult.setVisibility(VISIBLE);

                //搜索
                mAdapter.startQuery(s.toString().trim());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etSearchText:
                updateSearchMode(true);
                break;
            case R.id.btnSearchCancel: //取消 退出search
                updateSearchMode(false);
                if (mListener != null) {
                    mListener.onQuitSearch(ISearchListener.NULL);
                }
                break;
            case R.id.ivSearchClear:
                mEtSearch.setText("");
                break;
            case R.id.frameLayer: //点击遮罩层， 退出search
                updateSearchMode(false);
                if (mListener != null) {
                    mListener.onQuitSearch(ISearchListener.NULL);
                }
                break;
        }
    }

    private void updateSearchMode(boolean searchMode) {
        if (inSearchMode == searchMode) return;
        inSearchMode = searchMode;
        if (inSearchMode) {//进入search模式
            mEtSearch.setCursorVisible(true);
            mBtnCancel.setVisibility(VISIBLE);
            mFrameLayer.setVisibility(VISIBLE);
            mlvSearchResult.setVisibility(GONE);
        } else {//退出search模式
            mBtnCancel.setVisibility(GONE);
            mEtSearch.setCursorVisible(false);
            mEtSearch.setText("");
        }
    }
}
