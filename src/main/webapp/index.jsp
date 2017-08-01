<%@page language="java" contentType="text/html; utf-8" %>

<html>
<body>
<h2>Hello World!</h2>

Spring MVC上传文件
<form action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="spring mvc上传文件"/>
</form>
富文本Spring MVC上传文件
<form action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="spring mvc上传文件"/>
</form>
</body>
</html>
