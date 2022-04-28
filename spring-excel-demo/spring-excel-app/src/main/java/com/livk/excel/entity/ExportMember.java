package com.livk.excel.entity;

import lombok.Data;

/**
 * <p>
 * ExportMember
 * </p>
 *
 * @author livk
 * @date 2022/2/11
 */
@Data
public class ExportMember {

	private String name;

	private Integer gender;

	private String idCard;

	private String bankNo;

	private String bankName;

	private String phone;

	/**
	 * 性别处理
	 */
	public String getGender() {
		return gender == 0 ? "男" : "女";
	}

}
