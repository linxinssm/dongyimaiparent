/*继承*/
app.controller('baseController',function ($scope) {

    //重新加载列表 数据
    $scope.reloadList=function(){
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    /*  paginationConf 变量各属性的意义：
        currentPage：当前页码
        totalItems:总条数
        itemsPerPage:
        perPageOptions：页码选项
        onChange：更改页面时触发事件
    */
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){/*页面一加载就在此处调用seach方法*/
            $scope.reloadList();//重新加载
        }
    };

    $scope.selectIds=[];//选中的ID集合
    //更新复选,$event事件源
    $scope.updateSelection = function($event, id) {
        if($event.target.checked){//如果是被选中,则增加到数组
            $scope.selectIds.push(id);
        }else{
            /*如果id=25::$scope.selectIds.indexOf(id)就是返回id=25在数组中的索引*/
            /*splice(idx, 1)意思是:如果idx=2,就从索引idx往后删除1位包含idx*/
            var idx = $scope.selectIds.indexOf(id);
            alert(idx)
            $scope.selectIds.splice(idx, 1);//删除
        }
    }

})