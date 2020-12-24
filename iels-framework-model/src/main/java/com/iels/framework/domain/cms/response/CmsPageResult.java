package com.iels.framework.domain.cms.response;

import com.iels.framework.domain.cms.CmsPage;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CmsPageResult extends ResponseResult {
    CmsPage cmsPage;

    public CmsPageResult(ResultCode resultCode, CmsPage cmsPage) {
        super(resultCode);
        this.cmsPage = cmsPage;
    }
}
