package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 动态相关的地理位置
 *
 * @author auto create
 * @since 1.0, 2016-08-12 11:11:25
 */
public class NewsfeedLocationInfo extends AlipayObject {

	private static final long serialVersionUID = 5772776643428385358L;

	/**
	 * 地理信息
	 */
	@ApiField("ad_code")
	private String adCode;

	/**
	 * 纬度 latitude（填写非0非1）
	 */
	@ApiField("lat")
	private String lat;

	/**
	 * 经度 longitude（填写非0非1）
	 */
	@ApiField("lon")
	private String lon;

	public String getAdCode() {
		return this.adCode;
	}
	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}

	public String getLat() {
		return this.lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return this.lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}

}
