<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path=request.getContextPath();
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet">
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet">
	<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet">

	<script src="jquery/jquery-1.11.1-min.js" type="text/javascript"></script>
	<script src="jquery/bootstrap_3.3.0/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
	<script src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
	<script src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<meta charset="UTF-8">
<script type="text/javascript">

	$(function(){
		$("#importActivityBtn").click(function () {
			var fileName=$("#activityFile").val();
			alert(fileName);
			var suffix=fileName.substr(fileName.lastIndexOf(".")+1).toUpperCase();
			if(suffix!='XLS'){
				alert("文件后缀名必须为xls");
				return;
			}
			var activityFile=$("#activityFile")[0].files[0];
			if(activityFile.size>5*1024*1024){
				alert("文件大小不能大于5M");
				return;
			}
			var formData=new FormData();
			formData.append("activityFile",activityFile);
			$.ajax({
				url:"workbench/activity/importActivity.do",
				data:formData,
				processData:false,//向后台提交参数前，是否将参数统一转换为字符串
				contentType:false,////设置ajax向后台提交参数之前，是否把所有的参数统一按urlencoded编码：true--是,false--不是，默认是true
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code==1){
						alert("成功上传"+data.retData+"条");
						$("#importActivityModal").modal("hide");
						queryAllActivity(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
					}else{
						alert(data.message);
					}
				}
			})
		})
		$("#exportActivityXzBtn").click(function () {
			var checkedList=$("#tActivityList input[type='checkbox']:checked");
			if(!checkedList.size()>0){
				alert("每次至少选择导出一条记录");
				return;
			}
			var c="";
			$.each(checkedList,function (i,obj) {
				c+="id="+obj.value+"&";
			})
			c=c.substring(0,c.length-1);
			window.location.href="workbench/activity/exportActivity.do?"+c;
		})
        $("#exportActivityAllBtn").click(function () {
            window.location.href="workbench/activity/exportAllActivity.do";
        })

		$("#deleteActivityBtn").click(function () {
			if(window.confirm("你确定要删除吗")){
				var checklist=$("#tActivityList input[type='checkbox']:checked");
				if(!checklist.size()>0){
					alert("请至少删除一个活动");
					return;
				}
				var parm="";
				$.each(checklist,function (i,obj) {
					parm+="id="+obj.value+"&";
				})
				parm=parm.substring(0,parm.length-1);
				$.ajax({
					url:"workbench/activity/removeActivityList.do",
					data:parm,
					dataType:"json",
					type:"post",
					success:function (data) {
						if(data.code==1){
							queryAllActivity(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
						}else{
							alert(data.message);
						}
					}
				})
			}
		})

		$("#editActivityDivBtn").click(function () {
			$("#editActivityForm").get(0).reset();
			$("#editActivityModal").modal("show");
		})
		$("#updateActivityBtn").click(function () {
			var checkedList=$("#tActivityList input[type='checkbox']:checked");
			if(!checkedList.size()>0){
				alert("修改活动数目不能为0");
				return;
			}
			if(checkedList.size()>1){
				alert("修改活动数目只能为1个");
				return;
			}
			var id=checkedList[0].value;
			var owner=$("#edit-Owner").val();
			var name=$.trim($("#edit-Name").val());
			var startDate=$("#edit-startDate").val();
			var endDate=$("#edit-endDate").val();
			var cost=$.trim($("#edit-cost").val());
			var description=$.trim($("#edit-description").val());
			if(owner==null||owner==''){
				alert("所有者不能为空");
				return;
			}
			if(name==null||name==''){
				alert("活动名不能为空");
				return;
			}
			if(startDate!=null&&endDate!=null){
				if(startDate>endDate){
					alert("起始日期不能大于开始日期");
					return;
				}
			}
			var regExp=/^(([0-9]\d*)|0)$/
			if(!regExp.test(cost)){
				alert("金额需为非负整数")
				return;;
			}
			$.ajax({
				url:"workbench/activity/editActivity.do ",
				data:{
					id:id,
					name:name,
					owner:owner,
					startDate:startDate,
					endDate:endDate,
					description:description,
					cost:cost
				},
				dataType:"json",
				type:"post",
				success:function (data) {
					if(data.code==1){
						$("#editActivityModal").modal("hide");
						queryAllActivity(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
					}else{
						alert(data.message);
					}
				}
			})
		})
		$("#queryActivityBtn").click(function () {
			queryAllActivity(1,10);
		})
		$("#createActivityBtn").click(function () {
			$("#createActivityForm").get(0).reset();
			$("#createActivityModal").modal("show");
		})
		$("#saveCreateActivityBtn").click(function () {
			var owner=$("#create-Owner").val();
			var name=$.trim($("#create-ActivityName").val());
			var startDate=$("#create-startTime").val();
			var endDate=$("#create-endTime").val();
			var cost=$.trim($("#create-cost").val());
			var description=$.trim($("#create-description").val());
			if(owner==''||name==''){
				alert("名字与所有者不能为空");
				return;
			}
			if(startDate!=null&&endDate!=null){
				if(startDate>endDate){
					alert("结束日期不能大于开始日期");
					return;
				}
			}
			var regExp=/^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("成本只能为非负整数");
				return;
			}
			$.ajax({
				url:"workbench/activity/saveActivity.do",
				data:{
					name:name,
					owner:owner,
					cost:cost,
					description:description,
					startDate:startDate,
					endDate:endDate
				},
				dataType:"json",
				type:"post",
				success:function (data) {
					if(data.code==1){
						$("#createActivityModal").modal("hide");
						queryAllActivity(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
					}else{
						alert(data.message);
					}
				}
			})
		})
		queryAllActivity(1,10);
		$("input[name='date']").datetimepicker({
			language:'zh-CN', //语言
			format:'yyyy-mm-dd',//日期的格式
			minView:'month', //可以选择的最小视图
			initialDate:new Date(),//初始化显示的日期
			autoclose:true,//设置选择完日期或者时间之后，日否自动关闭日历
			todayBtn:true,//设置是否显示"今天"按钮,默认是false
			clearBtn:true//设置是否显示"清空"按钮，默认是false
		})
		
	});
	function queryAllActivity(pageNo,pageNum) {
		var name=$.trim($("#queryActivityName").val());
		var owner=$.trim($("#queryActivityOwner").val());
		var startDate=$("#queryActivityStartDate").val();
		var endDate=$("#queryActivityEndDate").val();
		$.ajax({
			url:"workbench/activity/queryActivities.do",
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageNum:pageNum
			} ,
			dataType: "json",
			type: "get",
			success:function (data) {
				$("#activityCount").text(data.count);
				var html="";
				$.each(data.activitiesList,function (i,obj) {
					html+="<tr class=\"active\">";
					html+="<td><input type=\"checkbox\" value=\""+obj.id+"\"/></td>";
					html+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detail.do?id="+obj.id+"'\">"+obj.name+"</a></td>"
					html+="<td>"+obj.owner+"</td>"
					html+="<td>"+obj.startDate+"</td>"
					html+="<td>"+obj.endDate+"</td>"
					html+="</tr>"
				})
				$("#tActivityList").html(html);
				var totalPages=1;
				if(data.count%pageNum==0){
					totalPages=data.count/pageNum;
				}else{
					totalPages=parseInt(data.count/pageNum)+1;
				}
				$("#demo_pag1").bs_pagination({
					currentPage:pageNo,//当前页号,相当于pageNo
					rowsPerPage:pageNum,//每页显示条数,相当于pageSize
					totalRows:data.count,//总条数
					totalPages: totalPages,  //总页数,必填参数.
					visiblePageLinks:5,//最多可以显示的卡片数
					showGoToPage:true,//是否显示"跳转到"部分,默认true--显示
					showRowsPerPage:true,//是否显示"每页显示条数"部分。默认true--显示
					showRowsInfo:true,//是否显示记录的信息，默认true--显示
					onChangePage:function(event,pageObj) {
						queryAllActivity(pageObj.currentPage,pageObj.rowsPerPage);
					}
				})
			}
		})
	}
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-Owner">
									<c:forEach items="${userList}" var="item">
										<option value="${item.id}">${item.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-ActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-ActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input  name="date" type="text" class="form-control" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input name="date" type="text" class="form-control" id="create-endTime"readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button  type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveCreateActivityBtn" type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form  id="editActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-Owner">
									<c:forEach items="${userList}" var="item">
										<option value="${item.id}">${item.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-Name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-Name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input name="date" type="text" class="form-control" id="edit-startDate" readonly>
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input name="date" type="text" class="form-control" id="edit-endDate" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="updateActivityBtn" type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input id="queryActivityName" class="form-control" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input  id="queryActivityOwner" class="form-control" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input  id="queryActivityStartDate"name="date" class="form-control" type="text" id="startTime" readonly/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input id="queryActivityEndDate" name="date" class="form-control" type="text" id="endTime" readonly>
				    </div>
				  </div>
				  
				  <button id="queryActivityBtn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" id="createActivityBtn" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" id="editActivityDivBtn" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" id="deleteActivityBtn"class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tActivityList">
						<!--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>-->
					</tbody>
				</table>
			</div>
			
			<!--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="activityCount">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>-->
			<div id="demo_pag1"></div>
		</div>
		
	</div>
</body>
</html>