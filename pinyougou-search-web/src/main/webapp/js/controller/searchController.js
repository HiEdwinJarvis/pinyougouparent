app.controller('searchController',function($scope,$location,searchService){

    //搜索
    $scope.search=function(){
        searchService.search( $scope.searchMap ).success(
            function(response){
                $scope.resultMap=response;//搜索返回的结果

                buildPageLabel();//分页
            }
        );
    }

    /**
     * 将各项搜素项放在map集合中，传入后台，
     * 用于搜索
     * */
    $scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sortField':'','sort':''};


    //添加搜索项方法
    $scope.addSearchItem=function(key,value){

        if(key=='category' || key=='brand' || key=='price') {
            $scope.searchMap[key] = value;
        }else{

            $scope.searchMap.spec[key]=value;
        }

        $scope.search();//执行搜索

    }

    //移除复合搜索条件
    $scope.removeSearchItem = function(key){


        if(key=="category" || key=="brand" || key=='price'){
            //如果是分类或者品牌
            $scope.searchMap[key]="";

        }else{

            delete $scope.searchMap.spec[key];//移除此属性
        }
        $scope.search();//执行搜索
    }

    //构建分页标签（totalpages为总页数）
    buildPageLabel=function(){

        $scope.pageLabel=[];//新增分页栏属性

        var maxPageNo = $scope.resultMap.totalPages;//得到最后页码

        var firstPage =1;
        var lastPage =maxPageNo;//截止页码

        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后面有点


        if($scope.resultMap.totalPages>5){
            //如果总页数大于五，
            if($scope.searchMap.pageNo<=3){
                //如果当前页小于等于3
                lastPage=5;
                $scope.firstDot=false;//前面没点
            }else if($scope.searchMap.pageNo>=maxPageNo-2){

                //如果当前页大于等于最大页码-2
                firstPage = maxPageNo-4;//后5页

                $scope.lastDot=false;//后面没点
            }else{

                //显示当前页为中心5页
                firstPage = $scope.searchMap.pageNo-2;
                lastPage = $scope.searchMap.pageNo+2;
            }

        }else{

            $scope.firstDot=false;//
            $scope.lastDot=false;//

        }
        for(var i= firstPage;i<=lastPage;i++){

            $scope.pageLabel.push(i);

        }


    }

    //根据页码查询
    $scope.queryByPage=function(pageNo){
        pageNo = parseInt(pageNo);
        //页码验证
        if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
            return;
        }
        $scope.searchMap.pageNo=pageNo;
        $scope.search();
    }

    //判断该当前页为第一页
    $scope.isTopPage=function(){

        if($scope.searchMap.pageNo==1){

            return true;
        }else{

            return false;
        }
    }
    $scope.isEndPage=function(){

        if($scope.searchMap.pageNo==$scope.resultMap.totalPages){

            return true;
        }else{

            return false;
        }
    }

    //设置排序规则

    $scope.sortSearch=function(sortField,sort){

        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;

        $scope.search();
    }

    //判断关键字是不是品牌
    $scope.keywordsIsBrand=function(){

        for(var i = 0;i<$scope.resultMap.brandList.length;i++){

            if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){

                //如果包含
                return true;
            }

        }
        return false;
    }

    //加载查询字符串
    $scope.loadkeywords=function(){
        $scope.searchMap.keywords=  $location.search()['keywords'];
        $scope.search();
    }








})