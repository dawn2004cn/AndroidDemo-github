/**
 * © 2019 www.youxuepai.com
 * @file name：TTSConstants.java.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2019-7-25下午7:02:51
 * @version 1.0
 */
package com.noahedu.common.constant;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：TTSConstants
 *  描述：简单描述该类的作用
 * @class name：TTSConstants
 * @anthor : daisg
 * @time 2019-7-25下午7:02:51
 * @version V1.0
 */
public class TTSConstants {
	 public static final String[] TTSSstandard_Roles = {"标准合成_甜美女声_楠楠",
		 "标准合成_标准女声_瑶瑶",
		 "标准合成_标准男生_阿科",
		 "标准合成_中文儿童_冉冉",
		 "标准合成_模仿儿童_小君", 	
		 "标准合成_成熟女声_小玉" ,
		 "标准合成_知性女声_冰儿" ,	
		 "标准合成_邻家女声_娇娇" ,	
		 "标准合成_美语女声_Lisa" ,		
		 "标准合成_美语男生_Troy" ,			
		 "标准合成_磁性男生_阿铭" ,			
		 "标准合成_萝莉女声_小菲" ,			
		 "标准合成_欢乐女童_乐乐" 	,		
		 "标准合成_模仿儿童_果子" };
	 public static final String[] TTSCharacteristic_Roles={	"特色合成_孙悟空",
		 "特色合成_星仔",
		 "特色合成_小锤子",
		 "特色合成_台湾女声_小美",
		 "特色合成_小说男生M3",  	
		 "特色合成_东北大叔" ,	
		 "特色合成_小腾", 	
		 "特色合成_粤语女声_阿紫" 	};
	 public static final String[] TTSPersonalized_Roles={	"个性化合成_老年男生_077",
		 "个性化合成_老年女声_088" ,	
		 "个性化合成_普通男生_013"	,	
		 "个性化合成_普通男生_094" ,
		"个性化合成_普通男生_101" 	,
		 "个性化合成_普通女声_031", 
		"个性化合成_普通女声_025" ,
		"个性化合成_普通女声_102" ,
		"个性化合成_童声_027" 	};
	 public static final String[] TTSIntegent_Role={"智能客服_静静",
		 "智能客服_果果", 	
		 "智能客服_小金" , 	
		 "智能客服_子衿"};
	 public static final String client_secret="NjU1N2ZhYTAtNjUzNi00OTI4LWIwNmYtYzE1N2JjMTZjOTA3";
	 public static final String client_id="1cae6918-0bc7-4220-934c-57e83a15d58e";
	 
	 public static final String TTS_TOKEN_URL = "https://openapi.data-baker.com/oauth/2.0/token?grant_type=client_credentials&client_secret="+client_secret+"&client_id="+client_id;
	 
	 //	 https://openapi.data-baker.com/tts?access_token=***&language=***&domain=***&text=***
	 public static final String TTS_URL = "https://openapi.data-baker.com/tts";
	 
	 
}