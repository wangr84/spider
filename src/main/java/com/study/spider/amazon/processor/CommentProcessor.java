package com.study.spider.amazon.processor;

import com.alibaba.fastjson.JSON;
import com.study.common.SpiderConfigTool;
import com.study.dao.CommentMapper;
import com.study.po.Comment;
import com.study.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CommentProcessor implements PageProcessor {
    @Autowired
    private CommentMapper commentMapper;

    public class ArticleXpath{
        public String nameXpath;	//姓名过滤表达式
        public String dateXpath;	//日期表达式
        public String commentXpath; 	//评论
        public String ratingXpath;  //评价
        public String titleXpath;
        public String pageLink;  //下一页
        public String updateWay; //更新方式
        public String asinCode;
    }

    private Site site = new Site();
    private SpiderConfigTool spiderConfig;
    private String domain;//当前域名
    private String blogFlag;
    private List<ArticleXpath> articleXpaths;			//获取文件表达式

    /**
     * 初始化
     */
    @PostConstruct
    public void init() throws Exception {
        spiderConfig = new SpiderConfigTool("www.amazon.com");
        if(spiderConfig == null){
            throw new Exception("不支持的网站！");
        }

        String domain = spiderConfig.getSpiderNode().selectSingleNode("domain").getText();
        site = Site.me().setDomain(domain);
        this.domain = domain;
        String charset = spiderConfig.getSpiderNode().selectSingleNode("charset").getText();
        site.setCharset(charset);
        site.setSleepTime(1);

        blogFlag = spiderConfig.getSpiderNode().selectSingleNode("blog-flag").getText();
        initArticleXpath();

    }

    /**
     * 初始化 文章规则
     */
    @SuppressWarnings("unchecked")
    private void initArticleXpath(){
        articleXpaths = new ArrayList<ArticleXpath>();	//获取文件表达式

        List<Node> list = spiderConfig.getSpiderNode().selectNodes("article-xpath");
        String link = spiderConfig.getSpiderNode().selectSingleNode("page-links-rex").getText();
        String updateWay = spiderConfig.getSpiderNode().selectSingleNode("update-way").getText();
        String asinCode = spiderConfig.getSpiderNode().selectSingleNode("asin-code").getText();

        for(Node node : list){
            String name = node.selectSingleNode("name-xpath").getText();
            String date = node.selectSingleNode("date-xpath").getText();
            String comment = node.selectSingleNode("comment-xpath").getText();
            String rating = node.selectSingleNode("rating-xpath").getText();
            String title = node.selectSingleNode("title-xpath").getText();
            String pageLink = node.selectSingleNode("rating-xpath").getText();

            ArticleXpath articleXpath = new ArticleXpath();
            articleXpath.nameXpath=name;
            articleXpath.dateXpath=date;
            articleXpath.commentXpath=comment;
            articleXpath.ratingXpath=rating;
            articleXpath.pageLink = link;
            articleXpath.titleXpath = title;
            articleXpath.updateWay = updateWay;
            articleXpath.asinCode = asinCode;
            articleXpaths.add(articleXpath);
        }
    }

    @Override
    public void process(Page page) {
       //String comments =  page.getHtml().xpath("div[@id=cm_cr-review_list]").toString();
        //System.out.println(JSON.toJSONString(comments));
        getCommentsPage(page);
    }

    public String getAsinCode(){
        String asinCode = articleXpaths.get(0).asinCode.trim();
        return asinCode;
    }

    /**
     * 抓取博客内容
     * @param page
     */
    private void getCommentsPage(Page page){
        if(Objects.isNull(page.getHtml().xpath(articleXpaths.get(0).nameXpath))){
            //没有评论，返回
            return;
        }
        String url = page.getUrl().toString();
        String asin="";
        //从url中匹配ASIN码
        Pattern p2 = Pattern.compile("product-reviews/[A-Z0-9]+");
        Matcher m2 = p2.matcher(url);
        if(m2.find()){
            String matStr = m2.group(0);
            asin = matStr.split("/")[1];
            page.putField("asin",asin);
        }
        if(StringUtils.isEmpty(asin)){
            log.info("url错误，从url中未获取到ASIN码，url:{}",url);
        }



        List<String> nameList = page.getHtml().xpath(articleXpaths.get(0).nameXpath).all();
        List<String> dateList = page.getHtml().xpath(articleXpaths.get(0).dateXpath).all();
        List<String> commentList = page.getHtml().xpath(articleXpaths.get(0).commentXpath).all();
        List<String> ratingList = page.getHtml().xpath(articleXpaths.get(0).ratingXpath).all();
        List<String> titleList = page.getHtml().xpath(articleXpaths.get(0).titleXpath).all();
        commentList = commentList.stream().filter(p->!p.contains("<i class")).collect(Collectors.toList());
//        if(nameList.size()>10){
        nameList= nameList.stream().distinct().collect(Collectors.toList());
//        }

        page.putField("nameList", nameList);
        page.putField("dateList", dateList);
        page.putField("commentList", commentList);
        page.putField("ratingList", ratingList);
        page.putField("titleList",titleList);
        page.putField("url",url);

//        String lastCommentDay = dateList.get(0);
//        String firstComment = dateList.get(dateList.size()-1);
//        Date lastDay = DateUtil.getDayFromDateStr(lastCommentDay);
//        Date fistCommDay =  DateUtil.getDayFromDateStr(firstComment);
        //默认增量更新，全量更新返回true
        boolean flag =articleXpaths.get(0).updateWay.equalsIgnoreCase("all"); //nextPage(asin,fistCommDay);

        //处理下一页链接
        Selectable nextBtn = page.getHtml().xpath(articleXpaths.get(0).pageLink);
        if(Objects.nonNull(nextBtn.get())) {
            String nextPage = page.getHtml().xpath(articleXpaths.get(0).pageLink).toString();
            Pattern p = Pattern.compile("pageNumber=(\\d*)");
            Matcher m = p.matcher(nextPage);
            if (m.find()) {
                String pageNO = m.group(1);
                System.out.println("pageNo===" + pageNO);
                //最多取20页
                if (flag && Integer.valueOf(pageNO) < 20) {
                    page.addTargetRequest(nextPage);
                }
            }
            page.putField("nextPage", nextPage);
        }
        String updateWay = spiderConfig.getSpiderNode().selectSingleNode("update-way").getText();
        page.putField("updateWay",updateWay);
    }

    private boolean nextPage(String asin,Date firCommDate){
        Comment lastComment = commentMapper.getLastCommment(asin);
        //db中不存在，说明第一次
        if(Objects.isNull(lastComment)){
            return true;
        }
        //当前页的第1条评论在数据库最新评论之后，需要取下一页
        if(firCommDate.compareTo(lastComment.getCreateTime())>0){
             return true;
        }
        return false;
    }



    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args){
        String url ="/Grand-Ride-Premium-Trailer-175/product-reviews/B00MXO0F60/ref=cm_cr_getr_d_paging_btm_4?ie=UTF8&amp;pageNumber=4&amp;pageSize=10";

        Pattern p=Pattern.compile("pageNumber=(\\d*)");
        Matcher m=p.matcher(url);

        if(m.find()) {
            String spiderName = m.group(1);
            System.out.println(spiderName);
        }

    }
}
