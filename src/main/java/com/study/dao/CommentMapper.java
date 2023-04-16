package com.study.dao;

import com.study.po.Comment;
import org.apache.ibatis.annotations.Param;
import org.assertj.core.internal.bytebuddy.implementation.bind.annotation.Argument;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
public interface CommentMapper extends Mapper<Comment> {

    int batchInsertOrUpdate(List<Comment> commentList);

    Comment getLastCommment(@Param("asin") String asin);
}