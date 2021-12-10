<!-- 获取网站根路径 -->
<#assign basePath=request.contextPath>
<html>
<!-- js目录在static目录下，request.contextPath就是从static目录开始-->
<script src="${basePath}/base/jquery-easyui-1.7.0/jquery.min.js" type="text/javascript"></script>
<script src="${basePath}/js/login.js" type="text/javascript"></script>
<body>
<p> ${basePath}</p>
<p> ${request.getContextPath()}</p>
<p> ${password}</p>
<p> ${userName}</p>
<#--语法展示，多层级-->
<#list users as user>
<div>
    <P>${user.name}</P>
    <P>${user.address}</P>
    <#list user.books as book>
    <ul>
        <li>${book.name}</li>
        <li>${book.price}</li>
    </ul>
    </#list>

</div>
</#list>

<br>
<hr>

<!-- 反例 -->
<#--<#users user>-->
<#--<P> ${user.name}</P>-->
<#--</#users>-->

</body>
</html>