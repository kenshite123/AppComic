package com.ggg.home.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ConfigModel : Serializable {
    @SerializedName("site_deploy")
    @Expose
    var siteDeploy: String = ""

    @SerializedName("site_description")
    @Expose
    var siteDescription: String = ""

    @SerializedName("site_email")
    @Expose
    var siteEmail: String = ""

    @SerializedName("site_facebook")
    @Expose
    var siteFacebook: String = ""

    @SerializedName("site_footer")
    @Expose
    var siteFooter: String = ""

    @SerializedName("site_icon")
    @Expose
    var siteIcon: String = ""

    @SerializedName("site_keyword")
    @Expose
    var siteKeyword: String = ""

    @SerializedName("site_limit")
    @Expose
    var siteLimit: String = ""

    @SerializedName("site_logo")
    @Expose
    var siteLogo: String = ""

    @SerializedName("site_name")
    @Expose
    var siteName: String = ""

    @SerializedName("site_phone")
    @Expose
    var sitePhone: String = ""

    @SerializedName("site_summary")
    @Expose
    var siteSummary: String = ""

    @SerializedName("site_title")
    @Expose
    var siteTitle: String = ""
}