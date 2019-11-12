package com.ggg.common.ws

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("data")
	val data: T? = null,

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("page_size")
	val pageSize: Int? = null
)