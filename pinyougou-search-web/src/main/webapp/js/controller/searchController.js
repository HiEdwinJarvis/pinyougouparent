app.controller('searchController',function($scope,searchService){

    //搜索
    $scope.search=function(){
        searchService.search( $scope.searchMap ).success(
            function(response){
                $scope.resultMap=response;//搜索返回的结果
            }
        );
    }

    /**
     * 将各项搜素项放在map集合中，传入后台，
     * 用于搜索
     * */
    $scope.searchMap={'keywords':'','category':'','brand':'','spec':{}};

    //添加搜索项方法
    $scope.addSearchItem=function(key,value){

        if(key=='category' || key=='brand') {
            $scope.searchMap[key] = value;
        }else{

            $scope.searchMap.spec[key]=value;
        }

        $scope.search();//执行搜索

    }

    //移除复合搜索条件
    $scope.removeSearchItem = function(key){


        if(key=="category" || key=="brand"){
            //如果是分类或者品牌
            $scope.searchMap[key]="";

        }else{

            delete $scope.searchMap.spec[key];//移除此属性
        }
        $scope.search();//执行搜索
    }




})