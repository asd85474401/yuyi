package top.lcywings.pony.service.common;

import top.lcywings.pony.common.constant.Constant;
import top.lcywings.pony.common.exception.CustomException;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/12
 **/
public abstract class BaseService {

    /**
     * 校验分页参数
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @author majuehao
     * @date 2022/2/28 14:24
     **/
    protected void checkPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) {
            throw new CustomException("很抱歉，传参错误，页码必须大于0");
        }

        if (pageSize == null || pageSize <= 0) {
            throw new CustomException("很抱歉，传参错误，每页数量必须大于0");
        }

        if (pageSize > Constant.Page.MAX_PAGE_SIZE) {
            throw new CustomException("很抱歉，传参错误，每页数量不能超过100条");
        }
    }
}