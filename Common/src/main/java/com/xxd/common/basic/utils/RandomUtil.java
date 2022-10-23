package com.xxd.common.basic.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:随机工具类
 */
public class RandomUtil {


    /**
     * 将传入的List的元素进行随机排序后再返回新的列表
     *
     * @param list 原列表
     * @param <E>
     * @return 原列表中元素随机排序后的列表
     */
    public static <E> List<E> randomSortList(final List<E> list) {
        if (list == null || list.size() == 0) {
            return list;
        }
        List<E> tmpList = new ArrayList<>(list);
        List<E> retList = new ArrayList<>();
        Random random = new Random();
        int index = 0;
        while (tmpList.size() > 1) {
            index = random.nextInt(tmpList.size());
            retList.add(tmpList.get(index));
            tmpList.remove(index);
        }
        retList.add(tmpList.get(0));
        return retList;
    }

    /**
     * 返回一个元素列表，该元素列表包含includedItem，同时从list中随机取出getCount个元素放入该返回的元素列表中
     *
     * @param list
     * @param includedItem 需包含在返回的列表中的元素
     * @param getCount     要获取的除includedItem元素外的其他元素的数量
     * @param <E>
     * @return
     */
    public static <E> List<E> randomGetItems(final List<E> list, E includedItem, int getCount) {
        return randomGetItems(list, getCount, includedItem);
    }

    /**
     * 返回一个元素列表，该元素列表包含includedItems，同时从list中随机取出getCount个元素放入该返回的元素列表中
     *
     * @param list
     * @param getCount      要获取的除includedItems元素外的其他元素的数量
     * @param includedItems 需包含在返回的列表中的元素
     * @param <E>
     * @return
     */
    public static <E> List<E> randomGetItems(final List<E> list, int getCount, E... includedItems) {
        if (list == null || list.size() == 0) {
            return list;
        }

        List<E> retList = new ArrayList<>();
        List<E> tmpList = new ArrayList<>(list);
        //从tmpList删除要排除的元素，同时将该元素加到retList中
        if (includedItems != null) {
            for (E includedItem : includedItems) {
                tmpList.remove(includedItem);
                retList.add(includedItem);
            }
        }

        Random random = new Random();
        int index = 0;
        while (getCount > 0 && tmpList.size() > 0) {
            index = random.nextInt(tmpList.size());
            retList.add(tmpList.get(index));
            tmpList.remove(index);
            getCount--;
        }
        return randomSortList(retList);
    }

    /**
     * 从列表list中随机删除一个元素并返回该元素
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> E randomRemoveOne(List<E> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(list.size());
        E item = list.get(index);
        list.remove(index);
        return item;
    }
}
