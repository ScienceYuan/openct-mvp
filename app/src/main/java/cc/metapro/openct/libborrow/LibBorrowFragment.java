package cc.metapro.openct.libborrow;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.metapro.openct.R;
import cc.metapro.openct.data.BorrowInfo;
import cc.metapro.openct.utils.ActivityUtils;
import cc.metapro.openct.utils.RecyclerViewHelper;


public class LibBorrowFragment extends Fragment implements LibBorrowContract.View {

    @BindView(R.id.lib_borrow_recycler_view)
    RecyclerView mRecyclerView;

    private Context mContext;
    private Unbinder mUnbinder;
    private BorrowAdapter mBorrowAdapter;
    private LibBorrowContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lib_borrow, container, false);
        mContext = getContext();
        mUnbinder = ButterKnife.bind(this, view);

        mBorrowAdapter = new BorrowAdapter(getContext());
        RecyclerViewHelper.setRecyclerView(getContext(), mRecyclerView, mBorrowAdapter);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        mPresenter.start();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mPresenter.storeBorrows();
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void setPresenter(LibBorrowContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showDue(List<BorrowInfo> infos) {
        if (infos != null) {
            List<BorrowInfo> dueInfo = new ArrayList<>(infos.size());
            for (BorrowInfo b : infos) {
                if (b.isExceeded()) {
                    dueInfo.add(b);
                }
            }
            mBorrowAdapter.setNewBorrows(dueInfo);
            mBorrowAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadBorrows(List<BorrowInfo> infos) {
        mBorrowAdapter.setNewBorrows(infos);
        mBorrowAdapter.notifyDataSetChanged();
        ActivityUtils.dismissProgressDialog();
        Toast.makeText(mContext, "共有 " + infos.size() + " 条借阅信息", Toast.LENGTH_SHORT).show();
    }

}
