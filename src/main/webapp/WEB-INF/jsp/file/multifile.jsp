<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/7/13
  Time: 8:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/user/domultiupload" method="post" enctype="multipart/form-data">
    <div>
        <input type="file" name="file1">
    </div>
    <div>
        <input type="file" name="file2">
    </div>
    <div>
        <input type="submit" value="确认上传">
    </div>
</form>
</body>
</html>
