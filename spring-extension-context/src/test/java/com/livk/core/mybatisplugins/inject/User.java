package com.livk.core.mybatisplugins.inject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.livk.commons.util.DateUtils;
import com.livk.core.mybatisplugins.inject.annotation.SqlFunction;
import com.livk.core.mybatisplugins.inject.enums.FunctionType;
import com.livk.core.mybatisplugins.inject.enums.SqlFill;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 */
@Data
class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	@JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
	@SqlFunction(fill = SqlFill.INSERT, time = FunctionType.DATE)
	private Date insertTime;

	@JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
	@SqlFunction(fill = SqlFill.INSERT_UPDATE, time = FunctionType.DATE)
	private Date updateTime;

}
