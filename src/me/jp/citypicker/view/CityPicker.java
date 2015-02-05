package me.jp.citypicker.view;

import java.util.ArrayList;

import me.jp.citypicker.AreaUtil;
import me.jp.citypicker.R;
import me.jp.citypicker.view.ScrollerNumberPicker.OnSelectListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * 省市选择器
 * 
 * 将省份和城市放入同一个布局
 * 
 * @author JiangPing
 * 
 */
public class CityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;

	private String city_code_string;

	private AreaUtil mAreaUtil;
	private ArrayList<String> mProvinceList = new ArrayList<String>();
	/** 初始省份设置 */
	private final String DEFUALT_PROVINCE = "北京";

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAreaInfo();
	}

	public CityPicker(Context context) {
		super(context);
		getAreaInfo();
	}

	private void getAreaInfo() {
		mAreaUtil = new AreaUtil();
		mProvinceList = mAreaUtil.getProvinces();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);

		provincePicker.setData(mProvinceList);
		provincePicker.setDefault(1);

		cityPicker.setData(mAreaUtil.getCitysByProvince(DEFUALT_PROVINCE));
		cityPicker.setDefault(1);

		provincePicker.setOnSelectListener(new OnSelectListener() {
			@Override
			public void endSelect(int id, String text) {
				// System.out.println("id-->" + id + "text----->" + text);
				if (text.equals("") || text == null)
					return;
				if (tempProvinceIndex != id) {
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					// 根据省份，获取Map的城市集合（产生联动效果）
					ArrayList<String> citys = mAreaUtil
							.getCitysByProvince(mProvinceList.get(id));
					cityPicker.setData(citys);
					if (citys.size() > 1) {// 大于1个城市，中心显示位置为第2个城市
						cityPicker.setDefault(1);
					} else {// 从第一个城市开始
						cityPicker.setDefault(0);
					}
					int lastIndex = Integer.valueOf(provincePicker
							.getListSize());
					if (id > lastIndex) {
						provincePicker.setDefault(lastIndex - 1);
					}
				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
			}
		});

		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (temCityIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					int lastIndex = Integer.valueOf(cityPicker.getListSize());
					if (id > lastIndex) {
						cityPicker.setDefault(lastIndex - 1);
					}
				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {

			}
		});

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getCity_code_string() {
		return city_code_string;
	}

	/**
	 * 获取详细区域（省份+城市）
	 * 
	 * @return
	 */
	public String getDetailArea() {
		String detailArea = provincePicker.getSelectedText()
				+ cityPicker.getSelectedText();
		return detailArea;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
