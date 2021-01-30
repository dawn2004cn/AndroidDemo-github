package com.noahedu.demo.utils;

import java.util.List;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：RoboSysLogin$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/9/11$ 14:50$
 */
public class RoboSysLogin {

    /**
     * status : 0
     * msg : 登录成功
     * data : ["eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYXBpLnJvYm9zeXMuY2NcL2VkdVwvYXBpXC9hdXRoXC9sb2dpbiIsImlhdCI6MTU5OTgwNjg5OCwiZXhwIjoxNjAxMDE2NDk4LCJuYmYiOjE1OTk4MDY4OTgsImp0aSI6IjZiRTl5cUoyUTNkcGxWZFgiLCJzdWIiOjExMDkzLCJwcnYiOiI0ODZkNTcxNTMzNDNjMWVlMDY4YjUwZjcwM2NlMjdlZmFjZTRhMWI1In0.sVyRn6Ij9K_avQcxwjTnkK2WCU5CFqxYrLLAV9rVmzc","咖啡猫","normal",66,0,0,1,0,4,null,0,0,0,0,null]
     */

    private int status;
    private String msg;
    private List<String> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
