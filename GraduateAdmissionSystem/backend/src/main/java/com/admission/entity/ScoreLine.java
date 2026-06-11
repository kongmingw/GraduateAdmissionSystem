package com.admission.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分数线实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreLine {
    private Integer id;
    private String year;
    private Double politicsLine;
    private Double foreignLangLine;
    private Double majorBasisLine;
    private Double totalFirstLine;
    private Double admissionTotalLine;
}
