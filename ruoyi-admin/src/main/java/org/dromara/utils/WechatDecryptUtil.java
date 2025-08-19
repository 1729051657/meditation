package org.dromara.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 微信小程序数据解密工具类
 *
 * @author beauty
 */
@Slf4j
public class WechatDecryptUtil {

    /**
     * 解密微信手机号
     *
     * @param encryptedData 加密数据
     * @param iv            初始向量
     * @param sessionKey    会话密钥
     * @return 手机号
     */
    public static String decryptPhone(String encryptedData, String iv, String sessionKey) {
        try {
            // 解密
            String decryptedData = decrypt(encryptedData, iv, sessionKey);

            // 解析JSON获取手机号
            JSONObject jsonObject = JSONUtil.parseObj(decryptedData);
            String phoneNumber = jsonObject.getStr("phoneNumber");

            if (StrUtil.isEmpty(phoneNumber)) {
                throw new ServiceException("解密后未获取到手机号");
            }

            log.info("微信手机号解密成功：{}", phoneNumber);
            return phoneNumber;

        } catch (Exception e) {
            log.error("微信手机号解密失败", e);
            throw new ServiceException("手机号解密失败：" + e.getMessage());
        }
    }

    /**
     * 解密微信用户信息
     *
     * @param encryptedData 加密数据
     * @param iv            初始向量
     * @param sessionKey    会话密钥
     * @return 用户信息JSON
     */
    public static JSONObject decryptUserInfo(String encryptedData, String iv, String sessionKey) {
        try {
            String decryptedData = decrypt(encryptedData, iv, sessionKey);
            return JSONUtil.parseObj(decryptedData);
        } catch (Exception e) {
            log.error("微信用户信息解密失败", e);
            throw new ServiceException("用户信息解密失败：" + e.getMessage());
        }
    }

    /**
     * AES解密
     *
     * @param encryptedData 加密数据
     * @param iv            初始向量
     * @param sessionKey    会话密钥
     * @return 解密后的数据
     */
    private static String decrypt(String encryptedData, String iv, String sessionKey) throws Exception {
        // Base64解码
        byte[] dataByte = Base64.decode(encryptedData);
        byte[] keyByte = Base64.decode(sessionKey);
        byte[] ivByte = Base64.decode(iv);

        // 创建AES密钥
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "AES");

        // 创建初始化向量
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);

        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 解密
        byte[] decryptedBytes = cipher.doFinal(dataByte);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
