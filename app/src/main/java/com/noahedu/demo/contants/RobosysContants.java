package com.noahedu.demo.contants;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：RobosysContants$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/18$ 14:56$
 */
public class RobosysContants {

    public  final static String ACCOUNT_TYPE = "app";
    public  final static String APP_ID = "r_z16nopwygoqt6vafhq";
    public  final static String SECRET_KEY ="cy0lfg9il3odyamg6tmm2asvyxbnu27q";
    public final static String  ROBOSYS_FLAG = "robogo2020";

    //public static final String ROBOSYS_TOKEN_URL = "https://openapi.data-baker.com/oauth/2.0/token?grant_type=client_credentials&client_secret="+client_secret+"&client_id="+client_id;

    //	 https://openapi.data-baker.com/tts?access_token=***&language=***&domain=***&text=***
   // public static final String ROBOSYS_URL = "https://openapi.data-baker.com/tts";

    //获取token
    /*account_type - 此处只能是"app"
        appid - 系统分配给当前API用户的唯一编码
        deviceid - API用户需要提供deviceid用来标识每一台具体的设备，不能有重复，否则会引起问题
        password - API用户自己定义一套算法，自动生成密码，最简单的可以使用md5(deviceid+appid+自定义的特殊标记)
    */
    public static final String  LOGIN_URL ="https://api.robosys.cc/edu/api/auth/login";

    //https://api.robosys.cc/edu/api/opencourse?appid=APPID&id=173&menu=opencourse&sn=1&timestamp=2019-12-14T16:12:30&token=TOKEN&type=page&sign=5b19fd04bfe1095d539efa345edfa704
    /*appid - 每个用户可以有多个产品类别，每个类别对应一个appid
    sn - 每个产品（appid），会有很多个终端设备，sn是每个设备的唯一标识
    id - 每个产品（appid）， 所授权的内容的唯一编号
    type - 内容的类型， type=page， 是课件页， type=course，是课件
    timestamp - 时间戳，当前的时间UTC格式2019-12-14T16:12:30，注意中间的T，表示UTC格式
    sign - 签名，每次请求，为保证不被篡改，都需要携带签名。
    token - 登录时候返回的TOKEN，每次请求必须携带*/
    public static final String COURSE_URL ="https://api.robosys.cc/edu/api/opencourse";
}
