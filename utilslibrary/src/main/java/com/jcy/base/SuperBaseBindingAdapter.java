package com.jcy.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: jiangcy
 * @date: 16-8-30
 * @time: 下午4:34
 */
public abstract class SuperBaseBindingAdapter<T> extends BaseAdapter {
	protected final Context context;
	protected final LayoutInflater mLayoutInflater;
	protected List<T> dataList = new ArrayList<>();

	public SuperBaseBindingAdapter(Context context, List<T> dataList) {
		this.context = context;
		if (null != dataList && !dataList.isEmpty()) {
			this.dataList.clear();
			this.dataList.addAll(dataList);
		}
		mLayoutInflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public T getItem(int position) {
		return dataList == null ? null : dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHelper viewHelper = new ViewHelper(mLayoutInflater, convertView, parent).invoke();
		convertView = viewHelper.getConvertView();
		T item = getItem(position);
		ViewDataBinding mBinding = viewHelper.getViewBinding();
		setItemData(position, item, mBinding);
		return convertView;
	}

	public void setData(List<T> dataList) {
		if (null != dataList && !dataList.isEmpty()) {
			this.dataList = null;
			this.dataList = new ArrayList<>(dataList);
		}
	}

	/**
	 * 获取 item layout ResId
	 *
	 * @return int item layout resid
	 */
	public abstract int getItemLayoutResId();

	/**
	 * 设置item数据
	 */
	public abstract void setItemData(@Nullable final int position,@Nullable  final T dataItem,@Nullable  final ViewDataBinding binding);

	/**
	 * 实现中间环节的调用辅助类
	 */
	private class ViewHelper {
		private View convertView;
		private ViewGroup viewGroup;
		private ViewDataBinding mBinding;
		private LayoutInflater inflater;

		public ViewHelper(LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
			this.convertView = convertView;
			this.viewGroup = viewGroup;
			this.inflater = inflater;
		}

		public View getConvertView() {
			return convertView;
		}

		public ViewDataBinding getViewBinding() {
			return mBinding;
		}

		public ViewHelper invoke() {
			//covertView 复用的逻辑在这里呢  再也不用一遍一遍的写这个逻辑了
			if (convertView == null) {
				//新建 convertView
				mBinding = DataBindingUtil.inflate(inflater, getItemLayoutResId(), viewGroup, false);
				convertView = mBinding.getRoot();
				convertView.setTag(mBinding);
			} else {
				//复用 convertView
				mBinding = (ViewDataBinding) convertView.getTag();
			}
			return this;
		}
	}
}
