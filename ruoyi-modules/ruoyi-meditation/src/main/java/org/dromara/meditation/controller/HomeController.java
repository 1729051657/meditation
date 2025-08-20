package org.dromara.meditation.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.meditation.domain.vo.HomeDataVo;
import org.dromara.meditation.service.IHomeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页数据接口
 *
 * @author system
 * @date 2025-01-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/meditation/home")
public class HomeController extends BaseController {

    private final IHomeService homeService;

    /**
     * 获取首页数据
     * 
     * @return 首页数据
     */
    @SaIgnore
    @GetMapping("/data")
    public R<HomeDataVo> getHomeData() {
        return R.ok(homeService.getHomeData());
    }
}
