/*前端控制层*/
app.controller('brandController' ,function($controller,$scope,brandService){

   // 让brand Controller继承baseController
    /*$scope(base的作用对象) :(继承:作用代码从用) $scope(brandController的作用对象)*/
    $controller('baseController',{$scope:$scope});

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        brandService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    /*
     //重新加载列表 数据
    $scope.reloadList=function(){
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    //重新加载列表 数据
    $scope.reloadList=function(){
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    /!*paginationConf 变量各属性的意义：
        currentPage：当前页码
        totalItems:总条数
        itemsPerPage:
        perPageOptions：页码选项
        onChange：更改页面时触发事件
    *!/
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };*/


    //分页
    $scope.findPage=function(page,rows){
        brandService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }
    //保存
    $scope.save=function(){
        if($scope.entity.id!=null){//如果有ID
            brandService.update($scope.entity).success(
                function(response){
                    if(response.success){
                        //重新查询
                        $scope.reloadList();//重新加载
                    }else{
                        alert(response.message);
                    }
                }
            );
        }else{
            brandService.add($scope.entity).success(
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
    }

    //查询实体
    $scope.findOne=function(id){
        brandService.findOne(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }

  /*  $scope.selectIds=[];//选中的ID集合
//更新复选
    $scope.updateSelection = function($event, id) {
        if($event.target.checked){//如果是被选中,则增加到数组
            $scope.selectIds.push(id);
        }else{
            /!*如果id=25::$scope.selectIds.indexOf(id)就是返回id=25在数组中的索引*!/
            /!*splice(idx, 1)意思是:如果idx=1,就是 干掉从索引为1开始的1位*!/
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除
        }
    }*/

//批量删除
    $scope.dele=function(){
        //获取选中的复选框
        brandService.dele($scope.selectIds).success(
            function(response){
                if(response.success){
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity={};//定义搜索对象
//条件查询
    $scope.search=function(page,rows){
        brandService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.paginationConf.totalItems=response.total;//总记录数
                $scope.list=response.rows;//给列表变量赋值
            }
        );
    }

});