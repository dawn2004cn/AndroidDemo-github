package com.noahedu.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.noahedu.demo.R;
import com.noahedu.demo.service.DingDangService;

public class DingDangActivity extends BaseActivity {
    private static final String TAG = "DingDangActivity";

    private ListView lvDemo = null;

    String[][] contentList = {
            {"学一学", "", ""},
            {"测一测", "", ""},
            {"练一练", "", ""},
            {"宝箱", "", ""},
            {"听一听", "", ""},
            {"叮当推理", "", ""},
            {"逻辑拼图", "", ""},
            {"逻辑推理", "", ""},
            {"同步课堂", "", ""},
            {"叮当单词", "", ""},
            {"叮当拼音", "", ""},
            {"叮当识字", "", ""},
            {"叮当字母", "", ""},
            {"综合测试", "", ""},
            {"综合测试", "", ""},
            {"逻辑思维测试", "", ""},
            {"英文表达测试", "", ""},
            {"中文表达测试", "", ""},
            {"专注记忆测试", "", ""},
            {"习惯养成测试", "", ""},
            {"百科常识测试", "", ""},
            {"数学运算测试", "", ""},
            {"幼小衔接测试", "", ""},
            {"中文表达练习", "", ""},
            {"英文表达练习", "", ""},
            {"逻辑思维练习", "", ""},
            {"百科常识练习", "", ""},
            {"习惯养成练习", "", ""},
            {"专注记忆练习", "", ""},
            {"数学运算练习", "", ""},
            {"小火车", "", ""},
            {"打海盗", "", ""},
            {"打地鼠", "", ""},
            {"踢足球", "", ""},
            {"逻辑狗", "", ""},
            {"魔法师", "", ""},
            {"叮当单词", "水果", ""},
            {"叮当单词", "食物", ""},
            {"叮当单词", "身体部位", ""},
            {"叮当单词", "水果", ""},
            {"叮当单词", "食物", ""},
            {"叮当单词", "节日", ""},
            {"叮当单词", "场所", ""},
            {"叮当单词", "数字", ""},
            {"叮当单词", "头部器官", ""},
            {"叮当单词", "动物", ""},
            {"叮当单词", "颜色", ""},
            {"叮当单词", "动作", ""},
            {"叮当单词", "家居", ""},
            {"叮当单词", "交通工具", ""},
            {"叮当单词", "玩具", ""},
            {"叮当单词", "衣物", ""},
            {"叮当单词", "蔬菜", ""},
            {"叮当单词", "人物", ""},
            {"叮当单词", "乐器", ""},
            {"叮当单词", "礼貌用语", ""},
            {"叮当单词", "爱好", ""},
            {"叮当单词", "学校", ""},
            {"叮当单词", "工具", ""},
            {"叮当单词", "时间", ""},
            {"叮当单词", "体型", ""},
            {"叮当拼音", "单韵母第一课", ""},
            {"叮当拼音", "单韵母第二课", ""},
            {"叮当拼音", "声母第一课", ""},
            {"叮当拼音", "声母第二课", ""},
            {"叮当拼音", "声母第三课", ""},
            {"叮当拼音", "声母第四课", ""},
            {"叮当拼音", "声母第五课", ""},
            {"叮当拼音", "声母第六课", ""},
            {"叮当拼音", "复韵母第一课", ""},
            {"叮当拼音", "复韵母第二课", ""},
            {"叮当拼音", "复韵母第三课", ""},
            {"叮当拼音", "前鼻韵母", ""},
            {"叮当拼音", "后鼻韵母", ""},
            {"叮当拼音", "整体认读音节第一课", ""},
            {"叮当拼音", "整体认读音节第二课", ""},
            {"叮当拼音", "整体认读音节第三课", ""},
            {"叮当拼音", "整体认读音节第四课", ""},
            {"叮当拼音", "整体认读音节第五课", ""},
            {"叮当识字", "乐园", ""},
            {"叮当识字", "数字", ""},
            {"叮当识字", "自然", ""},
            {"叮当识字", "方位", ""},
            {"叮当识字", "称呼", ""},
            {"叮当识字", "量词", ""},
            {"叮当识字", "动物", ""},
            {"叮当识字", "对比", ""},
            {"叮当识字", "动作", ""},
            {"叮当识字", "人物", ""},
            {"叮当识字", "颜色", ""},
            {"叮当识字", "时间", ""},
            {"叮当识字", "人体", ""},
            {"叮当识字", "五行", ""},
            {"叮当识字", "文具", ""},
            {"叮当识字", "植物", ""},
            {"叮当识字", "食物", ""},
            {"叮当识字", "学习", ""},
            {"叮当识字", "交通", ""},
            {"叮当识字", "爱好", ""},
            {"叮当识字", "气象", ""},
            {"叮当识字", "建筑", ""},
            {"叮当识字", "人体", ""},
            {"叮当识字", "物品", ""},
            {"叮当字母", "第一课", ""},
            {"叮当字母", "第二课", ""},
            {"叮当字母", "第三课", ""},
            {"叮当字母", "第四课", ""},
            {"叮当字母", "第五课", ""},
            {"叮当字母", "第六课", ""},
            {"叮当字母", "第七课", ""},
            {"叮当字母", "第八课", ""},
            {"中文表达练习", "古诗", "小班"},
            {"中文表达练习", "数字", "小班"},
            {"中文表达练习", "人体", "小班"},
            {"中文表达练习", "自然", "小班"},
            {"中文表达练习", "五行", "小班"},
            {"中文表达练习", "方位", "小班"},
            {"中文表达练习", "称呼", "小班"},
            {"中文表达练习", "量词", "小班"},
            {"中文表达练习", "动物", "小班"},
            {"中文表达练习", "对比", "小班"},
            {"中文表达练习", "动作", "小班"},
            {"中文表达练习", "文具", "小班"},
            {"中文表达练习", "植物", "小班"},
            {"中文表达练习", "人物", "小班"},
            {"中文表达练习", "食物", "小班"},
            {"中文表达练习", "学习", "小班"},
            {"中文表达练习", "交通", "小班"},
            {"中文表达练习", "颜色", "小班"},
            {"中文表达练习", "爱好", "小班"},
            {"中文表达练习", "气象", "小班"},
            {"中文表达练习", "时间", "小班"},
            {"中文表达练习", "识字", "中班"},
            {"中文表达练习", "拼音", "中班"},
            {"中文表达练习", "量词", "中班"},
            {"中文表达练习", "反义词", "中班"},
            {"中文表达练习", "称呼", "中班"},
            {"中文表达练习", "颜色", "中班"},
            {"中文表达练习", "动物", "中班"},
            {"中文表达练习", "方位", "中班"},
            {"中文表达练习", "建筑", "中班"},
            {"中文表达练习", "人体", "中班"},
            {"中文表达练习", "人物", "中班"},
            {"中文表达练习", "数字", "中班"},
            {"中文表达练习", "物品", "中班"},
            {"中文表达练习", "自然", "中班"},
            {"中文表达练习", "对比", "中班"},
            {"中文表达练习", "乐园", "中班"},
            {"中文表达练习", "拼音", "大班"},
            {"中文表达练习", "词语", "大班"},
            {"中文表达练习", "近义词", "大班"},
            {"中文表达练习", "反义词", "大班"},
            {"中文表达练习", "动物", "大班"},
            {"中文表达练习", "建筑", "大班"},
            {"中文表达练习", "食物", "大班"},
            {"中文表达练习", "人体", "大班"},
            {"中文表达练习", "动作", "大班"},
            {"中文表达练习", "量词", "大班"},
            {"中文表达练习", "自然", "大班"},
            {"中文表达练习", "物品", "大班"},
            {"英文表达练习", "字母", "小班"},
            {"英文表达练习", "动物", "小班"},
            {"英文表达练习", "颜色", "小班"},
            {"英文表达练习", "水果", "小班"},
            {"英文表达练习", "动作", "小班"},
            {"英文表达练习", "身体部位", "小班"},
            {"英文表达练习", "家居", "小班"},
            {"英文表达练习", "交通工具", "小班"},
            {"英文表达练习", "玩具", "中班"},
            {"英文表达练习", "水果", "中班"},
            {"英文表达练习", "数字", "中班"},
            {"英文表达练习", "蔬菜", "中班"},
            {"英文表达练习", "节日", "中班"},
            {"英文表达练习", "乐器", "中班"},
            {"英文表达练习", "爱好", "中班"},
            {"英文表达练习", "学校", "中班"},
            {"英文表达练习", "工具", "中班"},
            {"英文表达练习", "动物", "中班"},
            {"英文表达练习", "颜色", "中班"},
            {"英文表达练习", "动作", "中班"},
            {"英文表达练习", "身体部位", "中班"},
            {"英文表达练习", "食物", "中班"},
            {"英文表达练习", "场所", "中班"},
            {"英文表达练习", "体型", "中班"},
            {"英文表达练习", "头部器官", "中班"},
            {"英文表达练习", "会话", "中班"},
            {"英文表达练习", "饮料", "中班"},
            {"英文表达练习", "动物", "大班"},
            {"英文表达练习", "动作", "大班"},
            {"英文表达练习", "家居", "大班"},
            {"英文表达练习", "人物", "大班"},
            {"英文表达练习", "数字", "大班"},
            {"英文表达练习", "工具", "大班"},
            {"英文表达练习", "季节", "大班"},
            {"英文表达练习", "交通工具", "大班"},
            {"英文表达练习", "属性", "大班"},
            {"英文表达练习", "衣物", "大班"},
            {"英文表达练习", "运动", "大班"},
            {"英文表达练习", "职业身份", "大班"},
            {"英文表达练习", "感觉", "大班"},
            {"英文表达练习", "情感", "大班"},
            {"英文表达练习", "玩具", "大班"},
            {"英文表达练习", "社交", "大班"},
            {"英文表达练习", "食物", "大班"},
            {"英文表达练习", "天气", "大班"},
            {"英文表达练习", "形状", "大班"},
            {"逻辑思维练习", "1种物理特征分类", "小班"},
            {"逻辑思维练习", "比较长短", "小班"},
            {"逻辑思维练习", "比较轻重", "小班"},
            {"逻辑思维练习", "图形认知-启蒙", "小班"},
            {"逻辑思维练习", "配对", "小班"},
            {"逻辑思维练习", "比较大小", "小班"},
            {"逻辑思维练习", "图形拼组-启蒙", "小班"},
            {"逻辑思维练习", "比较薄厚", "小班"},
            {"逻辑思维练习", "比较多少", "小班"},
            {"逻辑思维练习", "轮廓对应-启蒙", "小班"},
            {"逻辑思维练习", "找不同", "小班"},
            {"逻辑思维练习", "发生规律", "小班"},
            {"逻辑思维练习", "图形认知-基础", "中班"},
            {"逻辑思维练习", "不同角度观察-基础", "中班"},
            {"逻辑思维练习", "行为类比", "中班"},
            {"逻辑思维练习", "排列规律", "中班"},
            {"逻辑思维练习", "颜色规律", "中班"},
            {"逻辑思维练习", "等分-基础", "中班"},
            {"逻辑思维练习", "分辨远近", "中班"},
            {"逻辑思维练习", "镜像-基础", "中班"},
            {"逻辑思维练习", "轮廓对应-基础", "中班"},
            {"逻辑思维练习", "空间知觉", "中班"},
            {"逻辑思维练习", "找相同", "中班"},
            {"逻辑思维练习", "平面图形展开", "中班"},
            {"逻辑思维练习", "立体图形展开", "中班"},
            {"逻辑思维练习", "根据1-2个条件推理", "中班"},
            {"逻辑思维练习", "方位类比", "中班"},
            {"逻辑思维练习", "大小类比", "中班"},
            {"逻辑思维练习", "整体与部分-基础", "中班"},
            {"逻辑思维练习", "对称-进阶", "大班"},
            {"逻辑思维练习", "等量代换", "大班"},
            {"逻辑思维练习", "心理旋转", "大班"},
            {"逻辑思维练习", "数量规律", "大班"},
            {"逻辑思维练习", "不同角度观察-进阶", "大班"},
            {"逻辑思维练习", "立体图形折叠", "大班"},
            {"逻辑思维练习", "镜像-进阶", "大班"},
            {"逻辑思维练习", "数量类比", "大班"},
            {"逻辑思维练习", "运算规律-进阶", "大班"},
            {"百科常识练习", "化学反应", "小班"},
            {"百科常识练习", "交通工具", "小班"},
            {"百科常识练习", "生物常识", "小班"},
            {"百科常识练习", "生活认知", "小班"},
            {"百科常识练习", "人文地理", "中班"},
            {"百科常识练习", "民俗节气", "中班"},
            {"百科常识练习", "职业认知", "中班"},
            {"百科常识练习", "自然地理", "中班"},
            {"百科常识练习", "气候常识", "中班"},
            {"百科常识练习", "科学常识", "中班"},
            {"百科常识练习", "爱国知识", "中班"},
            {"百科常识练习", "安全标志", "中班"},
            {"百科常识练习", "灾难自救", "中班"},
            {"百科常识练习", "生活安全", "中班"},
            {"百科常识练习", "植物常识", "中班"},
            {"百科常识练习", "天体知识", "大班"},
            {"百科常识练习", "恐龙知识", "大班"},
            {"百科常识练习", "文史常识", "大班"},
            {"百科常识练习", "天文常识", "大班"},
            {"专注记忆练习", "听觉专注", "小班"},
            {"专注记忆练习", "记位置", "中班"},
            {"专注记忆练习", "记物品", "大班"},
            {"专注记忆练习", "记顺序", "大班"},
            {"数学运算练习", "10以内数的认识", "小班"},
            {"数学运算练习", "10以内数量对应", "小班"},
            {"数学运算练习", "10以内比较大小", "小班"},
            {"数学运算练习", "平面图形", "小班"},
            {"数学运算练习", "5以内加法", "小班"},
            {"数学运算练习", "5以内减法", "小班"},
            {"数学运算练习", "序数", "中班"},
            {"数学运算练习", "10以内数的分合", "中班"},
            {"数学运算练习", "11-20数的认识", "中班"},
            {"数学运算练习", "统计", "中班"},
            {"数学运算练习", "群数", "中班"},
            {"数学运算练习", "10以内加法", "中班"},
            {"数学运算练习", "10以内减法", "中班"},
            {"数学运算练习", "10以内连加", "中班"},
            {"数学运算练习", "10以内连减", "中班"},
            {"数学运算练习", "10以内加减混合", "中班"},
            {"数学运算练习", "10以内运算比较大小", "中班"},
            {"数学运算练习", "单双数", "中班"},
            {"数学运算练习", "数的排列", "中班"},
            {"数学运算练习", "21-40数量对应", "中班"},
            {"数学运算练习", "百以内比较大小", "大班"},
            {"数学运算练习", "数位", "大班"},
            {"数学运算练习", "20以内加法", "大班"},
            {"数学运算练习", "20以内减法", "大班"},
            {"数学运算练习", "20以内加减混合", "大班"},
            {"数学运算练习", "20以内比较大小", "大班"},
            {"数学运算练习", "认识钟表", "大班"},
            {"数学运算练习", "20以内连减", "大班"},
            {"数学运算练习", "认识方位", "大班"},
            {"数学运算练习", "20以内应用题", "大班"},
            {"数学运算练习", "20以内连加", "大班"},
            {"数学运算练习", "百以内整十加减法", "大班"},
            {"数学运算练习", "百以内两位数加法", "大班"},
            {"数学运算练习", "百以内两位数减法", "大班"},
            {"数学运算练习", "百以内连加", "大班"},
            {"数学运算练习", "百以内连减", "大班"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        init();

        Intent service = new Intent(this, DingDangService.class);
        startService(service);
    }

    /**
     * 初始化
     */
    private void init() {
        lvDemo = (ListView) findViewById(R.id.lv_demo);

        String[] showList = new String[contentList.length];
        Log.i(TAG, "showList.length" + showList.length);
        for(int i = 0;i < showList.length;i++){
            showList[i] = Integer.toString(i) + "." + contentList[i][0] + "--" + contentList[i][1] + "--" + contentList[i][2];
            Log.i(TAG, "showList[i]:" + showList[i]);
        }

        lvDemo.setAdapter(new ArrayAdapter<String>(this, R.layout.item, R.id.lv_name, showList));
        lvDemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "test----contentList[position][0]:" + contentList[position][0]);
                Log.i(TAG, "test----contentList[position][1]:" + contentList[position][1]);
                Log.i(TAG, "test----contentList[position][2]:" + contentList[position][2]);
                startDDContent(contentList[position][0], contentList[position][1], contentList[position][2]);
            }
        });
    }

    /**
     * 进入叮当学习应用功能
     *
     * @param key:   模块名称
     * @param param: 模块子内容参数
     * @param age:   用户年龄，可为null
     */
    private void startDDContent(String key, String param, String age) {
        Uri uri = Uri.parse("dingdang://content?key=" + key + "&param=" + param + "&age=" + age);
        Intent intent = new Intent("com.robosys.dingdang.content");
        intent.setData(uri);
        startActivity(intent);
    }

    void test() {
        //一级菜单，听一听需要安装第三方应用
        startDDContent("学一学", "", "");
        startDDContent("测一测", "", "");
        startDDContent("练一练", "", "");
        startDDContent("宝箱", "", "");
        startDDContent("听一听", "", "");

        //学一学二级菜单
        startDDContent("叮当推理", "", "");
        startDDContent("逻辑拼图", "", "");
        startDDContent("逻辑推理", "", "");
        startDDContent("同步课堂", "", "");
        startDDContent("叮当单词", "", "");
        startDDContent("叮当拼音", "", "");
        startDDContent("叮当识字", "", "");
        startDDContent("叮当字母", "", "");
        startDDContent("综合测试", "", "");

        //测一测二级菜单
        startDDContent("综合测试", "", "");
        startDDContent("逻辑思维测试", "", "");
        startDDContent("英文表达测试", "", "");
        startDDContent("中文表达测试", "", "");
        startDDContent("专注记忆测试", "", "");
        startDDContent("习惯养成测试", "", "");
        startDDContent("百科常识测试", "", "");
        startDDContent("数据运算测试", "", "");
        startDDContent("幼小衔接测试", "", "");

        //练一练二级菜单
        startDDContent("中文表达练习", "", "");
        startDDContent("英文表达练习", "", "");
        startDDContent("逻辑思维练习", "", "");
        startDDContent("百科常识练习", "", "");
        startDDContent("习惯养成练习", "", "");
        startDDContent("专注记忆练习", "", "");
        startDDContent("数学运算练习", "", "");

        //宝箱二级菜单
        startDDContent("小火车", "", "");
        startDDContent("打海盗", "", "");
        startDDContent("打地鼠", "", "");
        startDDContent("踢足球", "", "");
        startDDContent("逻辑狗", "", "");
        startDDContent("魔法师", "", "");


        //叮当单词三级菜单
        startDDContent("叮当单词", "水果", "");
        startDDContent("叮当单词", "食物", "");
        startDDContent("叮当单词", "身体部位", "");
        startDDContent("叮当单词", "水果", "");
        startDDContent("叮当单词", "食物", "");
        startDDContent("叮当单词", "节日", "");
        startDDContent("叮当单词", "场所", "");
        startDDContent("叮当单词", "数字", "");
        startDDContent("叮当单词", "头部器官", "");
        startDDContent("叮当单词", "动物", "");
        startDDContent("叮当单词", "颜色", "");
        startDDContent("叮当单词", "动作", "");
        startDDContent("叮当单词", "家居", "");
        startDDContent("叮当单词", "交通工具", "");
        startDDContent("叮当单词", "玩具", "");
        startDDContent("叮当单词", "衣物", "");
        startDDContent("叮当单词", "蔬菜", "");
        startDDContent("叮当单词", "人物", "");
        startDDContent("叮当单词", "乐器", "");
        startDDContent("叮当单词", "礼貌用语", "");
        startDDContent("叮当单词", "爱好", "");
        startDDContent("叮当单词", "学校", "");
        startDDContent("叮当单词", "工具", "");
        startDDContent("叮当单词", "时间", "");
        startDDContent("叮当单词", "体型", "");


        //叮当拼音三级菜单
        startDDContent("叮当拼音", "单韵母第一课", "");
        startDDContent("叮当拼音", "单韵母第二课", "");
        startDDContent("叮当拼音", "声母第一课", "");
        startDDContent("叮当拼音", "声母第二课", "");
        startDDContent("叮当拼音", "声母第三课", "");
        startDDContent("叮当拼音", "声母第四课", "");
        startDDContent("叮当拼音", "声母第五课", "");
        startDDContent("叮当拼音", "声母第六课", "");
        startDDContent("叮当拼音", "复韵母第一课", "");
        startDDContent("叮当拼音", "复韵母第二课", "");
        startDDContent("叮当拼音", "复韵母第三课", "");
        startDDContent("叮当拼音", "前鼻韵母", "");
        startDDContent("叮当拼音", "后鼻韵母", "");
        startDDContent("叮当拼音", "整体认读音节第一课", "");
        startDDContent("叮当拼音", "整体认读音节第二课", "");
        startDDContent("叮当拼音", "整体认读音节第三课", "");
        startDDContent("叮当拼音", "整体认读音节第四课", "");
        startDDContent("叮当拼音", "整体认读音节第五课", "");

        //叮当识字三级菜单
        startDDContent("叮当识字", "乐园", "");
        startDDContent("叮当识字", "数字", "");
        startDDContent("叮当识字", "自然", "");
        startDDContent("叮当识字", "方位", "");
        startDDContent("叮当识字", "称呼", "");
        startDDContent("叮当识字", "量词", "");
        startDDContent("叮当识字", "动物", "");
        startDDContent("叮当识字", "对比", "");
        startDDContent("叮当识字", "动作", "");
        startDDContent("叮当识字", "人物", "");
        startDDContent("叮当识字", "颜色", "");
        startDDContent("叮当识字", "时间", "");
        startDDContent("叮当识字", "人体", "");
        startDDContent("叮当识字", "五行", "");
        startDDContent("叮当识字", "文具", "");
        startDDContent("叮当识字", "植物", "");
        startDDContent("叮当识字", "食物", "");
        startDDContent("叮当识字", "学习", "");
        startDDContent("叮当识字", "交通", "");
        startDDContent("叮当识字", "爱好", "");
        startDDContent("叮当识字", "气象", "");
        startDDContent("叮当识字", "建筑", "");
        startDDContent("叮当识字", "人休", "");
        startDDContent("叮当识字", "物品", "");

        //叮当字母三级菜单
        startDDContent("叮当字母", "第一课", "");
        startDDContent("叮当字母", "第二课", "");
        startDDContent("叮当字母", "第三课", "");
        startDDContent("叮当字母", "第四课", "");
        startDDContent("叮当字母", "第五课", "");
        startDDContent("叮当字母", "第六课", "");
        startDDContent("叮当字母", "第七课", "");
        startDDContent("叮当字母", "第八课", "");

        //中文表达练习二级菜单小班
        startDDContent("中文表达练习", "古诗", "小班");
        startDDContent("中文表达练习", "数字", "小班");
        startDDContent("中文表达练习", "人体", "小班");
        startDDContent("中文表达练习", "自然", "小班");
        startDDContent("中文表达练习", "五行", "小班");
        startDDContent("中文表达练习", "方位", "小班");
        startDDContent("中文表达练习", "称呼", "小班");
        startDDContent("中文表达练习", "量词", "小班");
        startDDContent("中文表达练习", "动物", "小班");
        startDDContent("中文表达练习", "对比", "小班");
        startDDContent("中文表达练习", "动作", "小班");
        startDDContent("中文表达练习", "文具", "小班");
        startDDContent("中文表达练习", "植物", "小班");
        startDDContent("中文表达练习", "人物", "小班");
        startDDContent("中文表达练习", "食物", "小班");
        startDDContent("中文表达练习", "学习", "小班");
        startDDContent("中文表达练习", "交通", "小班");
        startDDContent("中文表达练习", "颜色", "小班");
        startDDContent("中文表达练习", "爱好", "小班");
        startDDContent("中文表达练习", "气象", "小班");
        startDDContent("中文表达练习", "时间", "小班");
        startDDContent("中文表达练习", "识字", "中班");
        startDDContent("中文表达练习", "拼音", "中班");
        startDDContent("中文表达练习", "量词", "中班");

        //中文表达练习二级菜单中班
        startDDContent("中文表达练习", "反义词", "中班");
        startDDContent("中文表达练习", "称呼", "中班");
        startDDContent("中文表达练习", "颜色", "中班");
        startDDContent("中文表达练习", "动物", "中班");
        startDDContent("中文表达练习", "方位", "中班");
        startDDContent("中文表达练习", "建筑", "中班");
        startDDContent("中文表达练习", "人体", "中班");
        startDDContent("中文表达练习", "人物", "中班");
        startDDContent("中文表达练习", "数字", "中班");
        startDDContent("中文表达练习", "物品", "中班");
        startDDContent("中文表达练习", "自然", "中班");
        startDDContent("中文表达练习", "对比", "中班");
        startDDContent("中文表达练习", "乐园", "中班");

        //中文表达练习二级菜单大班
        startDDContent("中文表达练习", "拼音", "大班");
        startDDContent("中文表达练习", "词语", "大班");
        startDDContent("中文表达练习", "近义词", "大班");
        startDDContent("中文表达练习", "反义词", "大班");
        startDDContent("中文表达练习", "动物", "大班");
        startDDContent("中文表达练习", "建筑", "大班");
        startDDContent("中文表达练习", "食物", "大班");
        startDDContent("中文表达练习", "人体", "大班");
        startDDContent("中文表达练习", "动作", "大班");
        startDDContent("中文表达练习", "量词", "大班");
        startDDContent("中文表达练习", "自然", "大班");
        startDDContent("中文表达练习", "物品", "大班");

        //英文表达练习二级菜单小班
        startDDContent("英文表达练习", "字母", "小班");
        startDDContent("英文表达练习", "动物", "小班");
        startDDContent("英文表达练习", "颜色", "小班");
        startDDContent("英文表达练习", "水果", "小班");
        startDDContent("英文表达练习", "动作", "小班");
        startDDContent("英文表达练习", "身体部位", "小班");
        startDDContent("英文表达练习", "家居", "小班");
        startDDContent("英文表达练习", "食物交通工具", "小班");

        //英文表达练习二级菜单中班
        startDDContent("英文表达练习", "玩具", "中班");
        startDDContent("英文表达练习", "水果", "中班");
        startDDContent("英文表达练习", "数字", "中班");
        startDDContent("英文表达练习", "蔬菜", "中班");
        startDDContent("英文表达练习", "节日", "中班");
        startDDContent("英文表达练习", "乐器", "中班");
        startDDContent("英文表达练习", "爱好", "中班");
        startDDContent("英文表达练习", "学校", "中班");
        startDDContent("英文表达练习", "工具", "中班");
        startDDContent("英文表达练习", "动物", "中班");
        startDDContent("英文表达练习", "颜色", "中班");
        startDDContent("英文表达练习", "动作", "中班");
        startDDContent("英文表达练习", "身体部位", "中班");
        startDDContent("英文表达练习", "食物", "中班");
        startDDContent("英文表达练习", "场所", "中班");
        startDDContent("英文表达练习", "体型", "中班");
        startDDContent("英文表达练习", "头部器官", "中班");
        startDDContent("英文表达练习", "会话", "中班");
        startDDContent("英文表达练习", "饮料", "中班");

        //英文表达练习二级菜单大班
        startDDContent("英文表达练习", "动物", "大班");
        startDDContent("英文表达练习", "动作", "大班");
        startDDContent("英文表达练习", "家居", "大班");
        startDDContent("英文表达练习", "人物", "大班");
        startDDContent("英文表达练习", "数字", "大班");
        startDDContent("英文表达练习", "工具", "大班");
        startDDContent("英文表达练习", "季节", "大班");
        startDDContent("英文表达练习", "交通工具", "大班");
        startDDContent("英文表达练习", "属性", "大班");
        startDDContent("英文表达练习", "衣物", "大班");
        startDDContent("英文表达练习", "运动", "大班");
        startDDContent("英文表达练习", "职业身份", "大班");
        startDDContent("英文表达练习", "感觉", "大班");
        startDDContent("英文表达练习", "情感", "大班");
        startDDContent("英文表达练习", "玩具", "大班");
        startDDContent("英文表达练习", "社交", "大班");
        startDDContent("英文表达练习", "食物", "大班");
        startDDContent("英文表达练习", "天气", "大班");
        startDDContent("英文表达练习", "形状", "大班");

        //逻辑思维练习三级菜单小班
        startDDContent("逻辑思维练习", "1种物理特征分类", "小班");
        startDDContent("逻辑思维练习", "比较长短", "小班");
        startDDContent("逻辑思维练习", "比较轻重", "小班");
        startDDContent("逻辑思维练习", "图形认知-启蒙", "小班");
        startDDContent("逻辑思维练习", "配对", "小班");
        startDDContent("逻辑思维练习", "比较大小", "小班");
        startDDContent("逻辑思维练习", "图形拼组-启蒙", "小班");
        startDDContent("逻辑思维练习", "比较薄厚", "小班");
        startDDContent("逻辑思维练习", "比较多少", "小班");
        startDDContent("逻辑思维练习", "轮廓对应-启蒙", "小班");
        startDDContent("逻辑思维练习", "找不同", "小班");
        startDDContent("逻辑思维练习", "发生规律", "小班");

        //逻辑思维练习三级菜单中班
        startDDContent("逻辑思维练习", "图形认知-基础", "中班");
        startDDContent("逻辑思维练习", "不同角度观察-基础", "中班");
        startDDContent("逻辑思维练习", "行为类比", "中班");
        startDDContent("逻辑思维练习", "排列规律", "中班");
        startDDContent("逻辑思维练习", "颜色规律", "中班");
        startDDContent("逻辑思维练习", "等分-基础", "中班");
        startDDContent("逻辑思维练习", "分辨远近", "中班");
        startDDContent("逻辑思维练习", "镜像-基础", "中班");
        startDDContent("逻辑思维练习", "轮廓对应-基础", "中班");
        startDDContent("逻辑思维练习", "空间知觉", "中班");
        startDDContent("逻辑思维练习", "找相同", "中班");
        startDDContent("逻辑思维练习", "平面图形展开", "中班");
        startDDContent("逻辑思维练习", "立体图形展开", "中班");
        startDDContent("逻辑思维练习", "根据1-2个条件推理", "中班");
        startDDContent("逻辑思维练习", "方位类比", "中班");
        startDDContent("逻辑思维练习", "大小类比", "中班");
        startDDContent("逻辑思维练习", "整体与部分-基础", "中班");

        //逻辑思维练习三级菜单大班
        startDDContent("逻辑思维练习", "对称-进阶", "大班");
        startDDContent("逻辑思维练习", "等量代换", "大班");
        startDDContent("逻辑思维练习", "心理旋转", "大班");
        startDDContent("逻辑思维练习", "数量规律", "大班");
        startDDContent("逻辑思维练习", "不同角度观察-进阶", "大班");
        startDDContent("逻辑思维练习", "立体图形折叠", "大班");
        startDDContent("逻辑思维练习", "镜像-进阶", "大班");
        startDDContent("逻辑思维练习", "数量类比", "大班");
        startDDContent("逻辑思维练习", "运算规律-进阶", "大班");

        //百科常识练习三级菜单小班
        startDDContent("百科常识练习", "化学反应", "小班");
        startDDContent("百科常识练习", "交通工具", "小班");
        startDDContent("百科常识练习", "生物常识", "小班");
        startDDContent("百科常识练习", "生活认知", "小班");

        //百科常识练习三级菜单中班
        startDDContent("百科常识练习", "人文地理", "中班");
        startDDContent("百科常识练习", "民俗节气", "中班");
        startDDContent("百科常识练习", "职业认知", "中班");
        startDDContent("百科常识练习", "自然地理", "中班");
        startDDContent("百科常识练习", "气候常识", "中班");
        startDDContent("百科常识练习", "科学常识", "中班");
        startDDContent("百科常识练习", "爱国知识", "中班");
        startDDContent("百科常识练习", "安全标志", "中班");
        startDDContent("百科常识练习", "灾难自救", "中班");
        startDDContent("百科常识练习", "生活安全", "中班");
        startDDContent("百科常识练习", "植物常识", "中班");

        //百科常识练习三级菜单大班
        startDDContent("百科常识练习", "天体知识", "大班");
        startDDContent("百科常识练习", "恐龙知识", "大班");
        startDDContent("百科常识练习", "文史常识", "大班");
        startDDContent("百科常识练习", "天文常识", "大班");

        //专注记忆练习三级菜单小班
        startDDContent("专注记忆练习", "听觉专注", "小班");

        //专注记忆练习三级菜单中班
        startDDContent("专注记忆练习", "记位置", "中班");

        //专注记忆练习三级菜单中班
        startDDContent("专注记忆练习", "记物品", "大班");
        startDDContent("专注记忆练习", "记顺序", "大班");

        //数学运算练习三级菜单小班
        startDDContent("数学运算练习", "10以内数的认识", "小班");
        startDDContent("数学运算练习", "10以内数量对应", "小班");
        startDDContent("数学运算练习", "10以内比较大小", "小班");
        startDDContent("数学运算练习", "平面图形", "小班");
        startDDContent("数学运算练习", "5以内加法", "小班");
        startDDContent("数学运算练习", "5以内减法", "小班");

        //数学运算练习三级菜单中班
        startDDContent("数学运算练习", "序数", "中班");
        startDDContent("数学运算练习", "10以内数的分合", "中班");
        startDDContent("数学运算练习", "11-20数的认识", "中班");
        startDDContent("数学运算练习", "统计", "中班");
        startDDContent("数学运算练习", "群数", "中班");
        startDDContent("数学运算练习", "10以内加法", "中班");
        startDDContent("数学运算练习", "10以内减法", "中班");
        startDDContent("数学运算练习", "10以内连加", "中班");
        startDDContent("数学运算练习", "10以内连减", "中班");
        startDDContent("数学运算练习", "10以内加减混合", "中班");
        startDDContent("数学运算练习", "10以内运算比较大小", "中班");
        startDDContent("数学运算练习", "单双数", "中班");
        startDDContent("数学运算练习", "数的排列", "中班");
        startDDContent("数学运算练习", "21-40数量对应", "中班");

        //数学运算练习三级菜单大班
        startDDContent("数学运算练习", "百以内比较大小", "大班");
        startDDContent("数学运算练习", "数位", "大班");
        startDDContent("数学运算练习", "20以内加法", "大班");
        startDDContent("数学运算练习", "20以内减法", "大班");
        startDDContent("数学运算练习", "20以内加减混合", "大班");
        startDDContent("数学运算练习", "20以内比较大小", "大班");
        startDDContent("数学运算练习", "认识钟表", "大班");
        startDDContent("数学运算练习", "20以内连减", "大班");
        startDDContent("数学运算练习", "认识方位", "大班");
        startDDContent("数学运算练习", "20以内应用题", "大班");
        startDDContent("数学运算练习", "20以内连加", "大班");
        startDDContent("数学运算练习", "百以内整十加减法", "大班");
        startDDContent("数学运算练习", "百以内两位数加法", "大班");
        startDDContent("数学运算练习", "百以内两位数减法", "大班");
        startDDContent("数学运算练习", "百以内连加", "大班");
        startDDContent("数学运算练习", "百以内连减", "大班");
    }
}
