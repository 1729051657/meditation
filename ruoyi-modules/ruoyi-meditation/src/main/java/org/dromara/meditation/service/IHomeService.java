package org.dromara.meditation.service;

import org.dromara.meditation.domain.vo.HomeDataVo;

/**
 * 首页数据服务接口
 *
 * @author system
 * @date 2025-01-01
 */
public interface IHomeService {

    /**
     * 获取首页数据
     *
     * @return 首页数据
     */
    HomeDataVo getHomeData();
}
