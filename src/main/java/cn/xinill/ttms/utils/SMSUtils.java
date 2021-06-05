package cn.xinill.ttms.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

//导入可选配置类
// 导入 SMS 模块的 client
// 导入要请求接口对应的 request response 类

/**
 * @Author: Xinil
 * @Date: 2021/2/9 21:27
 */
public class SMSUtils {
    public static String sendLOG_ON(String phone){
        try {
            String key = ""+(new Random().nextInt(899999)+100000);
            SendSmsResponse response = SMSUtils.sendMessage(SMSTemplateID.LOG_ON, phone, key);
            if(response.getSendStatusSet()[0].getCode().equals("Ok")){
                return key;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String sendLOG_IN(String phone){
        try {
            String key = ""+(new Random().nextInt(899999)+100000);
            SendSmsResponse response = SMSUtils.sendMessage(SMSTemplateID.LOG_IN, phone, key);
            if(response.getSendStatusSet()[0].getCode().equals("Ok")){
                return key;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String sendUPDATE(String phone){
        try {
            String key = ""+(new Random().nextInt(899999)+100000);
            SendSmsResponse response = SMSUtils.sendMessage(SMSTemplateID.UPDATE, phone, key);
            if(response.getSendStatusSet()[0].getCode().equals("Ok")){
                return key;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SendSmsResponse sendMessage(SMSTemplateID templateID, String phone, String code) {
        try{
            ClassPathResource classPathResource = null;
            InputStream inputStream = null;
            Credential cred = null;
            try {
                classPathResource = new ClassPathResource("utils.properties");
                inputStream = classPathResource.getInputStream();
                Properties p = new Properties();
                p.load(inputStream);
                // 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId 和 secretKey
                cred = new Credential(p.getProperty("SMSKeyId"), p.getProperty("SMSKeySecret"));
            }catch (Exception e) {
                try {
                    if(inputStream != null){
                        inputStream.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
            // 实例化一个 http 选项，可选，无特殊需求时可以跳过
            HttpProfile httpProfile = new HttpProfile();

            /* SDK 会自动指定域名，通常无需指定域名，但访问金融区的服务时必须手动指定域名
             * 例如 SMS 的上海金融区域名为 sms.ap-shanghai-fsi.tencentcloudapi.com      */
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            /* 非必要步骤:实例化一个客户端配置对象，可以指定超时时间等配置 */
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            /* 实例化 SMS 的 client 对象
             * 第二个参数是地域信息，可以直接填写字符串 ap-guangzhou，或者引用预设的常量 */
            SmsClient client = new SmsClient(cred, "", clientProfile);

            //实例化SMS请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
            SendSmsRequest req = new SendSmsRequest();

            // 设置下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]，最多同时下发200个
            String[] phoneNumberSet1 = {"+86"+phone};
            req.setPhoneNumberSet(phoneNumberSet1);

            /* 模板参数: 若无模板参数，则设置为空*/
            String[] templateParamSet1 = {code};
            req.setTemplateParamSet(templateParamSet1);



            req.setSign("任基伟学习软件工程开发");//签名
            req.setTemplateID(""+templateID.getCode());//模板
            req.setSmsSdkAppid("1400485543");//SDKAppId
            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的是一个 SendSmsResponse 类的实例，与请求对象对应 */

            return client.SendSms(req);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return null;
    }

}