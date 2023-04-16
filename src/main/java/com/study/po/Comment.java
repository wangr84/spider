package com.study.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Integer id;

    private String orderNo;

    private String url;

    private String name;

    private String title;

    private String content;

    private String rating;

    private Date createTime;

    private String timeStr;
    /**入库时间*/
    private Date inTime;


}