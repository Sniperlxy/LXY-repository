$(function () {
    // 抽取变量：这个js文件中出现多少次${"#....}，JS引擎就会去对应的页面做多少次元素查找。现在这样集中整合后，每种选择器只做一次，后面都是直接用变量
    var empDatagrid, empDatagridEditAndDel, empDialog, empForm;
    empDatagrid = $("#emp_datagrid");
    empDatagridEditAndDel = $("#emp_datagrid_del,#emp_datagrid_edit");
    empDialog = $("#emp_dialog");
    empForm = $("#emp_form");

    empDatagrid.datagrid({
        //请求地址
        url: "/employee_list",
        //适应
        fit: true,
        //适应列:展开配合width：1(比例为1)
        fitColumns: true,
        //行号
        rownumbers: true,
        //加上分页
        pagination: true,
        //顶部按钮
        toolbar: '#emp_datagrid_tb',
        //单选(选中要编辑的行)
        singleSelect:true,
        //分页显示几行
        pageList:[1,5,10,20],
        //将离职的员工编辑和离职按钮禁用
        onClickRow:function(rowIndex,rowData){
            //判断是否是离职员工
            if(rowData.state){
                //如果不是就开启
                empDatagridEditAndDel.linkbutton("enable")
            }else {
                //如果是则让离职和编辑按钮禁用
                empDatagridEditAndDel.linkbutton("disable")
            }
        },
        columns: [
            [
                {field: "username", title: "用户名", width: 1, align: 'center'},
                {field: "realname", title: "真实姓名", width: 1, align: 'center'},
                {field: "tel", title: "电话", width: 1, align: 'center'},
                {field: "email", title: "邮箱", width: 1, align: 'center'},
                {field: "dept", title: "部门", width: 1, align: 'center',formatter:deptFormatter},
                {field: "inputtime", title: "入职时间", width: 1, align: 'center'},
                {field: "state", title: "状态", width: 1, align: 'center',formatter: stateFormatter},
                {field: "admin", title: "是否超级管理员", width: 1, align: 'center',formatter:adminFormatter},
                {field: "online",title: "是否在线",width: 1,align: 'center',formatter:onlineFormatter}
            ]
        ]
    });
    //主页一加载就显示新增的对话框,closed:true默认隐藏
    empDialog.dialog({
        width:300,
        height:300,
        buttons:'#emp_dialog_bt',
        closed:true
    })

    //对按钮方法进行统一管理
    var cmdObj={
            //打开新增对话框并进行细微处理
            add:function () {
                empDialog.dialog("open")
                empDialog.dialog("setTitle","新增")
                empForm.form("clear")
            },

            //save方法才是真正保存员工的方法
            save:function (){
                //获取到提交表单中隐藏提交的字段id
                var idVal=$("#emp_form [name='id']").val()
                var url;
                if (idVal){
                    //如果id有值说明是编辑
                    url="/employee_update"
                }else{
                    //如果没值说明是新增
                    url="/employee_save"
                }
                empForm.form("submit",{
                    url:url,
                    //点击保存提交额外参数role.id 格式例如[role[0].id=?,role[1].id=?]
                    onSubmit:function(param){
                       //首先获得角色信息里所有的id
                       var ids=$("#emp_role").combobox("getValues");
                        for (var i=0;i<ids.length;i++){
                            param["roles["+i+"].id"]=ids[i]
                        }

                    },
                    success:function (data) {
                        var data=$.parseJSON(data)
                        if (data.success){
                            //给出成功信息
                            $.messager.alert("温馨提示",data.msg,"info",function () {
                                //关闭对话框
                                empDialog.dialog("close")
                                //刷新数据表格
                                empDatagrid.datagrid("reload")
                            })
                        }else {
                            //给出失败信息
                            $.messager.alert("温馨提示",data.msg,"info")
                        }
                    }

                })
            },

            //编辑员工的方法,只是回显数据,真正执行编辑操作其实是上面的save方法
            edit:function(){
                //得到被singleSelect(单选选中的行)->其实是一个employee
                var rowData=empDatagrid.datagrid("getSelected")
                //rowdata有值就为true
                if(rowData){
                    //打开编辑的表单对话框
                    empDialog.dialog("open")
                    //设置对话框的title
                    empDialog.dialog("setTitle","编辑")
                    //新打开的表单对话框要先清空
                    empForm.form("clear")
                    //如果说被选中的行的dept属性存在,将dept_id转换为dept.id
                if (rowData.dept) {
                    // 处理特殊属性：给rowData对象添加一个属性（Java是不能动态添加属性的)
                    rowData["dept.id"] = rowData.dept.id
                }
                //处理角色信息的回显 发送同步ajax请求 得到查询所有角色 得到一个List ids集合
                    var html=$.ajax({
                        url:"/role_queryByEid?eid="+rowData.id,
                        // 必须是同步请求，否则后台数据还没返回，前台就忙着设置，会是空！
                        async:false
                    }).responseText
                    //再将html转换为Json对象
                    html=$.parseJSON(html)
                    //将这个ids集合放进combox中进行回显该员工所对应的所有角色
                    $("#emp_role").combobox("setValues",html)
                //把被选中的行的数据显示在编辑表单中
                    empForm.form("load",rowData)
                }else
                    //如果rowdata没有值说明没有编辑前没有选中一行给出提示框
                    $.messager.alert("温馨提示","请选中一条需要编辑的数据","info")
            },

            //假删除员工(设置员工状态为false,离职)
           delete:function(){
             //得到当前所选 行的rowdata对象(实则为Employee对象)
               var rowData=empDatagrid.datagrid("getSelected")
               //如果rowData有数据
               if (rowData) {
                   //打开离职确认框
                   $.messager.confirm("温馨提示","确认离职该员工?",function (yes) {
                             if (yes){
                                 //如果点击确认执行离职操作
                                 $.get("/employee_delete?id="+rowData.id,function (data) {
                                           if (data.success){
                                               //离职成功返回成功信息
                                               $.messager.alert("温馨提示",data.msg,"icon")
                                               //刷新数据表格
                                               empDatagrid.datagrid("reload")
                                           }else {
                                               //离职失败返回失败信息
                                               $.messager.alert("温馨提示",data.msg,"icon")
                                           }
                                 },"json")
                             }
                   })
               }else {
                   //获取删除失败的提示框信息
                   $.messager.alert("温馨提示","请选择要离职的员工","info")
               }
           },

          //关键字搜索
        find: function () {
            var keyword = $("[name='keyword']").val();
            empDatagrid.datagrid("load", {
                keyword: keyword
            });
        },

            //刷新数据表格的方法
            reload:function () {
                empDatagrid.datagrid("reload")
            },
            //取消新增的方法
            cancel: function () {
                empDialog.dialog("close");
            },

            //将在职员工导出为excel
            download:function () {
               window.location.href='/exportEmployeeForExcel';
            }

    }

//对按钮进行统一的监听
$("a[data-cmd]").on("click",function () {
    var cmd= $(this).data("cmd")
    if (cmd){
        cmdObj[cmd]()
    }
});

    //允许用户通过enter键查询
    $(document).keyup(function (event) {
        if (event.keyCode == 13) {
            //如果按的是enter键，调用find()
            cmdObj.find();
        }
    });


})






//格式化显示部门名称
function deptFormatter(value,record,index){
    return value?value.name:"";
}
//格式化显示状态
function stateFormatter(value,record,index) {
    return value?"<span style='color: green'>在职</span>":"<span style='color: red'>离职</span>"
}
//格式化显示是否为超级管理员
function adminFormatter(value,record,index) {
    return value?"是":"否";
}

//格式化显示是否在线
function onlineFormatter(value,record,index) {
return value?"<span style='color: green'>在线</span>":"<span style='color: red'>离线</span>"
}