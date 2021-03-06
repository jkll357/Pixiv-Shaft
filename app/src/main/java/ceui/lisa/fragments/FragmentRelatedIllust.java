package ceui.lisa.fragments;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import ceui.lisa.activities.Shaft;
import ceui.lisa.adapters.BaseAdapter;
import ceui.lisa.adapters.IAdapter;
import ceui.lisa.core.RemoteRepo;
import ceui.lisa.databinding.FragmentBaseListBinding;
import ceui.lisa.http.Retro;
import ceui.lisa.model.ListIllust;
import ceui.lisa.models.IllustsBean;
import ceui.lisa.utils.Params;
import io.reactivex.Observable;

import static ceui.lisa.activities.Shaft.sUserModel;

/**
 * 相关插画
 */
public class FragmentRelatedIllust extends NetListFragment<FragmentBaseListBinding,
        ListIllust, IllustsBean> {

    private int illustID;
    private String mTitle;

    public static FragmentRelatedIllust newInstance(int id, String title) {
        Bundle args = new Bundle();
        args.putInt(Params.ILLUST_ID, id);
        args.putString(Params.ILLUST_TITLE, title);
        FragmentRelatedIllust fragment = new FragmentRelatedIllust();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        illustID = bundle.getInt(Params.ILLUST_ID);
        mTitle = bundle.getString(Params.ILLUST_TITLE);
    }

    @Override
    public void initRecyclerView() {
        staggerRecyclerView();
    }

    @Override
    public BaseAdapter<?, ? extends ViewDataBinding> adapter() {
        return new IAdapter(allItems, mContext);
    }

    @Override
    public RemoteRepo<ListIllust> repository() {
        return new RemoteRepo<ListIllust>() {
            @Override
            public Observable<ListIllust> initApi() {
                return Retro.getAppApi().relatedIllust(sUserModel.getResponse().getAccess_token(), illustID);
            }

            @Override
            public Observable<ListIllust> initNextApi() {
                return Retro.getAppApi().getNextIllust(sUserModel.getResponse().getAccess_token(),
                        mModel.getNextUrl());
            }

            @Override
            public boolean hasNext() {
                return Shaft.sSettings.isRelatedIllustNoLimit();
            }
        };
    }



    @Override
    public String getToolbarTitle() {
        return mTitle + "的相关作品";
    }
}
