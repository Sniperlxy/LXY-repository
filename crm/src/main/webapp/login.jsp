<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>客户关系管理系统</title>
    <link rel="stylesheet" href="css/style.css">
    <script type="text/javascript" src="/js/jquery-easyui/jquery.min.js"></script>
    <script type="text/javascript">
        //允许用户通过enter键提交表单
        $(document).keyup(function (event) {
            if (event.keyCode === 13) {
                //如果按的是enter键，提交表单
                submitForm();
            }
        });

        //ajax异步登录
        function submitForm() {
            $.post("/login", $("form").serialize(), function (data) {
                if (data.success) {
                    //跳转到首页
                    window.location.href = "/index";
                } else {
                    alert(data.msg);
                }
            }, "json")
        }

        //重置表单
        function resetForm() {
            $("form input[name]").val("");
        }

    </script>
</head>
<body>
<section class="container">
    <div class="login">
        <h1>用户登录</h1>
        <form method="post">
            <p><input type="text" name="username" value="" placeholder="账号"></p>
            <p><input type="password" name="password" value="" placeholder="密码"></p>
            <p class="submit">
                <input type="button" value="登录" onclick="submitForm()">
                <input type="button" value="重置" onclick="resetForm()">
            </p>
        </form>
    </div>
</section>
<div style="text-align:center;" class="login-help">
    <p>Copyright ©2020 xxxx科技有限公司</p>
</div>
</body>
</html>