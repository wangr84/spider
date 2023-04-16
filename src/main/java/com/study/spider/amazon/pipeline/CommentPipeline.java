package com.study.spider.amazon.pipeline;

import com.alibaba.fastjson.JSON;
import com.study.dao.CommentMapper;
import com.study.po.Comment;
import com.study.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommentPipeline implements Pipeline {

    @Autowired
    private CommentMapper commentMapper;
    private Map<String, Object> fields = new HashMap<String, Object>();
    @Override
    public void process(ResultItems resultItems, Task task) {
         List<String> nameList =resultItems.get("nameList");
         List<String> dateList =resultItems.get("dateList");
         List<String> commentList =resultItems.get("commentList");
         List<String> ratingList =resultItems.get("ratingList");
         List<String> titleList = resultItems.get("titleList");
         String asin = resultItems.get("asin");
         Comment lastComment = commentMapper.getLastCommment(asin);

         List<Comment> comments = new ArrayList<>();
         for(int i=0;i<nameList.size();i++){
             String name = nameList.get(i);
             String commentStr = commentList.get(i);
             String ratingStr = ratingList.get(i);
             String date = dateList.get(i);
             String title = titleList.get(i);
             Comment comment = new Comment();
             comment.setName(name);
             comment.setContent(commentStr);
             comment.setRating(ratingStr);
             comment.setTitle(title);
             comment.setUrl(resultItems.get("url"));
             comment.setOrderNo(resultItems.get("asin"));
             comment.setInTime(new Date());
             if(StringUtils.isNotEmpty(date)){
                 comment.setTimeStr(date);
                 Date commentDate =  DateUtil.getDayFromDateStr(date);
                 comment.setCreateTime(commentDate);
             }
             comments.add(comment);
         }

         if(CollectionUtils.isNotEmpty(comments)) {
             //评论倒叙排列
             comments = comments.stream().sorted(Comparator.comparing(Comment::getCreateTime).reversed()).collect(Collectors.toList());
             String updateWay = resultItems.get("updateWay");
             comments = comments.stream().filter(p->{
                 //增量更新方式，只取
                 if(Objects.nonNull(lastComment) && !updateWay.equals("all")){
                     Date lastDay = DateUtil.getDayFromDateStr(lastComment.getTimeStr());
                     //评论时间在db最新评论之前的，过滤掉
                     if(p.getCreateTime().compareTo(lastDay)<=0){
                         return false;
                     }
                 }
                 return true;
             }).collect(Collectors.toList());
         }

         if(CollectionUtils.isNotEmpty(comments)){
             log.info("评论入库：{}",JSON.toJSONString(comments));
             commentMapper.batchInsertOrUpdate(comments);
         }

    }
}
