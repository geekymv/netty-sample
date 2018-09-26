package com.geekymv.netty.sample.protobuf;

import java.io.ByteArrayOutputStream;

public class OrderInfoTest {

    public static void main(String[] args) throws Exception {

        OrderInfo.Order order = OrderInfo.Order.newBuilder()
                .setQuery("query1")
                .setPageNumber(1)
                .setResultPerPage(2)
                .build();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        order.writeTo(bos);
        byte[] bytes = bos.toByteArray();
        System.out.println(bytes.length);

        OrderInfo.Order order2 = OrderInfo.Order.parseFrom(bytes);
        System.out.println(order);
        System.out.println(order2);
    }

}
