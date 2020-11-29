app.service('cartService',function ($http) {
    //查询购物车
    this.findCartList = function () {
        return $http.get('http://192.168.188.146:8161/cart/findCartList.do');
    }
    
})