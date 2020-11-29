//控制层
app.controller('typeTemplateController' ,function($scope,$controller,specificationService,brandService,typeTemplateService){

    $controller('baseController',{$scope:$scope});//继承

    //当夜面加载时数据回显,格式转换的方法
    $scope.jsonToString = function(jsonString,key){
        //1、jsonString 是 json格式的字符串 key 是 键[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        // 1.1、json格式转换
        var json = JSON.parse(jsonString);
        //1.2、最终要返回的字符串
        var result = '';
        //2、循环
        for(var i=0;i<json.length;i++){
            //2.1 如果不是最后一个值 则在后面的数据拼接 , 仅是让第一个值前面无，
            if(i>0){
                result += ",";
            }
            result += json[i][key];
        }
        return result;
    }



    //规格列表
    $scope.specList={
        data:[]
    };

    $scope.selectSpecificationList = function(){
        specificationService.selectSpecificationList().success(
            function (response) {
                $scope.specList = {data:response};
            }
        );
    }

    //扩展属性
    $scope.entity={customAttributeItems:[]}

    $scope.addTableRow = function(){

        $scope.entity.customAttributeItems.push({});
    }

    $scope.deleTableRow = function(idx){

        alert(idx)
        alert($scope.entity.customAttributeItems)
        $scope.entity.customAttributeItems.splice(idx,1);
    }



    //品牌选择的数组
    $scope.brandList={
        data:[]
    };
    $scope.selectBrandList = function(){
        brandService.selectOptionList().success(
            function (response) {
                $scope.brandList = {data:response};
            }
        );
    }


/*======================================================*/
    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        typeTemplateService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    //分页
    $scope.findPage=function(page,rows){
        typeTemplateService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne=function(id){
        typeTemplateService.findOne(id).success(
            function(response){
                $scope.entity= response;
                /*在数据库中这些字段是字符串类型的JSON格式需要转化成JSON格式给浏览器*/
                $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
                $scope.entity.specIds = JSON.parse($scope.entity.specIds);
                $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);


            }
        );
    }

    //保存
    $scope.save=function(){
        var serviceObject;//服务层对象
        if($scope.entity.id!=null){//如果有ID
            serviceObject=typeTemplateService.update( $scope.entity ); //修改
        }else{
            serviceObject=typeTemplateService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    //重新查询
                    $scope.reloadList();//重新加载
                }else{
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        typeTemplateService.dele( $scope.selectIds ).success(
            function(response){
                if(response.success){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds=[];
                }
            }
        );
    }

    $scope.searchEntity={};//定义搜索对象

    //搜索
    $scope.search=function(page,rows){
        typeTemplateService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

});	