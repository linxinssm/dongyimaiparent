app.controller('indexController',function ($scope,indexService) {

    $scope.showName = function () {
        indexService.showName().success(
            function (respose) {
                alert(respose.loginName)
                $scope.loginName = respose.loginName;
            }
        )


    }
})