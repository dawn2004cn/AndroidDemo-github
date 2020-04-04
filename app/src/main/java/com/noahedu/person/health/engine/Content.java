package com.noahedu.person.health.engine;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 23061 on 2018/12/25.
 */

public class Content {
    public static class Item {
        public int stage; //0x01—4~5岁，0x02—5~6岁，0x04—小学，0x00—其它。
        public int type; //图片类型 0x01，表示图片为jpg格式；为0x02，表示图片为png格式；为0x03，表示图片为bmp格式；为0x04，表示图片为gif格式
        public String name;
        public String videoId;
        public int questNum;
        public int questOrder;
        public String cacheFile;
        public int logoAddr;
        public byte [] logo;
        public byte [] voice;

        public JSONObject writeToJson() {
            JSONObject obj = new JSONObject();
            try {
                obj.put("stage", stage);
                obj.put("name", name);
                obj.put("videoId", videoId);
                obj.put("questNum", questNum);
                obj.put("questOrder", questOrder);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return obj;
        }

        public static Item creatFromJron(JSONObject obj) {
            Item thiz = new Item();
            thiz.stage = obj.optInt("stage");
            thiz.name = obj.optString("name");
            thiz.questNum = obj.optInt("questNum");
            thiz.questOrder = obj.optInt("questOrder");
            thiz.videoId = obj.optString("videoId");
            return thiz;
        }
    }

    public int type; //0x01，表示图片为jpg格式；为0x02，表示图片为png格式；为0x03，表示图片为bmp格式；为0x04，表示图片为gif格式
    public int itemCount;
    public int itemOrder; // 主题的目录序号
    public String title; // 对应主题标题
    public String cacheFile;
    public int logoAddr;
    public byte [] logo;
    public byte [] voice;
    public Item [] items;
    public static Content createFromJson(JSONObject obj) {
        Content thiz = new Content();
        thiz.itemOrder = obj.optInt("itemOrder");
        thiz.itemCount = obj.optInt("itemCount");
        thiz.title = obj.optString("title");
        thiz.type = obj.optInt("type");
        JSONArray array = obj.optJSONArray("items");
        int length = array.length();
        thiz.items = new Item[length];
        for(int i=0; i<length; i++) {
            JSONObject qs = array.optJSONObject(i);
            thiz.items[i] = Item.creatFromJron(qs);
        }
        return thiz;
    }

    public JSONObject writeToJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("itemCount", itemCount);
            obj.put("itemOrder", itemOrder);
            obj.put("title",title);
            obj.put("type",type);

            JSONArray array = new JSONArray();
            for(int i=0; i<items.length; i++) {
                array.put(items[i].writeToJson());
            }
            obj.put("items", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
    public String GetContentPicType(int type)
    {
    	String pic = ".jpg";
    	switch(type)
    	{
    	case 1:
    		pic = ".jpg";
    		break;
    	case 2:
    		pic = ".png";
    		break;
    	case 3:
    		pic = ".bmp";
    		break;
    	case 4:
    		pic = ".gif";
    		break;
    	}
    	return pic;
    }
}
