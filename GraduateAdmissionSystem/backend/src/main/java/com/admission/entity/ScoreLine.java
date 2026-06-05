package com.admission.entity;

/**
 * 分数线实体类
 */
public class ScoreLine {
    private Integer id;
    private String year;
    private Double politicsLine;
    private Double foreignLangLine;
    private Double majorBasisLine;
    private Double totalFirstLine;
    private Double admissionTotalLine;

    public ScoreLine() {}

    public ScoreLine(Integer id, String year, Double politicsLine, Double foreignLangLine,
                     Double majorBasisLine, Double totalFirstLine, Double admissionTotalLine) {
        this.id = id;
        this.year = year;
        this.politicsLine = politicsLine;
        this.foreignLangLine = foreignLangLine;
        this.majorBasisLine = majorBasisLine;
        this.totalFirstLine = totalFirstLine;
        this.admissionTotalLine = admissionTotalLine;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public Double getPoliticsLine() { return politicsLine; }
    public void setPoliticsLine(Double politicsLine) { this.politicsLine = politicsLine; }
    public Double getForeignLangLine() { return foreignLangLine; }
    public void setForeignLangLine(Double foreignLangLine) { this.foreignLangLine = foreignLangLine; }
    public Double getMajorBasisLine() { return majorBasisLine; }
    public void setMajorBasisLine(Double majorBasisLine) { this.majorBasisLine = majorBasisLine; }
    public Double getTotalFirstLine() { return totalFirstLine; }
    public void setTotalFirstLine(Double totalFirstLine) { this.totalFirstLine = totalFirstLine; }
    public Double getAdmissionTotalLine() { return admissionTotalLine; }
    public void setAdmissionTotalLine(Double admissionTotalLine) { this.admissionTotalLine = admissionTotalLine; }
}
