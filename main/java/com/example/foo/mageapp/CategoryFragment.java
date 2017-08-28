package com.example.foo.mageapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foo.mageapp.catalog.Category;
import com.example.foo.mageapp.catalog.Product;
import com.example.foo.mageapp.helper.ImgDownloader;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 *
 * Needs a "Navigation Drawer" feature to render hamburger menu..
 */
public class CategoryFragment extends Fragment {

    protected static final String TAG = "CategoryFragment";
    protected static final int GRID_SPAN_CNT = 2;
    protected static final String ARG_CATEGORY = "ARG_CATEGORY";
    protected Category mCategory;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mRecyclerViewAdapter;
    protected ImgDownloader<ItemViewHolder> mImgDownloader;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mCategory = args.getParcelable(ARG_CATEGORY);

        mImgDownloader = new ImgDownloader<>(getContext(), new Handler());
        mImgDownloader.setOnDownloadListener(new ImgDownloader.OnDownloadListener<ItemViewHolder>() {
            @Override
            public void onDownloaded(ItemViewHolder holder, Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                holder.bindDrawable(drawable);
            }
        });
        mImgDownloader.start();
        mImgDownloader.getLooper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), GRID_SPAN_CNT));
        mRecyclerViewAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImgDownloader != null) {
            mImgDownloader.quit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mImgDownloader != null) {
            mImgDownloader.clearQueue();
        }
    }

    public static Fragment getNewInstance(Category cat) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, cat);
        Fragment frgmnt = new CategoryFragment();
        frgmnt.setArguments(args);
        return frgmnt;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        protected Product mItem;
        protected ImageView mIcon;
        protected TextView mLabel;
        protected TextView mPrice;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.item_icon);
            mLabel = (TextView) itemView.findViewById(R.id.item_label);
            mPrice = (TextView) itemView.findViewById(R.id.item_price);
        }
        public void bindItem(Product item) {
            mItem = item;
            mLabel.setText(item.getName());
            mPrice.setText(item.getPrice());
        }
        public void bindDrawable(Drawable drawable) {
            mIcon.setImageDrawable(drawable);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        @Override
        public int getItemCount() {
            return mCategory.getProducts().size();
        }
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater infalater = LayoutInflater.from(getContext());
            View view = infalater.inflate(R.layout.recycler_view_item, parent, false);
            return new ItemViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Product product = mCategory.getProducts().get(position);
            holder.bindItem(product);
            mImgDownloader.queueImg(holder, product.getIcon());
        }
    }
}
