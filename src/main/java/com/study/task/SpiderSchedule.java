package com.study.task;

import com.study.spider.amazon.pipeline.CommentPipeline;
import com.study.spider.amazon.processor.CommentProcessor;
import com.study.utils.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
@EnableScheduling
public class SpiderSchedule {
    @Autowired
    private CommentProcessor pageProcessor;
    @Autowired
    private CommentPipeline commentPipeline;
//    @Scheduled(cron = "0 0/5 * * * ?")//每5分钟
      @Scheduled(cron = "0 36 16 * * ?")  //12：30
    public void pullTask() throws IOException, InterruptedException {
        log.info("========定时拉取amazon评论任务开始执行========");
        String asinCodes = pageProcessor.getAsinCode();
        String[] asinArr = asinCodes.split("\\|");
        for(String asinStr:asinArr) {
            String asinCode = asinStr.split(",")[0].trim();
            String downUrl = UrlUtil.getReviewUrl(asinCode,1);
            log.info("======开始获取，asin:{},pageUrl:{}",asinCode,downUrl);
            Spider.create(pageProcessor)
                    .addUrl(downUrl)
                    .addPipeline(commentPipeline).run();
            Thread.sleep(10000);
        }

    }


}
