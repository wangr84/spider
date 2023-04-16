package com.study.utils;

public class UrlUtil {
    private final static String BASE_URL = "https://www.amazon.com/product-reviews";
    public static String getReviewUrl(String asin, int pageNumber) {
        //https://www.amazon.com/product-reviews/B085ZRSLPW/reviewerType=all_reviews/ref=cm_cr_getr_d_paging_btm_next_3?pageNumber=3
        return String.format("%s/%s/reviewerType=all_reviews/ref=cm_cr_getr_d_paging_btm_next_%s?pageNumber=%s",
                BASE_URL, asin, pageNumber, pageNumber);
    }
}
