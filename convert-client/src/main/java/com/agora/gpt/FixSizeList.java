package com.agora.gpt;

import java.util.ArrayList;

/**
 * Created by 许成谱 on 4/13/23 3:44 下午.
 * qq:1550540124
 * 热爱生活每一天！
 * 定长集合，超出移除第一个
 */
class FixSizeList<T> extends ArrayList<T> {

    private  int capacity;

    public FixSizeList(int capacity) {
        super();
        if (capacity <= 0) {
            capacity = 1;
        }
        this.capacity = capacity;
    }

    public boolean add(T t) {
        // 超过长度，移除第一个
        if (size() >= capacity && size() != 0) {
            super.remove(0);
        }
        return super.add(t);
    }

    public void setCapacity(int capacity){
        if (capacity <= 0) {
            capacity = 1;
        }
        this.capacity = capacity;
    }
}
