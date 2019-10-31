package com.sxkl.cloudnote.statistic.model;

import com.sxkl.cloudnote.flag.entity.Flag;
import lombok.Data;

/**
 * flag拥有文章最多的
 * @author wangyao
 */
@Data
public class FlagData {

    private Integer index;
    private String name;
    private Integer num;

    public static FlagData configure(int index, Flag flag) {
        FlagData flagData = new FlagData();
        flagData.setIndex(index);
        flagData.setName(flag.getName());
        flagData.setNum(flag.getNum());
        return flagData;
    }
}
