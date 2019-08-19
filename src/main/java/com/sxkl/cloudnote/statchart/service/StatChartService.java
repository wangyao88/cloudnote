package com.sxkl.cloudnote.statchart.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.google.gson.Gson;

@Service
public class StatChartService {


    public String monthArticleColumn(HttpServletRequest request) {

        String fun = "function(params) {"
                + "var colorList = ["
                + "'#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',"
                + "'#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',"
                + "'#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'"
                + "];"
                + "return colorList[params.dataIndex]"
                + "}";

        Option option = new Option();
        option.tooltip(Trigger.axis)
                .legend("数量(篇)");
        CategoryAxis category = new CategoryAxis();
        ValueAxis valueY = new ValueAxis();
        Bar bar = new Bar();
        category.data("java", "python", "go", "python1", "go2", "python3", "go4", "python5", "go6", "python7", "go8");
        bar.data(23, 15, 56, 23, 45, 63, 23, 12, 45, 6, 7);
        Normal normal = new Normal();
        normal.setColor(fun);
        ItemStyle itemStyle = new ItemStyle();
        itemStyle.setNormal(normal);
        bar.itemStyle(itemStyle);
        option.xAxis(category);
        option.yAxis(valueY);
        option.series(bar);
        return new Gson().toJson(option);
    }

}
