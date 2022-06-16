package com.livk.shardingsphere.jdbc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * SysLog
 * </p>
 *
 * @author livk
 * @date 2021/11/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_log")
public class SysLog implements Serializable {

	public static final String COL_ID = "id";

	public static final String COL_IP = "ip";

	public static final String COL_OS = "os";

	public static final String COL_DATA = "data";

	public static final String COL_DATE = "date";

	public static final String COL_INSERT_TIME = "insert_time";

	public static final String COL_UPDATE_TIME = "update_time";

	@Serial
	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * ip
	 */
	@TableField(value = "ip")
	private String ip;

	/**
	 * 操作系统
	 */
	@TableField(value = "os")
	private String os;

	/**
	 * 数据
	 */
	@TableField(value = "`data`")
	private String data;

	/**
	 * 分表键位
	 */
	@TableField(value = "`date`")
	private String date;

}
