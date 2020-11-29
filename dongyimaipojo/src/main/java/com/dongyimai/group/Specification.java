package com.dongyimai.group;

import com.dongyimai.pojo.TbSpecification;
import com.dongyimai.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

//规格
public class Specification implements Serializable {
    //封装TbSpecification和TbSpecificationOption两个对象

    private TbSpecification tbSpecification;

    private List<TbSpecificationOption> tbSpecificationOptionList;


    public Specification() {
    }

    public Specification(TbSpecification tbSpecification, List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecification = tbSpecification;
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getTbSpecificationOptionList() {
        return tbSpecificationOptionList;
    }

    public void setTbSpecificationOptionList(List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "tbSpecification=" + tbSpecification +
                ", tbSpecificationOptionList=" + tbSpecificationOptionList +
                '}';
    }
}
