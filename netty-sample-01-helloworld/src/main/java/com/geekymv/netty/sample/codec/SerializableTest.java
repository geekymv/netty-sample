package com.geekymv.netty.sample.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableTest {

    /**
     * 序列化
     * @param o
     * @return
     */
    public static byte[] serializer(Object o) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);

            os.writeObject(o);

            byte[] bytes = bos.toByteArray();
            System.out.println(bytes.length);

            return bytes;

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }


    /**
     * 反序列化
     * @param bytes
     * @return
     */
    public static Object deserializer(byte[] bytes) {
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;

        try {
            bis = new ByteArrayInputStream(bytes);
            is = new ObjectInputStream(bis);

            Object o = is.readObject();
            System.out.println(o);

            return o;

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1000L);
        userInfo.setUserName("zhangsan");

        byte[] buf = serializer(userInfo);
        Object o = deserializer(buf);
    }

}
