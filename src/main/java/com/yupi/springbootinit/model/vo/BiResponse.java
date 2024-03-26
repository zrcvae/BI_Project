package com.yupi.springbootinit.model.vo;

import lombok.Data;

/**
 * @author zrc
 * @date 2024/03/14
 */
@Data
public class BiResponse {
    private String genChart;

    private String genResult;

    // 新生成的图标id
    private Long chartId;
}
