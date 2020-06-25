package com.example.myweather.db;

import org.litepal.crud.DataSupport;
//存放省的数据表
public class Province extends DataSupport {
    private int id;//每个实体类中应有的字段
    private String provinceName;//省份的名字
    private int provinceCode;//省的代号

    public int getId(){
        return id;
    }
    public void  setId(int id){
        this.id=id;
    }

    public String getProvinceName(){
        return provinceName;
    }
    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }

    public int getProvinceCode(){
        return provinceCode;
    }
    public void setProvinceCode(int provinceCode){
        this.provinceCode=provinceCode;
    }

}
