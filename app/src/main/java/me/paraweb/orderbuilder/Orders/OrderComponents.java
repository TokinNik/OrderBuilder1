package me.paraweb.orderbuilder.Orders;

import java.util.ArrayList;

/**
 * Created by vault13 on 02.07.17.
 *
 * Компоненты заказа
 */

public class OrderComponents {

    private String componentName;
    private int hourse;

    public OrderComponents(String componentName, int hourse){
        this.componentName = componentName;
        this.hourse = hourse;
    }

    public String getComponentName() {
        return componentName;
    }

    public int getHourse() {
        return hourse;
    }
}
