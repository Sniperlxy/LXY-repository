$(function () {
    /* 抽取变量：这个js文件中出现多少次${"#....}，JS引擎就会去对应的页面做多少次元素查找。
     现在这样集中整合后，每种选择器只做一次，后面都是直接用变量*/
    var roleDatagrid, roleDialog, roleForm, allPermissions, selfPermissions;
    roleDatagrid = $("#role_datagrid");
    roleDialog = $("#role_dialog");
    roleForm = $("#role_form");
    allPermissions = $("#allPermissions");
    selfPermissions = $("#selfPermissions");


    /*-----------------------角色管理页面数据表格---------------------------------*/
    roleDatagrid.datagrid({
        //请求地址
        url: "/role_list",
        //适应
        fit: true,
        //适应列:展开配合width：1(比例为1)
        fitColumns: true,
        //行号
        rownumbers: true,
        //加上分页
        pagination: true,
        //顶部按钮
        toolbar: '#role_datagrid_tb',
        //单选(选中要编辑的行)
        singleSelect:true,
        //分页显示几行
        pageList:[1,5,10,20],
        columns: [
            [
                //显示角色编号和角色名称两个字段名
                {field: "sn", title: "角色编号", width: 1, align: 'center'},
                {field: "name", title: "角色名称", width: 1, align: 'center'}
            ]
        ]
    });

    /*----------------------------大弹窗对话框-------------------------------*/
    //主页一加载就显示新增的对话框,closed:true默认隐藏
    roleDialog.dialog({
        width:700,
        height:450,
        buttons:'#role_dialog_bt',
        closed:true
    })

    /*-----------------------------左边所有权限的数据表格------------------------*/
    allPermissions.datagrid({
        width:300,
        height:300,
        title:"所有权限",
        url:'/permission_list',
        rownumbers: true,
        singleSelect: true,
        pagination:true,
        fitColumns: true,
        pageList: [1,5,10,20],
        onDblClickRow:function(rowIndex,rowData){
            //得到已有权限数据表格中的数据
            var selfRows=selfPermissions.datagrid("getRows")
            var flag=true
            var index=-1
            //遍历得到行的数据
            for (var i=0;i<selfRows.length;i++) {
                //判断两边权限 id是否一致
                if (selfRows[i].id === rowData.id) {
                    //如果一致，说明已经拥有该权限，设置flag为false
                    flag = false;
                    //记录当前索引
                    index = i;
                    //退出该循环
                    break;
                }
            }
            //如果为真
            if (flag){
                //说明是新增
                selfPermissions.datagrid("appendRow",rowData)
            }else {
                //如果为假说明不是,右侧已有该数据,则不能添加
                selfPermissions.datagrid("selectRow",index)
            }
        },
        columns:[
            [
                //显示的标题字段名
                {field:"name",title:"权限名",width:1,align:'center'}
            ]
        ]
    })


    /*-----------------------------右边已有权限的数据表格-------------------------*/
    selfPermissions.datagrid({
        width:300,
        height:300,
        title:"已有权限",
        rownumbers: true,
        singleSelect: true,
        pagination:true,
        fitColumns: true,
        pageList: [1,5,10,20],
        onDblClickRow:function(rowIndex,rowData){
            selfPermissions.datagrid("deleteRow",rowIndex)
        },
        columns:[
            [
                //显示标题字段名
                {field:"name",title:"权限名",width:1,align:'center'}
            ]
        ]

    })



    /*-------------------------统一管理按钮--------------------------------*/
    var cmdObj={
        /*-----------------------打开新增弹窗-----------------------*/
        add:function () {
            //打开弹窗
            roleDialog.dialog("open")
            //设置标题为新增
            roleDialog.dialog("setTitle","新增")
            //清空三个参数字段
            $("[name='id'],[name='sn'],[name='name']").val("")
            // 右侧载入空数据，而左侧会请求后端，查询所有的permission_list
            var rows=selfPermissions.datagrid("getRows")
            for (var i=0;i<rows.length;i++){
                selfPermissions.datagrid("deleteRow",i)
                i--
            }

        },

        /*-----------save方法才是真正保存role角色信息的方法-----------------------*/
        save:function (){
            //获取到提交表单中隐藏提交的字段id
            var idVal=$("#role_form [name='id']").val()
            var url;
            if (idVal){
                //如果id有值说明是编辑
                url="/role_update"
            }else{
                //如果没值说明是新增
                url="/role_save"
            }
            roleForm.form("submit",{
                url:url,
                // 由于roleForm表单提交只会拿到角色的sn和name，所以在提交前要设法把选择的权限也捎上
                onSubmit: function (param) {
                    // 从selfPermissions获取已选权限，额外添加一些参数到param，一同提交后台
                    var rows = selfPermissions.datagrid("getRows");
                    for (var i = 0; i < rows.length; i++) {
                        // 把右侧选中的权限id都设置进param中的permission数组：permission[0].id = xx, permission[1].id = xx, ...
                        param["permissions["+i+"].id"] = rows[i].id;
                    }
                },
                success:function (data) {
                    var data=$.parseJSON(data)
                    if (data.success){
                        //给出成功信息
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            roleDialog.dialog("close")
                            //刷新数据表格
                            roleDatagrid.datagrid("reload")
                        })
                    }else {
                        //给出失败信息
                        $.messager.alert("温馨提示",data.msg,"info")
                    }
                }

            })
        },

        /*------编辑角色的方法,只是回显数据,真正执行编辑操作其实是上面的save方法----*/
        edit:function(){
            //得到被singleSelect(单选选中的行)->其实是一个role
            var rowData=roleDatagrid.datagrid("getSelected")
            //rowdata有值就为true
            if(rowData){
                //打开编辑的表单对话框
                roleDialog.dialog("open")
                //设置对话框的title
                roleDialog.dialog("setTitle","编辑")
                // 让右侧框也向后台发起请求，根据角色id查询对应的permission。而左侧本来就能向后台请求权限列表，不必额外设置
                var options = selfPermissions.datagrid("options");
                options.url = "/permission_queryByRid";
                selfPermissions.datagrid("load", {
                    rid:rowData.id,
                });
                //把被选中的行的数据显示在编辑表单中(sn,name)
                roleForm.form("load",rowData)
            }else
                //如果rowdata没有值说明没有编辑前没有选中一行给出提示框
                $.messager.alert("温馨提示","请选中一条需要编辑的数据","info")
        },


        /*--------------------删除角色---------------------------------------*/
        del:function(){
            //得到当前所选行的rowdata对象(实则为role对象)
            var rowData=roleDatagrid.datagrid("getSelected")
            //如果rowData有数据
            if (rowData) {
                //打开确认框
                $.messager.confirm("温馨提示","确认删除该数据?",function (yes) {
                    if (yes){
                        //如果点击确认执行删除操作
                        $.get("/role_delete?id="+rowData.id,function (data) {
                            if (data.success){
                                //删除成功返回成功信息
                                $.messager.alert("温馨提示",data.msg,"icon")
                                //刷新数据表格
                                roleDatagrid.datagrid("reload")
                            }else {
                                //删除失败返回失败信息
                                $.messager.alert("温馨提示",data.msg,"icon")
                            }
                        },"json")
                    }
                })
            }else {
                //获取删除失败的提示框信息
                $.messager.alert("温馨提示","请选择要删除的角色","info")
            }
        },

        /*----------------------------------关键字+分页查询------------------*/
        find: function () {
            var keyword = $("[name='keyword']").val();
            roleDatagrid.datagrid("load", {
                keyword: keyword
            });
        },

        /*------------------------------刷新数据表格----------------*/
        reload:function () {
            roleDatagrid.datagrid("reload")
        },

        /*-----------------------------取消新增/编辑----------------*/
        cancel: function () {
            roleDialog.dialog("close");
        }

    }
//------------------------------------对按钮进行统一的监听-----------------------------------------
    $("a[data-cmd]").on("click",function () {
        var cmd= $(this).data("cmd")
        if (cmd){
            cmdObj[cmd]()
        }
    })
//允许用户通过enter键查询
    $(document).keyup(function (event) {
        if (event.keyCode == 13) {
            //如果按的是enter键，调用find()
            cmdObj.find();
        }
    });
})



