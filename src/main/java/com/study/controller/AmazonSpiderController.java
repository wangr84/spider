package com.study.controller;

import com.study.dao.CommentMapper;
import com.study.po.Comment;
import com.study.spider.amazon.pipeline.CommentPipeline;
import com.study.spider.amazon.processor.CommentProcessor;
import com.study.utils.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/amazon")
@Slf4j
public class AmazonSpiderController {
    @Autowired
    private CommentProcessor pageProcessor;
    @Autowired
    private CommentPipeline commentPipeline;


    @RequestMapping(value = "/loadComments")
    public String loadComments(String url){
            Spider.create(pageProcessor)
                    .addUrl(url)
                    .addPipeline(commentPipeline).run();

        return "success";
    }

    public static void main(String[] args) throws Exception {
       String url = "https://www.amazon.com/product-reviews/B00MXO0F60/reviewerType=all_reviews/ref=cm_cr_getr_d_paging_btm_next_1?pageNumber=1";
        //爬取博客，结果存放在BLogList中
//        CommentProcessor processor = new CommentProcessor();
//        processor.init();
//        Spider.create(processor)
//                .addUrl(url)
//                .addPipeline(new CommentPipeline()).run();
        //从url中匹配ASIN码
        Pattern p2 = Pattern.compile("product-reviews+[/][A-Z0-9]+");
        Matcher m2 = p2.matcher(url);
        if(m2.find()){
            String pageNO = m2.group(0);

            System.out.println(pageNO.split("/")[1]);
        }
    }
}
