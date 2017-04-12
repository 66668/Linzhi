package com.linzhi.model;

import java.io.Serializable;

/**
 * Created by sjy on 2017/3/31.
 */

public class SiteIDModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String siteName;
    private String siteID;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        siteName = siteName;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        siteID = siteID;
    }
}
