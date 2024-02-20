package com.cinntra.ledure.model;

import java.util.ArrayList;

public class AttachmentModel {
    private String message;
    private int status;
    private ArrayList<Data> data;

    public class Data {
        private String id;
        
        private String File;
        
        private String LinkType;
        
        private String Caption;
        
        private String LinkID;
        
        private String CreateDate;
        
        private String CreateTime;
        
        private String UpdateDate;
        
        private String UpdateTime;
        
        private String Size;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFile() {
            return File;
        }

        public void setFile(String file) {
            File = file;
        }

        public String getLinkType() {
            return LinkType;
        }

        public void setLinkType(String linkType) {
            LinkType = linkType;
        }

        public String getCaption() {
            return Caption;
        }

        public void setCaption(String caption) {
            Caption = caption;
        }

        public String getLinkID() {
            return LinkID;
        }

        public void setLinkID(String linkID) {
            LinkID = linkID;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String createDate) {
            CreateDate = createDate;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getUpdateDate() {
            return UpdateDate;
        }

        public void setUpdateDate(String updateDate) {
            UpdateDate = updateDate;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String updateTime) {
            UpdateTime = updateTime;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String size) {
            Size = size;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
