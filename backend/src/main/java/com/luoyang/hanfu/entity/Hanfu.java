package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 汉服实体类
 * 
 * @author Backend Architect
 */
@Data
@TableName(value = "hanfu", autoResultMap = true)
@Schema(description = "汉服服饰详细信息实体")
public class Hanfu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "汉服名称")
    private String name;

    @Schema(description = "朝代分类（唐制/宋制/魏晋/明制/洛阳特色）")
    private String dynasty;

    @Schema(description = "主色系")
    private String color;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "图片列表")
    private List<String> imageList;

    @Schema(description = "尺码说明")
    private String size;

    @Schema(description = "租赁价格")
    private BigDecimal rentPrice;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "是否上架：1-上架，0-下架")
    private Integer onShelf;

    @Schema(description = "热度")
    private Integer hotScore;

    @Schema(description = "描述")
    private String description;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除标记")
    private Integer deleted;
}
