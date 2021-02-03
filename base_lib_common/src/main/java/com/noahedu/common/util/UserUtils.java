package com.noahedu.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：UserUtils$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/12/29$ 9:54$
 */
public class UserUtils {
    //用户中心uri
    public static final String TNAME = "personalinfo";
    public static final String AUTOHORITY = "com.noahedu.provider.personalinfo";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY + "/" + TNAME);

    static public class PersonalInfo {
        /**
         * 用户是云学习用户
         */
        public static final String ACCOUNT_PLATFORM_ANOAH = "anoah";

        /**
         * 用户是教育网用户
         */
        public static final String ACCOUNT_PLATFORM_EDU = "edu";

        /**
         * 正常注册：用户通过手动注册的注册
         */
        public static final int REGISTER_TYPE_NORMAL = 1;

        /**
         * 已注册：用户通过其他途径的注册
         */
        public static final int REGISTER_TYPE_OTHER_REGISTER_TYPE = 0;

        /**
         * 隐式注册：（自动）注册后，在使用这看来仍未注册
         */
        public static final int REGISTER_TYPE_IMPLICIT = 2;

        /**
         * 未注册
         */
        public static final int REGISTER_TYPE_NOT_REGISTERED = -1;

        /**
         * roleid 4: 柜台账户
         */
        public static final int ROLEID_COURTER = 4;

        /**
         * roleid 5: 普通账户
         */
        public static final int ROLEID_GENERAL = 5;

        /**
         * type 0: 游客
         */
        public static final int TYPE_VISITOR = 0;

        /**
         * type 1: 其他 柜员或者普通注册用户
         */
        // public static final int TYPE_OTHER = 1;

        /**
         * type 1: 普通注册用户
         */
        public static final int TYPE_GENERAL = 1;

        /**
         * type 2: 柜员
         */
        public static final int TYPE_COUNTER = 2;

        /**
         * 教育阶段：学前
         */
        public static final int STAGE_XQ = 1;

        /**
         * 教育阶段：小学
         */
        public static final int STAGE_XX = 2;

        /**
         * 教育阶段：初中
         */
        public static final int STAGE_CZ = 3;

        /**
         * 教育阶段：高中
         */
        public static final int STAGE_GZ = 4;

        /**
         * hint_register_flag 1: 开启弹出提示框
         */
        public static final int HINT_REGISTER_FLAG_ON = 1;

        /**
         * hint_register_flag 1: 关闭弹出提示框
         */
        public static final int HINT_REGISTER_FLAG_OFF = 0;

        public static final String UID = "uid";

        public static final String USER_FLAG = "user_flag";

        public static final String _ID = "_id";

        public static final String PW = "pw";

        public static final String CURRENT = "current";

        public static final String USERNAME = "username";

        public static final String REALNAME = "realname";

        public static final String EMAIL = "email";

        public static final String REMARK = "remark";

        public static final String SEX = "sex";

        public static final String EDU_SYSTEM = "edu_system";

        public static final String EDU_STAGE = "edu_stage";

        public static final String GRADE_ID = "grade_id";

        public static final String CLASS_NAME = "class_name";

        public static final String SCHOOL_NAME = "school_name";

        public static final String SCHOOL_ID = "school_id";

        public static final String PHONE = "phone";

        public static final String IDOL = "idol"; // 偶像

        public static final String BLOOD = "blood";

        public static final String CONSTELLATION = "constellation"; // 星座

        public static final String HOBBY = "hobby";

        public static final String PROVINCE_NAME = "province_name";

        public static final String PROVINCE_ID = "province_id";

        public static final String CITY_NAME = "city_name";

        public static final String CITY_ID = "city_id";

        public static final String DISTRICT_NAME = "district_name";

        public static final String DISTRICT_ID = "district_id";

        public static final String QQ = "qq";

        public static final String BIRTHDAY = "birthday";

        public static final String REGISTER_TYPE = "register_type"; // 其他，1.正常，2.绑定

        public static final String TYPE = "type"; // 0游客, 1注册用户 ,2柜台

        public static final String ROLE_ID = "roleid"; // 4柜员，5.答疑学生

        public static final String TEMP_ROLE_ID = "temp_roleid"; // 临时角色(在某些机身编码上登录的账号拥有柜员的权限)

        public static final String MACHINE_NO = "machine_no"; // 机身编码

        public static final String CURRENT_AREA = "current_area";

        // 双指无界面切换用户：切换到下一个阶段的用户时，选择这个阶段中最后一次使用的用户
        // 注意,U30上已经改成切换学段了,故此参数的含义修改为中学界面 用户最后一次设置的是初中还是高中(3为初中,4为高中)
        public static final String STAGE_CURRENT = "stage_current";

        public static final String HEAD = "head";

        public static final String HEAD_URL = "headUrl";

        /**
         * 用户信息的更新时间。仅对已已登录用户意义。yyyy-MM-dd HH:mm:ss
         */
        public static final String UPDATE_TIME = "update_time";

        /**
         * 用户所在的平台标识: edu(用户是教育网用户) anoah（用户是云学习用户）
         */
        public static final String ACCOUNT_PLATFORM = "account_platform";

        /**
         * 密码种子
         */
        public static final String PW_SEED = "noahedu";

        // 对于未注册或者未详细注册的用户是否弹出注册提示框 1 弹出 , 0 不弹
        public static final String HINT_REGISTER_FLAG = "hint_register_flag";

        // 教材版本
        public static final String SUBJECTID = "subjectid";

        public static final String SUBJECTNAME = "subjectname";

        public static final String BOKEDITIONID = "bokeditionid";

        public static final String BOKEDITIONNAME = "bokeditionname";

        // 已经弹出登录用户奖励U币的提示框(yyyy-MM-dd)
        public static final String HINT_DATE = "hint_date";

        // 备份用户自己设置的各个学段的年级、班级、学校等信息(只保存在本地)
        public static final String XQ_GRADE_ID = "xq_grade_id";

        public static final String XQ_CLASS_NAME = "xq_class_name";

        public static final String XQ_SCHOOL_NAME = "xq_school_name";

        public static final String XQ_SCHOOL_ID = "xq_school_id";

        public static final String XX_GRADE_ID = "xx_grade_id";

        public static final String XX_CLASS_NAME = "xx_class_name";

        public static final String XX_SCHOOL_NAME = "xx_school_name";

        public static final String XX_SCHOOL_ID = "xx_school_id";

        public static final String CZ_GRADE_ID = "cz_grade_id";

        public static final String CZ_CLASS_NAME = "cz_class_name";

        public static final String CZ_SCHOOL_NAME = "cz_school_name";

        public static final String CZ_SCHOOL_ID = "cz_school_id";

        public static final String GZ_GRADE_ID = "gz_grade_id";

        public static final String GZ_CLASS_NAME = "gz_class_name";

        public static final String GZ_SCHOOL_NAME = "gz_school_name";

        public static final String GZ_SCHOOL_ID = "gz_school_id";

        public static boolean isRegisteredUser(String userid) {
            if (TextUtils.isEmpty(userid) || userid.equals("null") || userid.startsWith("0")) {
                return false;
            } else {
                return true;
            }
        }
    }

     static public class UserInfo extends Object{
        String realname;
        String phone;
        String gradeID;
        String schoolName;
        String province_name;
        String city_name;
        String district_name;
        String edu_stage;
        String sex;
        String user_id;

        UserInfo(){

        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGradeID() {
            return gradeID;
        }

        public void setGradeID(String gradeID) {
            this.gradeID = gradeID;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }

         public String getEdu_stage() {
             return edu_stage;
         }

         public void setEdu_stage(String edu_stage) {
             this.edu_stage = edu_stage;
         }

         public String getSex() {
             return sex;
         }

         public void setSex(String sex) {
             this.sex = sex;
         }

         public String getUser_id() {
             return user_id;
         }

         public void setUser_id(String user_id) {
             this.user_id = user_id;
         }

         @Override
         public String toString() {
             return "UserInfo{" +
                     "realname='" + realname + '\'' +
                     ", phone='" + phone + '\'' +
                     ", gradeID='" + gradeID + '\'' +
                     ", schoolName='" + schoolName + '\'' +
                     ", province_name='" + province_name + '\'' +
                     ", city_name='" + city_name + '\'' +
                     ", district_name='" + district_name + '\'' +
                     ", edu_stage='" + edu_stage + '\'' +
                     ", sex='" + sex + '\'' +
                     ", user_id='" + user_id + '\'' +
                     '}';
         }
     }
    /**
     * 获取用户信息
     * */
    static public UserInfo getUserInfo(Context mContext) {
        //获取用户信息
        UserInfo userInfo = new UserInfo();
        if(mContext != null)
        {
            ContentResolver resolver = mContext.getContentResolver();
            Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    userInfo.setRealname(cursor.getString(cursor.getColumnIndex(PersonalInfo.REALNAME)));
                    userInfo.setCity_name(cursor.getString(cursor.getColumnIndex(PersonalInfo.CITY_NAME)));
                    userInfo.setPhone(cursor.getString(cursor.getColumnIndex(PersonalInfo.PHONE)));
                    userInfo.setProvince_name(cursor.getString(cursor.getColumnIndex(PersonalInfo.PROVINCE_NAME)));
                    userInfo.setDistrict_name(cursor.getString(cursor.getColumnIndex(PersonalInfo.DISTRICT_NAME)));
                    userInfo.setEdu_stage(cursor.getString(cursor.getColumnIndex(PersonalInfo.EDU_STAGE)));
                    userInfo.setSex(cursor.getString(cursor.getColumnIndex(PersonalInfo.SEX)));
                    userInfo.setSchoolName(cursor.getString(cursor.getColumnIndex(PersonalInfo.SCHOOL_NAME)));
                    userInfo.setGradeID(cursor.getString(cursor.getColumnIndex(PersonalInfo.GRADE_ID)));
                    userInfo.setUser_id(cursor.getString(cursor.getColumnIndex(PersonalInfo.UID)));
                }
                cursor.close();
            }

        }
        return userInfo;
    }

    private final static String COM_NOAHEDU_SYSTEM_NAME = "com.noahedu.fw.noahsys.util.NoahProduct";
    /**
     * Returns the current device id.
     * load <uses-library android:name="com.noahedu" android:required="false"/>
     */
    public static String getProductID() {
        String productID_ = null;

        try {
            final Class<?> cls = Class.forName(COM_NOAHEDU_SYSTEM_NAME);
            final Method isInitializedMethod = cls.getMethod("getProductID", (Class[]) null);
            Object result = null;
            try {
                result = isInitializedMethod.invoke(null, (Object[]) null);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (result instanceof String) {
                productID_ = (String) result;
            }
        }
        catch (ClassNotFoundException ignored) {
            ignored.printStackTrace();
        }
        catch (NoSuchMethodException ignored) {
            ignored.printStackTrace();
        }
        catch (InvocationTargetException ignored) {
            ignored.printStackTrace();
        }

        return productID_;
    }
    /**
     * Returns the current device name.
     * load <uses-library android:name="com.noahedu" />
     */
    public static String getProductName() {
        String productName_ = null;

        try {
            final Class<?> cls = Class.forName(COM_NOAHEDU_SYSTEM_NAME);
            final Method isInitializedMethod = cls.getMethod("getProductName", (Class[]) null);
            Object result = null;
            try {
                result = isInitializedMethod.invoke(null, (Object[]) null);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (result instanceof String) {
                productName_ = (String) result;
            }
        }
        catch (ClassNotFoundException ignored) {
            ignored.printStackTrace();
        }
        catch (NoSuchMethodException ignored) {
            ignored.printStackTrace();
        }
        catch (InvocationTargetException ignored) {
            ignored.printStackTrace();
        }

        return productName_;
    }
}
