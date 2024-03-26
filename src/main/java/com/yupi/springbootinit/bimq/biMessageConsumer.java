package com.yupi.springbootinit.bimq;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.BiMqConstant;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


/**
 * @author zrc
 * @date 2024/03/26
 */
@Slf4j
@Configuration
public class biMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    // 通过注解的方式绑定生成队列和交换机
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = BiMqConstant.QUEUE_NAME),
            exchange = @Exchange(name = BiMqConstant.EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
            key = {"bi"}
    ))
    public void receiveMessage(String message){
        log.info("接收到消息：{}", message);
        if (StringUtils.isBlank(message)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if(chart == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }

        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        log.info("得到的id为：{}", message);
        updateChart.setStatus("running");
        boolean b = chartService.updateById(updateChart);
        if(!b){
            handleChartUpdateError(chart.getId(), "更新状态执行中失败");
            return;
        }
        // 调用AI
        String result = aiManager.doChar(CommonConstant.BI_MODEL_ID, buildUserInput(chart));
        log.info("调用ai得到结果为：{}", result);
        // 对返回结果进行拆分（这个ai经过训练，可以按照固定的格式回答）
        String[] splits = result.split("【【【【【");
        // 对拆分结果进行校验
        if(splits.length < 3){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI生成的结果错误");
        }
        // 将得到的结果保存到数据库以及输出到前端中
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        // 调用AI成功后再更新一次状态
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setStatus("succeed");
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        boolean b1 = chartService.updateById(updateChartResult);
        if(!b1){
            handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
            return;
        }

    }

    // 处理上面接口的异常方法
    private void handleChartUpdateError(long chartId, String execMessage){
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if(!updateResult){
            log.error("更新表更新状态失败" + chartId + "," + execMessage);
        }
    }

    private String buildUserInput(Chart chart){
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析的目标
        String userGoal = goal;
        // 如果图表类型不为空
        if(StringUtils.isNotBlank(chartType)){
            userGoal += ",请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }
}
