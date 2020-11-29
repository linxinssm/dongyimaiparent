app.controller('indexController',function ($scope,$filter,indexService) {

    $scope.showName = function () {
        indexService.showName().success(
            function (respose) {
                alert(respose.loginName)
                $scope.loginName = respose.loginName;
            }
        )
    }

    //时间格式处理

     var time = new  Date()
    $scope.dateAsString = $filter('date')(time, "yyyy-MM-dd hh:mm:ss");

        alert($scope.dateAsString )


})