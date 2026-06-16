package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门店信息实体类
 * 
 * @author Backend Architect
 */
@Data
@TableName("store")
@Schema(description = "洛阳当地门店的基本信息实体")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "门店名称")
    private String name;

    @Schema(description = "门店地址")
    private String address;

    @Schema(description = "营业时间")
    private String businessTime;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "门店简介/备注")
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
