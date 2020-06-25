package com.example.myweather.db;

import org.litepal.crud.DataSupport;
//存放省数据表
class Province extends DataSupport {
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
    public void setProvinceName(){
        this.provinceName=provinceName;
    }

    public int getProvinceCode(){
        return provinceCode;
    }
    public void setProvinceCode(){
        this.provinceCode=provinceCode;
    }

}
