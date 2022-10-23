package com.xxd.common.basic.bean;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:带是否被选中标志的实体类
 */
public class SelBean extends BaseBean {

    //是否被选中的标志
    protected boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
