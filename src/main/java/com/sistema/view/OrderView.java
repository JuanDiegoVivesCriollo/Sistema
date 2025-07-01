package com.sistema.view;

import java.util.List;

public class OrderView {
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public void displayOrderList(List<Object> orders) {
        for (Object o : orders) {
            System.out.println(o.toString());
        }
    }
}
