package com.yupi.springbootinit.manager;

import com.alibaba.excel.EasyExcel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zrc
 * @date 2024/03/14
 * 对接AI平台
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    /**
     * AI对话
     * @param message
     * @return
     */
    public String doChar(Long biModelId, String message){
        DevChatRequest devChatRequest = new DevChatRequest();

        devChatRequest.setMessage(message);
        devChatRequest.setModelId(biModelId);

        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        if (response == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }

        return response.getData().getContent();
    }
}
