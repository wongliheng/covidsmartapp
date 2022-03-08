package com.covidsmartapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ResponseBody {

    private String status, total_hits;
    private int page, total_pages, page_size;
    private JSONArray articlesList;
    private List<user_input> user_inputList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_hits() {
        return total_hits;
    }

    public void setTotal_hits(String total_hits) {
        this.total_hits = total_hits;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public JSONArray getArticlesList() {
        return articlesList;
    }

    public void setArticlesList(JSONArray articlesList) {
        this.articlesList = articlesList;
    }

    public List<user_input> getUser_inputList() {
        return user_inputList;
    }

    public void setUser_inputList(List<user_input> user_inputList) {
        this.user_inputList = user_inputList;
    }

    private class user_input {
        private String q, lang, from, sort_by;
        private int page, size;

        public String getQ() {
            return q;
        }

        public void setQ(String q) {
            this.q = q;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getSort_by() {
            return sort_by;
        }

        public void setSort_by(String sort_by) {
            this.sort_by = sort_by;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
